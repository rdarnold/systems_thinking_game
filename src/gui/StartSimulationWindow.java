
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

public class StartSimulationWindow extends DialogWindow {

    private Label titleLabel;
    private Label textLabel;
    private MovableButton okBtn;
    private int mode = 0;
    private boolean showWelcome = true; //false; // True to turn this back on for live mode.
    
    private StartSimulationWindow thisScreen;

    public StartSimulationWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;
    }

    @Override
    public void showAndWait() {
        if (showWelcome == false) {
            Gos.stage.show();
            Gos.mainScene.showFirstPanelSet(); 
            return;
        }
        removeTempControls();

        // Actually when we do this, let's hide the main stage so that it looks
        // better.  We show it automatically when we transition from this into the game.
        // Also let's switch to the main scene now so we don't show the only thing
        // in the background.
        Gos.stage.hide();
        Gos.showMainScreen();

        int wid = m_nWidth;
        int hgt = m_nHeight;
        int space = 10;
        int labelWid = wid - 20;

        String str = "";
        VBox box = m_MainVBox;

        if (Gos.skipSurveys == false) {
            str = "Thank you for completing the surveys!";
            titleLabel = addCenteredLabel(str);
            Utils.addVerticalSpace(box, space);
        }

        str = "";
        Info info = Data.getInfoByName("Introduction");
        if (info != null) {
            str = info.getText();
        }
        textLabel = addLeftLabel(str);
        Utils.addVerticalSpace(box, space);

        // Add the ID now
        addCenteredLabel("Your personal ID is:");
        
        TextField copyable = new TextField("" + Player.getId());
        copyable.setEditable(false);
        copyable.getStyleClass().add("copyable-label");
        copyable.setMaxWidth(m_nWidth - 20);
        copyable.setAlignment(Pos.CENTER);
        m_MainVBox.getChildren().add(copyable);

        addCenteredLabel("Please use this ID if you decide to replay the game; it will allow\r\nyou to skip all of the survey questions, and helps with data collection.");
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
        addTempControl(okBtn);

        super.showAndWait();
    }

    private void onOK() {
            
        close();

        // Now finally show the main stage, if it isn't showing already.
        //Gos.stage.show();

        // Ok so for now, we are actually going to skip to the observation
        // screen from here.
        //Gos.mainScene.showObservePanelSet(); 
    }

    // Don't transition on the close but rather on the completion of the
    // fadeout.
    @Override
    protected void onFadeOutFinished() {
        super.onFadeOutFinished();
        
        // Now finally show the main stage, if it isn't showing already.
        Gos.stage.show();

        // Ok so for now, we are actually going to skip to the observation
        // screen from here.
        Gos.mainScene.showFirstPanelSet(); 
    }
    
    public void addOneInfoItem(Accordion accordion, Info info) {
        VBox box = new VBox();
        VBox.setVgrow(box, Priority.ALWAYS);
        box.setPrefWidth(m_nWidth - 40);
        box.setMaxWidth(m_nWidth - 40);
        Text text = new Text(info.getText());
        text.setWrappingWidth(m_nWidth - 40);
        box.getChildren().add(text);
        Utils.addTitledPane(accordion, box, info.getName(), this.className());
    }
}