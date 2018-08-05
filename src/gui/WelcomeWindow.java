
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
import javafx.beans.property.ReadOnlyIntegerProperty;
    
import gos.*;

public class WelcomeWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    private MovableButton okBtn;
    //private int mode = 0;
    //private boolean showWelcome = true; //false; // True to turn this back on for live mode.
    
    //private TextField m_tfId;
    //private TextField m_tfName;
    //private TextField m_tfEmail;
    //private TextField m_tfTimes;

    private CheckBox m_cbPlayed;
    //private CheckBox m_cbConsent;

    private WelcomeWindow thisScreen;

    public WelcomeWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;
        setup();
    }

    private Label createTextFieldLabel(String str) {
        Label lab = new Label(str);
        lab.setPrefWidth(150);
        lab.setAlignment(Pos.CENTER_RIGHT);
        return lab; 
    }

    private TextField addNewLabelTextField(String str) {
        Label lab = createTextFieldLabel(str);
        TextField tf = new TextField();
        tf.setPrefWidth(250);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER); // Vertically align to the center of the hbox
        hbox.getChildren().add(lab);
        hbox.getChildren().add(tf);
        m_MainVBox.getChildren().add(hbox);
        return tf;
    }

    private void setup() {
        int wid = m_nWidth;
        int hgt = m_nHeight;
        int space = 10;
        int labelWid = wid - 20;

        String str;
        VBox box = m_MainVBox;

        Utils.log(Player.getDataFileName());

        str = "Welcome to the Systems Thinking Game!";
        titleLabel = addCenteredLabel(str);
        Utils.addVerticalSpace(box, space);

        str = "";
        Info info = Data.getInfoByName("Welcome");
        if (info != null) {
            str = info.getText();
        }
        textLabel = addLeftLabel(str);
        Utils.addVerticalSpace(box, space);

        Label lab;
        HBox hbox;
        int labWidth = 500;
        //int labTfWidth = 200;
        //int tfWidth = 200;

        m_cbPlayed = new CheckBox("Please check this box if you have played the game before.");
        m_MainVBox.getChildren().add(m_cbPlayed);

        m_cbPlayed.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                /*if (newValue == true) {
                    m_tfId.setDisable(false);
                    m_tfTimes.setDisable(false);
                    m_tfTimes.setText("");
                    m_tfId.setText("");
                    // At this point the bottom part should appear, before this we
                    // shouldn't actually show the bottom part.  Or it could be a
                    // separate screen or something.  Like if you check the box
                    // then the next screen appears where you input your prior stuff.
                }
                else {
                    m_tfId.setDisable(true);
                    m_tfTimes.setDisable(true);
                    m_tfTimes.setText("0");
                    m_tfId.setText("" + Player.getId());
                    // If we did this then we should remove/hide the bottom section again.
                }*/
            }
        });

        Utils.addVerticalSpace(box, space);
        //Utils.addVerticalSpace(box, space);

        /*lab = new Label("To maintain anonymity, each player is tracked through an ID number.");
        lab.setWrapText(true);
        lab.setTextAlignment(TextAlignment.CENTER);
        lab.setPrefWidth(labWidth);
        m_MainVBox.getChildren().add(lab);*/

       /* lab = new Label("If you know your ID number, please enter it below.  If not, " +
          "a new one will be generated for you. ");
        lab.setWrapText(true);
        //lab.setPrefWidth(labWidth);
        //lab.setTextAlignment(TextAlignment.CENTER);
        m_MainVBox.getChildren().add(lab);
         
        m_tfId = addNewLabelTextField("ID Number (Optional): ");
        m_tfId.setText("" + Player.getId());*/
        
        // Now we need, text fields for ID, Name, and Email.
        // And how many times you've played through this simulation.
        // A consent box for "I am OK with being contacted 
        // regarding this test."
        // And a Generate ID button.
        /*MovableButton btn = new MovableButton("Generate ID");
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Gos.gos.showMainScreen();
                Player.recordButtonAction(event, thisScreen.className());
                onGenerateID();
            }
        });
        m_MainVBox.getChildren().add(btn);*/

        /*Utils.addVerticalSpace(box, space);
        
        lab = new Label("Please enter the number " +
            "of times you've played.");
        lab.setWrapText(true);
        lab.setPrefWidth(labWidth);
        m_MainVBox.getChildren().add(lab);

        m_tfTimes = addNewLabelTextField("Times Played: ");
        Utils.addVerticalSpace(box, space);
        m_tfTimes.setText("0");*/

        // Check this box if you consent to being contacted about your results.
        //Utils.addVerticalSpace(box, space);

    /*  lab = new Label("You may provide your name and/or email below. ");
        lab.setWrapText(true);
        lab.setPrefWidth(labWidth);
        m_MainVBox.getChildren().add(lab);

        m_cbConsent = new CheckBox("Check this box if you consent to being contacted about your results.");
        m_MainVBox.getChildren().add(m_cbConsent);

        m_tfName = addNewLabelTextField("Name (Optional): ");
        m_tfEmail = addNewLabelTextField("Email (Optional): ");*/

        // Do we want a consent box for "I am OK with being contacted 
        // regarding this test." 

        okBtn = new MovableButton("OK");
        okBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Gos.gos.showMainScreen();
                Player.recordButtonAction(event, thisScreen.className());
                onOK();
            }
        });
        m_MainVBox.getChildren().add(okBtn);

        // And make some text fields numeric only
        /*m_tfId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    m_tfId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        m_tfTimes.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    m_tfTimes.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        m_tfId.setDisable(true);
        m_tfTimes.setDisable(true);*/
    }


    @Override
    public void showAndWait() {
        if (Gos.testing == true) {
            return;
        }

        super.showAndWait();
    }

    private void onOK() {
        /*String strId = m_tfId.getText();

        // We must have an ID, although I could just generate one if
        // this one isn't good.
        if (strId == null || strId.equals("") == true) {
            // Actually we've already generated one so no need to do
            // anything, but we'll update the screen.
            m_tfId.setText("" + Player.getId());
        }
        else {
            // I'll just use whatever they type.  Even if it isn't
            // the "right number of numbers" who cares, it just needs
            // to have a very high probability of being unique.
            Player.setId(Utils.tryParseInt(strId));
        }

        //Player.setName(m_tfName.getText());
        //Player.setEmail(m_tfEmail.getText());
        Player.setTimesPlayed(Utils.tryParseInt(m_tfTimes.getText()));*/
        Player.setPlayedBefore(m_cbPlayed.isSelected());
            
        close();

        // Maybe now we should pop up the ID window to say here is what your
        // generate ID is so keep track of this for future iterations.  Or
        // just pop it up at the end, like at the final screen, if you want
        // to play again, please use this ID.
    }

    /*private void onGenerateID() {
        Player.generateId();
        m_tfId.setText("" + Player.getId());
    }*/
}