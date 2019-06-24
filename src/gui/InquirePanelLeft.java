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

public class InquirePanelLeft extends SelectableShapePanel {

    // Add more configs of different sets of controls here if needs be.
    enum Configs {
        None,
        Default;
    }

    // If the config is the same we don't need to remove and re-add everything.
    private Configs config = Configs.None; 

    RevealButton rvShapeSpinSpeed;
    RevealButton rvShapeSize;
    //RevealButton rvShapeGravityPull;
    RevealButton rvShapeArmor;
    RevealButton rvShapeSizeStolen;
    RevealButton rvShapeSizeGiven;
    RevealButton rvShapeSizeStolenFrom;
    RevealButton rvShapeSizeGivenTo;

    Label discoveryPointText;

    InquirePanelSet m_ParentPanelSet;
    
    private InquirePanelLeft thisScreen;

    public InquirePanelLeft(InquirePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
    }

    public void setup(Configs configType) {
        if (config == configType) {
            setSelectedPanelShape();
            return;
        }

        removeControls();

        super.setup();
        
        rvShapeSpinSpeed = new RevealButton("Spin Speed");
        rvShapeSpinSpeed.addToolTip("The speed at which the shape is spinning.");
        addRevealButton(rvShapeSpinSpeed);
        /*spinSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    selected.setSpinSpeedPercent(new_val.doubleValue());
                }
        });*/
        rvShapeSize = new RevealButton("Size");
        rvShapeSize.addToolTip("The size of the shape.");
        addRevealButton(rvShapeSize);

        /*rvShapeGravityPull = new RevealButton("Gravity Pull");
        rvShapeGravityPull.addToolTip("The percentage that this shape contributes to the total gravity.");
        addRevealButton(rvShapeGravityPull);*/

        rvShapeArmor = new RevealButton("Armor");
        rvShapeArmor.addToolTip("The specific armor value of the shape.");
        //addRevealButton(rvShapeArmor);

        rvShapeSizeStolen = new RevealButton("Size Stolen");
        rvShapeSizeStolen.addToolTip("The amount of size this shape has stolen from other shapes.");
        addRevealButton(rvShapeSizeStolen);

        rvShapeSizeGiven = new RevealButton("Size Given");
        rvShapeSizeGiven.addToolTip("The amount of size this shape has given to other shapes.");
        addRevealButton(rvShapeSizeGiven);

        rvShapeSizeStolenFrom = new RevealButton("Size Stolen From");
        rvShapeSizeStolenFrom.addToolTip("The amount of size that has been stolen from this shape.");
        //addRevealButton(rvShapeSizeStolenFrom);

        rvShapeSizeGivenTo = new RevealButton("Size Received");
        rvShapeSizeGivenTo.addToolTip("The amount of size this shape has been given by other shapes.");
        //addRevealButton(rvShapeSizeGivenTo);

        
        // Total amount of mass your shape has stolen
        // Total amount of mass your shape has given
        // Total amount of mass that has been stolen from your shape
        
        /*discoveryPointText = new Label();
        getChildren().add(discoveryPointText);
        discoveryPointText.textProperty().bind(Player.discoveryPointsProperty().asString(
            "Discovery Points: %d"
        ));*/
        
        // This should be at the end so we can check to see if we have configured yet.
        config = configType;
        update();
    }

    /*@Override
    public void updateOneFrame(boolean running, Turn currentTurn) { 
        super.updateOneFrame(running, currentTurn);
    }*/
    @Override
    public void update() {
        super.update();

        // Update the RevealButton values as they may have changed since we last came here.
        setShapeValues();
    }

    @Override
    public void setShapeValues() {
        if (selected == null || config == Configs.None) {
            return;
        }
        
        rvShapeSpinSpeed.setValue(selected.getSpinSpeed());
        rvShapeSize.setValue(selected.getSize());
        rvShapeArmor.setValue(selected.getArmor());

        //double totalGravity = Gos.sim.calculateTotalGravity();
        //double grav = Gos.sim.getGravity(selected);
        //rvShapeGravityPull.setText(String.format("%.1f%%", ((grav / totalGravity) * 100)));

        rvShapeSizeStolen.setValue(selected.getSizeStolen());
        rvShapeSizeGiven.setValue(selected.getSizeGiven());
        rvShapeSizeStolenFrom.setValue(selected.getSizeStolenFrom());
        rvShapeSizeGivenTo.setValue(selected.getSizeGivenTo());
    }

    @Override
    public void reset() {
        for (VBox box : controlsList) {
            if (RevealButton.class.isInstance(box)) {
                RevealButton rv = (RevealButton)box;
                rv.reset();
            }
        }
    }
}