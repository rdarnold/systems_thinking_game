
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

public class PanelTopBase extends ActionPanelBase {

    private HBox m_MainHBox;

    protected ScreenInfoArea infoArea;
    public ScreenInfoArea getInfoArea() { return infoArea; }

    protected int instructionTextWidth = 750;

    public PanelTopBase() {
        super();
        setStyle("-fx-border-color: darkgray;");
        setPrefHeight(195);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        setSpacing(5);

        m_MainHBox = new HBox();
        super.getChildren().add(m_MainHBox);
        
        infoArea = new ScreenInfoArea("");
        m_MainHBox.getChildren().add(infoArea);
        m_MainHBox.setSpacing(5);
        
        // Create a new m_MainVBox.  This is used by the parent class
        // as the main vbox upon which to add children nodes.
        setMainVBox(new VBox()); 
        m_MainVBox.setPadding(new Insets(5));
        m_MainHBox.getChildren().add(m_MainVBox);
        
        setStyle("-fx-background-color: #E0EEEE; " + 
                 "-fx-border-color: black; " +
                 "-fx-border-radius: 3; " +
                 "-fx-border-width: 2;");
    }
    
    public void addToRightSide(Node node) {
        m_MainVBox.getChildren().add(node);
    }

    // So here's how we do this, we actually override getChildren to 
    // return our vbox that is in the RIGHT side of our main hbox rather
    // than "this" object itself.  That way we can always have the
    // info area showing to the left side for consistency.
    /*@Override
    public ObservableList<Node> getChildren() {
        return m_MainVBox.getChildren();
    }*/
    
    // To be overridden if necessary.
    public void update() { 
        infoArea.update();
    }
    public void reset() { }
    public void updateOneFrame(boolean running, Turn currentTurn) { }
    public void updateOneTurn(Turn currentTurn) { }
}