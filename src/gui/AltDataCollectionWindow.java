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
    
import gos.*;

public class AltDataCollectionWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    public Label getTextLabel() { return textLabel; }
    public Label getTitleLabel() { return titleLabel; }
    
    private MovableButton okBtn;
    
    private AltDataCollectionWindow thisScreen;


    /*public OKWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;
    }*/
    public AltDataCollectionWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;
        setup();
    }

    public void setup() {
        int wid = m_nWidth;
        int hgt = m_nHeight;
        int space = 10;
        int labelWid = wid - 20;

        VBox box = m_MainVBox;

        /*TextField copyable = new TextField("ID: " + Player.getId());
        copyable.setEditable(false);
        copyable.getStyleClass().add("copyable-label");
        copyable.setMaxWidth(m_nWidth - 20);
        copyable.setAlignment(Pos.CENTER);
        m_MainVBox.getChildren().add(copyable);
        Utils.addToolTip(copyable, "Please use this ID if you want to replay the game.");*/

        String strTitle = "Alterate Data Collection";
        titleLabel = addCenteredLabel(strTitle);
        Utils.addVerticalSpace(box, space);

        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        String strText = 
        "If the upload has failed, and the file " + Constants.SAVED_DATA_FILE_NAME + " either has not saved correctly " +
        "to your My Documents or Home folder or you can't find it, there is one " +
        "more alternative.  Your research data has been copied to the clipboard.  If you " +
        "compose a new email right now, you can select Edit->Paste into the main body, " +
        "or click inside the main body and press Ctrl+V.  A long block of text should " +
        "paste into the email.  Just set the subject as 'Systems Thinking Data' and " +
        "send it to systemsthinkingtest@gmail.com.  If the data is lost, you can click " +
        "the Copy Data button to copy it again and try again.  If this still does not work, " +
        "please email us and let us know what happened so we can correct the problem " +
        "in the future.  Thank you!";
        textLabel = addLeftLabel(strText);
        Utils.addVerticalSpace(box, space);

        MovableButton btn = new MovableButton("Copy Data");
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                Player.copyDataToClipboard();
            }
        });
        addTempControl(btn);

        okBtn = new MovableButton("Close");
        okBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                close();
            }
        });
        addTempControl(okBtn);
    }

    @Override
    public void showAndWait() {
        // Copy the latest so we capture this button press
        Player.copyDataToClipboard();
        super.showAndWait();
    }

    // Text left-aligns by default but we can center it if we want.
    public void centerText() {
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setTextAlignment(TextAlignment.CENTER);
    }
}