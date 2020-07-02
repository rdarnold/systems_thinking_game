package gos.gui;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Screen; 
import javafx.stage.Modality;
import javafx.stage.StageStyle;
/*import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;*/
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressBar;


// Figure out what I don't need later
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
    
import gos.*;

public class BetaWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    //private Label uploadingLabel;
    private TextArea uploadingText;
    private MovableButton okBtn;
    private MovableButton altBtn;
    private ProgressIndicator pi;
    private ProgressBar pb;

    private int mode = 0;
    
    private BetaWindow thisScreen;

    private int failures = 0;

    public BetaWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;
    }

    @Override
    public void showAndWait() {
        removeTempControls();

        int wid = m_nWidth;
        int hgt = m_nHeight;
        int space = 10;
        int labelWid = wid - 20;

        String str;
        VBox box = m_MainVBox;

        //str = "You Win!";
        //titleLabel = addCenteredLabel(str);
        //Utils.addVerticalSpace(box, space);

        str = "Congratulations, you have reached the end of the game.  Thank you for playing!\r\n\r\n" +
        "If you play the game again, please input your ID on the welcome screen.  This will help us " +
        "track your research data, such as trends and differences in gameplay decisions.  Also, this " +
        "will allow you to skip all the survey questions.";
        textLabel = addCenteredLabel(str);
       // Utils.addVerticalSpace(box, space);

        // Add the "ID" next to the copyable part
        TextField copyable = new TextField("ID: " + Player.getId());
        copyable.setEditable(false);
        copyable.getStyleClass().add("copyable-label");
        copyable.setMaxWidth(m_nWidth - 20);
        copyable.setAlignment(Pos.CENTER);
        m_MainVBox.getChildren().add(copyable);
        Utils.addToolTip(copyable, "Please use this ID if you want to replay the game.");

        str = "Before closing the game, please wait as your gameplay data is uploaded to the research server.";
        //uploadingLabel = addCenteredLabel(str);
        //Utils.addVerticalSpace(box, space);
        uploadingText = new TextArea();
        uploadingText.setWrapText(true);
        uploadingText.setText(str);
        uploadingText.setEditable(false);
        uploadingText.setPrefHeight(200);
        addTempControl(uploadingText);


        /*str = "Uploading...";
        uploadingLabel = addCenteredLabel(str);
        Utils.addVerticalSpace(box, space);*/

        //pi = new ProgressIndicator(0);
        //addTempControl(pi);
        pb = new ProgressBar(-1);
        pb.setPrefWidth(Constants.BUTTON_WIDTH);
        //pi = new ProgressIndicator(-1);

        //pb.progressProperty().bind(FileTransfer.uploadProgressProperty());
        //pi.progressProperty().bind(FileTransfer.uploadProgressProperty());
        addTempControl(pb);
        //addTempControl(pi);
            
        // This is a little nuts but it actually works.  The problem here is,
        // we run the upload on a separate thread.  When it updates the uploadSuccess
        // property it does so on that thread, so this listener is called from that thread.
        // But we need to update UI elements on the main thread.  The way we fix this is to
        // store the value in an threadsafe AtomicInteger, then call Platform.runLater which 
        // actually runs back on the main thread.
        final AtomicInteger success = new AtomicInteger(0);
        FileTransfer.uploadSuccessProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                success.set(new_val.intValue());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (success.get() == 1) {
                            pb.setProgress(1);
                            uploadingText.setText("Upload complete.");
                            okBtn.setDisable(false);
                            okBtn.setText("OK");
                        }
                        else if (success.get() == 2) {
                            pb.setProgress(0);
                            failures++;
                            if (failures >= 2) {
                                Player.copyDataToClipboard();
                                String strSavedTo = Player.saveDataToFile();

                                uploadingText.setText("Unable to upload.  There may be " +
                                "firewall issues or other network constraints.  Your results " +
                                "have been saved to file here: " + strSavedTo + ".  Please " + 
                                "attach this file " +
                                "to an email and send it to systemsthinkingtest@gmail.com.  If " +
                                "this is not done, your results may not be processed and your efforts " +
                                "in the game may have been in vain!  Sorry for the inconvenience!");
                                okBtn.setDisable(false);
                                okBtn.setText("I have sent the email");
                                okBtn.setPrefWidth(300);

                                altBtn = new MovableButton("The file was not created / I can't find it");
                                altBtn.setPrefWidth(300);
                                altBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        Player.recordButtonAction(event, thisScreen.className());
                                        onAltButton();
                                    }
                                });
                                m_MainVBox.getChildren().add(altBtn);
                            }
                            else {
                                uploadingText.setText("Upload failed.  Please retry.");
                                okBtn.setDisable(false);
                                okBtn.setText("Retry");
                            }
                        } 
                    }
                }); 

            }
        });

        okBtn = new MovableButton("OK");
        okBtn.setDisable(true);
        okBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // We're done here; we've already uploaded the final stuff
                //Player.recordButtonAction(event, thisScreen.className());
                if (failures == 1) {
                    pb.setProgress(-1);
                    FileTransfer.runUploadThread();
                }
                else {
                    close();
                    System.exit(0);
                }
            }
        });
        addTempControl(okBtn);
        
        super.showAndWait();
    }

    private void onAltButton() {
        Gos.betaWindow.close();
        AltDataCollectionWindow window = new AltDataCollectionWindow(400, 500);
        window.showAndWait();
        System.exit(0);
    }
}