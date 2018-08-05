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

public class InquirePanelBottom extends PanelBottomBase {

    InquirePanelSet m_ParentPanelSet;
    
    private InquirePanelBottom thisScreen;

    public InquirePanelBottom(InquirePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        create();
    }

    private void setupGridPane() {
        ColumnConstraints col;
        
        col = new ColumnConstraints();
        col.setPercentWidth(20);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(20);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(20);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(20);
        getColumnConstraints().add(col);
        
        col = new ColumnConstraints();
        col.setPercentWidth(20);
        getColumnConstraints().add(col);
    }

    private void create() {
        setupGridPane();
        MovableButton btn;

        btn = new MovableButton("Main Screen");
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                m_ParentPanelSet.onCancelButton();
            }
        });
        getChildren().add(btn);
        GridPane.setRowIndex(btn, 0);
        GridPane.setColumnIndex(btn, 0);
        GridPane.setHalignment(btn, HPos.CENTER);
    }
}