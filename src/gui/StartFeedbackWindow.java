
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

public class StartFeedbackWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    private MovableButton okBtn;
    private MovableButton skipButton;
    //private int mode = 0;
    //private boolean showWelcome = true; //false; // True to turn this back on for live mode.
    
    //private TextField m_tfId;
    //private TextField m_tfName;
    //private TextField m_tfEmail;
    //private TextField m_tfTimes;

    private CheckBox m_cbPlayed;
    //private CheckBox m_cbConsent;

    private StartFeedbackWindow thisScreen;

    public StartFeedbackWindow(int wid, int hgt) {
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
        
        // We haven't started yet so when we show this screen, we're on the survey exercise.
        titleLabel = addCenteredLabel("Option to End Simulation");
        Utils.addVerticalSpace(box, space);

        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        str = "You have indicated that you have played the simulation before.  If you previously " +
        "filled out all the feedback questions, completed the Systems Thinking self-assessment " +
        "after the simulation, AND you used the same ID and/or the same name to play this time, you may end the game here.  However, if you have not " +
        "completed those sections during a previous play-through, or you have used a different name and ID, please click the Continue button to proceed " +
        "to the feedback and self-assessment sections.  Thank you!";
        textLabel = addLeftLabel(str);
        Utils.addVerticalSpace(box, space);

        okBtn = new MovableButton("Continue");
        okBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onOK();
            }
        });
        m_MainVBox.getChildren().add(okBtn);

        skipButton = new MovableButton("End Game");
        skipButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onEndButton();
            }
        });
        m_MainVBox.getChildren().add(skipButton);
    }

    private boolean onEndButton() {
        Gos.simRunner.finishSim();
        //Player.setCurrentExercise(null); 
        //Gos.simRunner.finishExercise();

        // Close the window
        close();
        return true;
    }

    @Override
    public void showAndWait() {
        super.showAndWait();
    }

    private void onOK() {
        close();
    }
}