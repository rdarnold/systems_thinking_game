package gos;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

public interface AdjustValuesInterface  
{  
    //void setValues();
    //void setValues(Node node);
    void onChanged(Node node);
    Node getNodeForModel(SelectionModel model);

    default void onChangeAnything(Node changed) {
        //Utils.log("Yeah? " + changed.getClass().getSimpleName());
        //setValues(changed);
        onChanged(changed);
    }

    default void processChange(Node node) {
        if (node.getUserData() == null || (boolean)node.getUserData() == false) {
            node.setUserData(true);
            // When this is called it'll recursively call changes to this
            // thing and we don't want that to happen so we pause it until
            // after this is done by setting a user data boolean.
            onChangeAnything(node);
            node.setUserData(null);
        }
    }

    // This is horribly stupid.  There does not appear to be a way to get
    // the parent choicebox from a selection model so we have to like do yet another
    // comparison here.  JavaFX falls short here.  Luckily we have a list of our
    // local controls that we can iterate through to figure this out.
    default Node getNodeForModel(SelectionModel model, ArrayList<Node> nodeList) {
        for (Node node : nodeList) {
            if (ChoiceBox.class.isInstance(node)) {
                ChoiceBox cb = (ChoiceBox)node;
                if (cb.getSelectionModel() == model)
                    return cb;
            }
         }

        // Should never get here, if so we have forgotten to add
        // a choicebox above.
        Utils.err("no ChoiceBox found in getNodeForModel");
        return null;
    }
    
    default boolean changeAllowed(Node node1, Node node2) {
        if (node1.getUserData() != null && (boolean)node1.getUserData() == true)
            return false;
        return (Utils.equals(node1, node2) == false);
    }

    default void setStyleForControl(VBox box, boolean colored) {
        if (colored == true) {
            box.getStyleClass().clear();
            box.getStyleClass().add("selected-change-vbox");
        }
        else {
            box.getStyleClass().clear();
            // We have this otherwise when we add a border later it messes up
            // the sizing, so we put a transparent border on it of the same width.
            // That way we are basically just "filling in the transparency" when
            // we assign a real border later.
            box.getStyleClass().add("no-change-vbox");
        }
    }

    default boolean isDifferent(ChoiceBox cb, int val) {
        return (cb.getSelectionModel().getSelectedIndex() != val);
    }

    default boolean isDifferent(CheckBox cb, boolean val) {
        return (cb.isSelected() != val);
    }

    default boolean isDifferent(Slider slider, double val) {
        return (slider.getValue() != val);
    }

    default boolean isDifferent(ChoiceBox cb, boolean val) {
        return (cb.getSelectionModel().getSelectedIndex() != (val ? 1 : 0));
    }

    default boolean isDifferent(ColorPicker picker, Color col) {
        Color color = (Color)picker.getValue();
        return (color.equals(col) == false);
    }

    default boolean isDifferent(ColorPicker picker, int red, int green, int blue) {
        Color color = (Color)picker.getValue();
        if (((int)(255.0 * color.getRed()) != red) ||
            ((int)(255.0 * color.getGreen()) != green) ||
            ((int)(255.0 * color.getBlue()) != blue)) 
        {
            return true;
        }
        return false;
    }
    
    default void clearStyles(ArrayList<VBox> controlsList) {
        for (VBox box : controlsList) {
            box.getStyleClass().clear();
            // We have this otherwise when we add a border later it messes up
            // the sizing, so we put a transparent border on it of the same width.
            // That way we are basically just "filling in the transparency" when
            // we assign a real border later.
            box.getStyleClass().add("no-change-vbox");
        }
    }

    
    // add to any node to trigger the change event
    // The choicebox one is a little different because the getBean
    // would return the selectionmodel which we dont want to cast to
    // a node.
    default void addCBChangeListener(ChoiceBox box) {
        ReadOnlyIntegerProperty property = box.getSelectionModel().selectedIndexProperty();
        property.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    // This one won't be a node so we have to get the node from the model,
                    // unfortunately there is no direct way to do this apparently.
                    Node node = getNodeForModel((SelectionModel)property.getBean());
                    processChange(node);
                }
        });
    }
    default void addChangeListener(ReadOnlyDoubleProperty property) {
        property.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    processChange((Node)property.getBean());
                }
        });
    }
    default void addChangeListener(ReadOnlyIntegerProperty property) {
        property.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    processChange((Node)property.getBean());
                }
        });
    }
    default void addChangeListener(ReadOnlyBooleanProperty property) {
        property.addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    processChange((Node)property.getBean());
                }
        });
    }
    default void addChangeListener(ReadOnlyObjectProperty<Color> property) {
        property.addListener(new ChangeListener<Color>() {
            public void changed(ObservableValue<? extends Color > ov,
                Color  old_val, Color  new_val) {
                    processChange((Node)property.getBean());
                }
        });
    }

    default void setupListeners(ArrayList<Node> localControlsList) {
        // Let's see if we can use our list to make this easy and maintainable.
        for (Node node : localControlsList) {
            if (ChoiceBox.class.isInstance(node)) {
                ChoiceBox cb = (ChoiceBox)node;
                addCBChangeListener(cb);
            }
            else if (Slider.class.isInstance(node)) {
                Slider slider = (Slider)node;
                addChangeListener(slider.valueProperty());
            }
            else if (CheckBox.class.isInstance(node)) {
                CheckBox cb = (CheckBox)node;
                addChangeListener(cb.selectedProperty());
            }
            else if (ColorPicker.class.isInstance(node)) {
                ColorPicker picker = (ColorPicker)node;
                addChangeListener(picker.valueProperty());
            }
         }

        // Choice boxes
        /*addCBChangeListener(cbDur);
        addCBChangeListener(cbRain);
        addCBChangeListener(cbGravity);
        addCBChangeListener(cbArmor);
        addCBChangeListener(cbParadigm);

        // Sliders
        addChangeListener(globalSlider.valueProperty());
        addChangeListener(rainRateSlider.valueProperty());
        addChangeListener(rainSpeedSlider.valueProperty());
        addChangeListener(dotSize.valueProperty());
        addChangeListener(dotSpeed.valueProperty());

        // Check boxes
        addChangeListener(chkSteal.selectedProperty());
        addChangeListener(chkGive.selectedProperty());

        // Other stuff
        addChangeListener(dotColor.valueProperty());*/
    }
}  