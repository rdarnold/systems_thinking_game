
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

public class SkipToWindow extends DialogWindow {

    private SkipToWindow thisScreen;

    public SkipToWindow(int wid, int hgt) {
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

    private void addSkipButton(String text, int num) {
        MovableButton btn = new MovableButton(text);
        btn.setPrefWidth(250);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onSkip(num);
            }
        });
        m_MainVBox.getChildren().add(btn);
    }

    private void setup() {
        int wid = m_nWidth;
        int hgt = m_nHeight;
        int space = 10;
        int labelWid = wid - 20;

        String str;
        VBox box = m_MainVBox;
        MovableButton btn;
        
        addCenteredLabel("TEST MODE");
        addCenteredLabel("Skip To Section");
        Utils.addVerticalSpace(box, space);

        // demo survey; career survey; exercise 2, play; 3, growth 1/2; 4, exploration 1/2; 5, mastery 1/2; final questions; final upload
        int num = 0;
        addSkipButton("Demographic Survey", num++);
        addSkipButton("Career Survey", num++);
        addSkipButton("1-1: Practice", num++);
        addSkipButton("2-1: Four Shapes (1)", num++);
        addSkipButton("2-2: Four Shapes (2)", num++);
        //addSkipButton("3-1: Eight Shapes (1)", num++);
        //addSkipButton("3-2: Eight Shapes (2)", num++);
        addSkipButton("3-1: Chaos (1)", num++);
        addSkipButton("3-2: Chaos (2)", num++);
        addSkipButton("Feedback Questions", num++);

        btn = new MovableButton("Cancel");
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onCancel();
            }
        });
        m_MainVBox.getChildren().add(btn);
    }

    private boolean onCancel() {
        // Close the window
        close();
        return true;
    }

    private boolean onSkip(int nNum) {
        switch (nNum) {
            // demo survey
            case 0:
                //Gos.simRunner.skipToExercise(0);
                break;
            // career survey
            case 1:
                //Gos.simRunner.skipToExercise(1);
                Gos.simRunner.finishExercise();
                break;
            // exercise 2, play;
            case 2:
                Gos.simRunner.skipToExercise(2);
                break;
            // 3, growth 1
            case 3:
                Gos.simRunner.skipToExercise(3);
                break;
            // growth 2
            case 4:
                Gos.simRunner.skipToExercise(3);
                Player.nextTask();
                break;
            // 4, exploration 1
            /*case 5:
                Gos.simRunner.skipToExercise(4);
                break;
            // 4, exploration 2
            case 6:
                Gos.simRunner.skipToExercise(4);
                Player.nextTask();
                break;*/
            // mastery 1
            case 5:
                Gos.simRunner.skipToExercise(4);
                break;
            // mastery 2
            case 8:
                Gos.simRunner.skipToExercise(4);
                Player.nextTask();
                break;
            // final questions
            case 9:
                Gos.simRunner.skipToExercise(4);
                Player.nextTask();
                Gos.simRunner.finishExercise();
                break;
        }
        Player.setSkipped();
        close();
        return true;
    }

    @Override
    public void showAndWait() {
        if (Gos.testing == true) {
            return;
        }

        super.showAndWait();
    }
}