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
import javafx.beans.binding.Bindings;

import gos.*;

public class ChangePanelBottom extends PanelBottomBase {

    ChangePanelSet m_ParentPanelSet;
    
    private ChangePanelBottom thisScreen;

    private Label turnText;

    public ChangePanelBottom(ChangePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        create();
    }

    private void setupGridPane() {
        ColumnConstraints col;
        
        col = new ColumnConstraints();
        col.setPercentWidth(25);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(23);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(4);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(23);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(25);
        getColumnConstraints().add(col);
    }

    private void create() {
        setupGridPane();
        MovableButton btn;

        btn = new MovableButton("Submit");
        //btn.moveTo(0, 230);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Player.inPracticeMode() == false) {
                    Gos.variableWindow.showAndWait();
                }
                Player.recordButtonAction(event, thisScreen.className());
                m_ParentPanelSet.onSubmitButton(thisScreen.className());
                //FileTransfer.runUploadThread();
            }
        });
        getChildren().add(btn);
        GridPane.setRowIndex(btn, 0);
        GridPane.setColumnIndex(btn, 1);
        GridPane.setHalignment(btn, HPos.RIGHT);

        btn = new MovableButton("Cancel");
        //btn.moveTo(0, 200);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                m_ParentPanelSet.onCancelButton();
            }
        });
        getChildren().add(btn);
        GridPane.setRowIndex(btn, 0);
        GridPane.setColumnIndex(btn, 3);
        GridPane.setHalignment(btn, HPos.LEFT);
        
        turnText = new Label();
        turnText.setMinWidth(Constants.BUTTON_WIDTH / 2);
        turnText.setAlignment(Pos.CENTER);
        turnText.setStyle("-fx-font-weight: bold; ");
        getChildren().add(turnText);
        GridPane.setRowIndex(turnText, 0);
        GridPane.setColumnIndex(turnText, 4);
        GridPane.setHalignment(turnText, HPos.RIGHT);

        turnText.textProperty().bind(Bindings.createStringBinding(() -> 
            "Turn " + Player.getCurrentTurnNumberDisplay() + " / " + Player.getMaxTurns(),
            Player.currentTurnNumberProperty(), 
            Player.getPlayedTurns().maxTurnsProperty()));

        /*turnText.textProperty().bind(Bindings.createStringBinding(() -> 
            "Turn " + (Player.getPlayedTurns().getTurnNumber() + 1) + " / " + 
            Player.getPlayedTurns().getMaxTurns(),
            Player.getPlayedTurns().turnNumberProperty(), 
            Player.getPlayedTurns().maxTurnsProperty()));*/
    }
}