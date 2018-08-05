
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
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
import javafx.scene.web.HTMLEditor;
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
import javafx.scene.text.TextAlignment;

import gos.*;

public class ExpCreationPanelSet extends PanelSetBase {
    
    // Partner panel set, the experiment panel
    ExperimentPanelSet experimentPanelPartner;

    // For observing
    ExpCreationPanelBottom panelBottom;
    ExpCreationPanelLeft panelLeft;
    ExpCreationPanelTop panelTop;

    public ExpCreationPanelSet(MainScreen main) {
        super(main);

        panelBottom = new ExpCreationPanelBottom(this);
        panelLeft = new ExpCreationPanelLeft(this);
        panelTop = new ExpCreationPanelTop(this);
        
        addPanels(panelLeft, panelTop, panelBottom);

        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        String strText = 
        "Experiment with the system by changing the overall system values " +
        "at the top of the screen and / or changing the values of a specific " +
        "shape on the left.  When you have the values where you want them, " +
        "click the 'Create' button to create and view the system resulting " +
        "from your changes.";
        
        // And set the helper text for the info area
        panelTop.getInfoArea().setHelpText(strText);
    }

    public void setPartner(ExperimentPanelSet partner) {
        experimentPanelPartner = partner;
    }

    @Override
    public void show() {
        panelLeft.setup(AdjustShapeValuesPanel.Configs.Default);
        panelTop.setup(AdjustOverallValuesPanel.Configs.Default);
        super.show();
    }
    
    @Override
    public void onSelectedShapeUpdated() {
        if (panelLeft != null && panelLeft.isVisible() == true) {
            panelLeft.setSelectedPanelShape();
        }
    }

    // Something changed from either the top or left panel, and since
    // we want to keep the two in sync, signal both to update their values.
    public void onValueChanged(Node node) { 
        panelTop.setValues(node);
        panelLeft.setValues(node);
    }

    public void onCancelButton() {
        experimentPanelPartner.show();
    }

    public void onCreateButton(String fromClassName) {
        if (Gos.checkDiscoveryPoints() == false)
            return;

        /*if (Player.getDiscoveryPoints() <= 0) {
            // Disallow, cancel instead.  Pop up telling
            // them they dont have enough points.
            Gos.showInsufficientDiscoveryPoints();
            return;
        }*/
        // Show a pop up asking them to confirm?  Or dont bother?
        Player.subDiscoveryPoints(1);

        // We need to figure out which shape is selected, so we know what
        // to submit to when we create the experiment.
        int index = Gos.sim.getShapeIndex(Player.getSelectedShape());

        Experiment exp = new Experiment();
        exp.setStartingState(Gos.sim);

        // Now we select the equivalent copied shape from the experiment
        // that we had selected in the sim, and submit our changes to that one.
        SysShape shape = exp.getSnap().shapes.get(index);

        ChangeSet changeSet = new ChangeSet(exp.getSnap().getValues(), shape);

        // Pull current values and create an experiment from them.
        panelTop.submit(exp);
        panelLeft.submit(shape);

        changeSet.setNew(exp.getSnap().getValues(), shape);
        Player.recordAction(Action.Type.SubmitExpChange, changeSet.toString(), fromClassName);
        
        // Now show the right panel.  Man this is weird calling out
        // like this.
        experimentPanelPartner.onCreateExperiment(exp);
    }
    
    @Override
    public void updateOneFrame(boolean running, Turn currentTurn) {
        if (panelLeft != null && panelLeft.isVisible() == true) {
            panelLeft.updateOneFrame(running, currentTurn);
        }
    }
}