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
import javafx.stage.Stage;
/*import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import gos.*;

public class SysPane extends Pane {
    
    public ArrayList<SysShape> shapes = new ArrayList<SysShape>();
    public ArrayList<Raindrop> drops = new ArrayList<Raindrop>();
    public ArrayList<Spike> spikes = new ArrayList<Spike>();
    public ArrayList<Earthpatch> patches = new ArrayList<Earthpatch>();

    public void create() {
        VBox.setVgrow(this, Priority.ALWAYS);

        // Add our outline to the playing area
        Line line = new Line(Constants.SIM_WIDTH, 0, Constants.SIM_WIDTH, Constants.SIM_HEIGHT);
        line.setStrokeWidth(4);
        getChildren().add(line);
        
        line = new Line(0, Constants.SIM_HEIGHT, Constants.SIM_WIDTH, Constants.SIM_HEIGHT);
        line.setStrokeWidth(4);
        getChildren().add(line);

        // And beyond the lines I guess I'll just add like a big gray shape or something... kind of
        // weird but should work.
        Rectangle rec = new Rectangle();
        rec.setFill(Color.LIGHTGRAY);
        rec.setStroke(Color.LIGHTGRAY);
        rec.setX(Constants.SIM_WIDTH);
        rec.setY(0);
        rec.setWidth(1200);
        rec.setHeight(1200);
        getChildren().add(rec);

        rec = new Rectangle();
        rec.setFill(Color.LIGHTGRAY);
        rec.setStroke(Color.LIGHTGRAY);
        rec.setX(0);
        rec.setY(Constants.SIM_HEIGHT);
        rec.setWidth(1200);
        rec.setHeight(1200);
        getChildren().add(rec);
    }

    public void addShape(SysShape shape) {
        shapes.add(shape);
        // Add it to the scene graph
        getChildren().add(shape);
        getChildren().add(shape.getShapeText());
        getChildren().add(shape.getSelectedCircle());
        //if (shape.drawBounds == true) {
          //  getChildren().add(shape.getBoundingCircle());
        //}
        if (shape.getDrawRangeIsOn() == true) {
            getChildren().add(shape.getShareRangeCircle());
        }
    }

    public void removeShape(SysShape shape) {
        removeShapeData(shape);
        shapes.remove(shape);
    }

    public void removeShapeData(SysShape shape) {
        //if (shape.drawBounds == true) {
          //  getChildren().remove(shape.getBoundingCircle());
        //}
        if (shape.getDrawRangeIsOn() == true) {
            getChildren().remove(shape.getShareRangeCircle());
        }
        getChildren().remove(shape.getSelectedCircle());
        getChildren().remove(shape.getShapeText());
        getChildren().remove(shape);
    }

    public void addRaindrop(Raindrop drop) {
        drops.add(drop);
        // Add it to the scene graph
        getChildren().add(drop);
    }

    public void removeRaindrop(Raindrop drop) {
        getChildren().remove(drop);
        drops.remove(drop);
    }

    public void addSpike(Spike spike) {
        getChildren().add(spike);
        spikes.add(spike);
    }

    public void removeSpike(Spike spike) {
        getChildren().remove(spike);
        spikes.remove(spike);
    }

    public void addEarthpatch(Earthpatch patch) {
        getChildren().add(patch);
        patches.add(patch);
    }

    public void removeEarthpatch(Earthpatch patch) {
        getChildren().remove(patch);
        patches.remove(patch);
    }
    
    public void reset() {
        // Must do these one at a time since they have
        // additional stuff like text, bounding circle.
        for (SysShape shape : shapes) {
            removeShapeData(shape);
        }
        // These can be done in batch.
        getChildren().removeAll(drops);
        getChildren().removeAll(spikes);
        getChildren().removeAll(patches);
        shapes.clear();
        drops.clear();
        spikes.clear();
        patches.clear();
    }
}