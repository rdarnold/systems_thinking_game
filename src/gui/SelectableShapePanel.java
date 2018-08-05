package gos.gui;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.Event;
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
import javafx.stage.Stage;
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

import gos.*;
// A left panel with a selectable shape, so you can click on a shape and it
// puts it in a pane at the top of the panel.  Base for several other
// types of panels.
public class SelectableShapePanel extends PanelLeftBase {

    protected SysShape selected = null;
    protected Pane shapePane = null;

    public SysShape getSelected() { return selected; }

    public void setup() {

        if (shapePane == null) {
            shapePane = new Pane();
            shapePane.setPrefWidth(width);
            shapePane.setPrefHeight(SysShape.DEFAULT_MAX_SIZE + 5);
            //shapePane.setPadding(new Insets(0));
            //shapePane.setStyle("-fx-background-color: blue;");
            getChildren().add(shapePane);
        }

        // We should show the currently selected shape.  And it should have some
        // kind of outline around it too on screen.
        setSelectedPanelShape();
    }

    // Meant to be overridden
    public void setShapeValues() { }

    @Override
    public void updateOneFrame(boolean running, Turn currentTurn) {
        if (selected != null) {
            selected.updateOneFrame(false, Gos.sim);
        }
    }

    protected void setSelectedPanelShape() {
        if (shapePane == null) {
            return;
        }
        // Remember this has to literally be a different shape, because we dont have
        // meta-shapes or anything like that.  The other shape is literally the thing
        // being drawn on screen so we cant also draw it here as that would change
        // the location.
        if (selected != null) {
            shapePane.getChildren().remove(selected);
        }
        if (Player.getSelectedShape() == null) {
            return;
        }
        selected = new SysShape(Player.getSelectedShape());
        selected.setSpeed(0, 0);
        selected.moveTo(shapePane.getPrefWidth()/2, shapePane.getPrefHeight()/2);
        shapePane.getChildren().add(selected);

        // This is now called in the subclass
        setShapeValues();
    }
}