
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

public class PlayedBeforeWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    private MovableButton okBtn;
    
    private TextField m_tfId;
    private TextField m_tfTimes;

    private PlayedBeforeWindow thisScreen;

    public PlayedBeforeWindow(int wid, int hgt) {
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

        str = "Player Info";
        titleLabel = addCenteredLabel(str);
        Utils.addVerticalSpace(box, space);

        str = "You have indicated that you have played this game before.  If you " +
        "know your ID number, please enter it below so that we can compare your results " +
        "of this play-through to your previous ones.  If you don't know your ID, a new one will be generated " +
        "for you.  Also, please enter the number of times you've played.  This helps us " +
        "to understand how multiple play-throughs affect play style.";
        textLabel = addLeftLabel(str);
        Utils.addVerticalSpace(box, space);
         
        m_tfId = addNewLabelTextField("ID Number: ");
        //m_tfId.setText("" + Player.getId());

        m_tfTimes = addNewLabelTextField("Times Played: ");
        //m_tfTimes.setText("1");

        Utils.addVerticalSpace(box, space);

        // And make some text fields numeric only
        m_tfId.textProperty().addListener(new ChangeListener<String>() {
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
    }


    @Override
    public void showAndWait() {
        if (Gos.testing == true) {
            return;
        }

        super.showAndWait();
    }

    private void onOK() {
        String strId = m_tfId.getText();

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

        Player.setTimesPlayed(Utils.tryParseInt(m_tfTimes.getText()));
            
        close();
    }

    /*private void onGenerateID() {
        Player.generateId();
        m_tfId.setText("" + Player.getId());
    }*/
}