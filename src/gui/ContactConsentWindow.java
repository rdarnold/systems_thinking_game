
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

public class ContactConsentWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    private MovableButton okBtn;
    //private int mode = 0;
    //private boolean showWelcome = true; //false; // True to turn this back on for live mode.
    
    private TextField m_tfName;
    private TextField m_tfEmail;

    private CheckBox m_cbConsent;

    private ContactConsentWindow thisScreen;

    public ContactConsentWindow(int wid, int hgt) {
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

        str = "Consent to Contact";
        titleLabel = addCenteredLabel(str);
        Utils.addVerticalSpace(box, space);

        str = "If you consent to being contacted about your gameplay data and results, " +
         "please fill out the name and/or email fields below.  Any personally identifiable " +
         "data you provide will be protected and stored offline.";
        textLabel = addLeftLabel(str);
        Utils.addVerticalSpace(box, space);

        m_cbConsent = new CheckBox("Check this box if you consent to being contacted.");
        m_MainVBox.getChildren().add(m_cbConsent);

        m_tfName = addNewLabelTextField("Name (Optional): ");
        m_tfEmail = addNewLabelTextField("Email (Optional): ");

        Utils.addVerticalSpace(box, space);

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
        Player.setName(m_tfName.getText());
        Player.setEmail(m_tfEmail.getText());
        Player.setContactConsent(m_cbConsent.isSelected());
            
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