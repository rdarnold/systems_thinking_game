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

public class AdjustShapeValuesPanel extends SelectableShapePanel implements AdjustValuesInterface {

    // Add more configs of different sets of controls here if needs be.
    enum Configs {
        None,
        Default;
    }

    ChoiceBox cbCorner;
    ChoiceBox cbSpin;
    ColorPicker picker;
    Slider sizeSlider;
    Slider spinSlider;
    //Button btnSac;
    
    ActionPanelVBox avbCorner;
    ActionPanelVBox avbSpin;
    ActionPanelVBox avbSpinSpeed;

    // Which config of change panel?  If the config is the same we don't need to remove and re-add
    // everything.
    private Configs config = Configs.None; 

    public void setup(Configs configType) {
        clearStyles(controlsList);
        if (config == configType) {
            setSelectedPanelShape();
            return;
        }
        
        config = configType;

        removeControls();

        super.setup();
        
        spinSlider = createSlider();
        avbSpinSpeed = addSliderControl(spinSlider, "Spin Speed");
        spinSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    selected.setSpinSpeedPercent(new_val.doubleValue());
                }
        });
        
        cbSpin = new ChoiceBox();
        avbSpin = addChoiceBox(cbSpin, "Spin Direction");
        cbSpin.getItems().add("Normal");
        cbSpin.getItems().add("Reverse");
        cbSpin.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, 
                    Number number, Number number2) {
                if ((Integer)number2 == 0) {
                    selected.setSpinRight();
                } else {
                    selected.setSpinLeft();
                }
            }
        });

        sizeSlider = createSlider();
        /*addSliderControl(sizeSlider, "Size");
        sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    // Lets say between half and 1.5x
                    //double var = 0.5 + (new_val.doubleValue() / 100F);
                    //selected.setSize(baseSize * var);
                    selected.setSizePercent(new_val.doubleValue());
                }
        });*/

        cbCorner = new ChoiceBox();
        avbCorner = addChoiceBox(cbCorner, "Type");
        cbCorner.getItems().setAll("Triangle", "Square", "Pentagon", "Hexagon");
        cbCorner.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, 
                    Number old_val, Number new_val) {
                int newCorners = (Integer)new_val + 3;
                if (selected.getNumCorners() != newCorners) {
                    selected.makeShape(newCorners);
                }
            }
        });
        
        picker = new ColorPicker((Color)selected.getFill());
        /*addColorControl(picker, "Color");
        picker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                Color c = (Color)picker.getValue();
                selected.setFill(c);
            }
        });*/

        //btnSac = new Button("Sacrifice");
        /*addButton(btnSac);
        btnSac.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                onClickSacrifice();
            }
        });*/

        setStyles(); // This is for the controls, not the vboxes
        addToolTips();
        clearStyles(controlsList); // This clears styles from the vboxes
        setValues();
        setupListeners(localControlsList);
    }
    
    public void addToolTips() {
        String str = "The spin speed of a shape has an effect on how it interacts with " +
        "other parts of the system.";
        avbSpinSpeed.addToolTip(str);

        str = "The effect of the spin direction is tied to the spin speed of the shape.";
        avbSpin.addToolTip(str);

        str = "Each type of shape (triangle, square, etc.) has a different special trait.";
        avbCorner.addToolTip(str);
    }

    private void setStyles() {
        spinSlider.getStyleClass().add("gos-slider");
        cbSpin.getStyleClass().add("gos-cb");
        cbCorner.getStyleClass().add("gos-cb");
    }

    @Override
    public void onChanged(Node node) { 
        setValues(node);
    }

    @Override
    public void setShapeValues() {
        setValues();
    }

    public void setValues() {
        setValues(null);
    } 

    /*private void onClickSacrifice() {
        Gos.sim.sacrificeShapeForSpikes(Player.getSelectedShape());
    }*/

    public void setToCurrentValues() {
        if (selected == null) {
            return;
        }
        SysShape shape = Player.getSelectedShape();

        selected.makeShape(shape.getNumCorners());
        cbCorner.getSelectionModel().select(selected.getNumCorners() - 3);
        selected.setSpin(shape.getSpin());
        if (selected.getSpinRight() == true) {
            cbSpin.getSelectionModel().selectFirst();
        } else {
            cbSpin.getSelectionModel().select(1);
        }
        selected.setFill((Color)shape.getFill());
        picker.setValue((Color)selected.getFill());
        selected.setSizePercent(shape.getSizePercent());
        sizeSlider.setValue(selected.getSizePercent());
        selected.setSpinSpeedPercent(shape.getSpinSpeedPercent());
        spinSlider.setValue(selected.getSpinSpeedPercent());
    }

    // When we change anything, there are times when we only allow one change
    // per turn so we would have to change everything back.
    public void setValues(Node node) {
        if (selected == null) {
            return;
        }
        // We may not have created our controls yet so make sure we have them.
        if (cbCorner == null) {
            return;
        }
        if (node == null) {
            setToCurrentValues();
            return;
        }

        // If the change isnt allowed we also have to reset the selected shape
        // value for whatever attribute to the player selected shape.
       // SysShape shape = Player.getSelectedShape();

        
        // So if any of these changeAllowed things are TRUE, that means that this
        // was NOT the value that was just changed, - we just changed something else,
        // so we would want to change these back to their original.

        /*if (changeAllowed(cbCorner, node) == true) {
            selected.makeShape(shape.getNumCorners());
            cbCorner.getSelectionModel().select(selected.getNumCorners() - 3);
        }

        if (changeAllowed(cbSpin, node) == true) {
            selected.setSpin(shape.getSpin());
            if (selected.getSpinRight() == true) {
                cbSpin.getSelectionModel().selectFirst();
            } else {
                cbSpin.getSelectionModel().select(1);
            }
        }

        if (changeAllowed(picker, node) == true) {
            selected.setFill((Color)shape.getFill());
            picker.setValue((Color)selected.getFill());
        }

        if (changeAllowed(sizeSlider, node) == true) {
            selected.setSizePercent(shape.getSizePercent());
            sizeSlider.setValue(selected.getSizePercent());
        }

        if (changeAllowed(spinSlider, node) == true) {
            selected.setSpinSpeedPercent(shape.getSpinSpeedPercent());
            spinSlider.setValue(selected.getSpinSpeedPercent());
        }

        // Now we say, if anything isnot normal, color it, if it is
        // normal, uncolor it, so it's easy to see what's different.
        updateChangedValueColors();*/
    }

    @Override
    public Node getNodeForModel(SelectionModel model) {
        return getNodeForModel(model, localControlsList);
    }
    
    private void setStyleForControl(Node control, boolean colored) {
        VBox box = getVBoxForLocalControl(control);
        AdjustValuesInterface.super.setStyleForControl(box, colored);
    }

    public void updateChangedValueColors() {
        if (selected == null) {
            return;
        }
        // We may not have created our controls yet so make sure we have them.
        if (cbCorner == null) {
            return;
        }
        // Look through and if anything is different, mark it for coloration.
        SysShape shape = Player.getSelectedShape();
        
        // ChoiceBoxes
        setStyleForControl(cbCorner, isDifferent(cbCorner, shape.getNumCorners() - 3));
        setStyleForControl(cbSpin, isDifferent(cbSpin, (shape.getSpinRight() == false)));
        
        // Sliders
        setStyleForControl(sizeSlider, isDifferent(sizeSlider, shape.getSizePercent()));
        setStyleForControl(spinSlider, isDifferent(spinSlider, shape.getSpinSpeedPercent()));

        // Other
        setStyleForControl(picker, isDifferent(picker, (Color)shape.getFill()));
    }

    public void submit(SysShape shape) {
        // Apply changes.
        if (shape == null) {
            Utils.err("Something went wrong, no shape to submit to");
            return;
        }

        shape.makeShape(selected.getNumCorners());
        shape.setSize(selected.getSize());
        shape.setSpinSpeed(selected.getSpinSpeed());
        shape.setSpin(selected.getSpin());
        shape.setFill((Color)selected.getFill());
    }
}