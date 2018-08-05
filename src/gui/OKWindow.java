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

public class OKWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    public Label getTextLabel() { return textLabel; }
    public Label getTitleLabel() { return titleLabel; }
    
    private MovableButton okBtn;
    private int mode = 0;

    private boolean shown = false;
    
    private OKWindow thisScreen;


    /*public OKWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;
    }*/
    public OKWindow(int wid, int hgt, String strTitle, String strText) {
        super(wid, hgt);
        thisScreen = this;
        setup(strTitle, strText);
    }

    public void setup(String strTitle, String strText) {
        removeTempControls();
        m_MainVBox.getChildren().removeAll();
        
        int wid = m_nWidth;
        int hgt = m_nHeight;
        int space = 10;
        int labelWid = wid - 20;

        VBox box = m_MainVBox;

        titleLabel = addCenteredLabel(strTitle);
        Utils.addVerticalSpace(box, space);

        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        textLabel = addLeftLabel(strText);
        Utils.addVerticalSpace(box, space);

        okBtn = new MovableButton("OK");
        okBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                close();
            }
        });
        addTempControl(okBtn);
    }

    // Text left-aligns by default but we can center it if we want.
    public void centerText() {
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setTextAlignment(TextAlignment.CENTER);
    }

    public void setText(String str) {
        textLabel.setText(str);
    }

    @Override
    public void showAndWait() {
        // We only show once.
        if (shown == true) {
            return;
        }
        shown = true;

        super.showAndWait();
    }

    // We can override the "shown" boolean if we want to force
    // a show, like on a button click or whatnot.
    public void showAndWait(boolean override) {
        if (override == true) {
            super.showAndWait();
        }
        else {
            showAndWait();
        }
    }
}