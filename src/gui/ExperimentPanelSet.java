
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

public class ExperimentPanelSet extends PanelSetBase {

    // The partner panel set, the experiment creation
    // which then affects this screen.
    ExpCreationPanelSet expCreationPartner;
    
    // For observing
    ExperimentPanelBottom panelBottom;
    ExperimentPanelLeft panelLeft;
    ExperimentPanelTop panelTop;

    public static OKWindow experimentPanelWindow;

    public ExperimentPanelSet(MainScreen main) {
        super(main);

        panelBottom = new ExperimentPanelBottom(this);
        panelLeft = new ExperimentPanelLeft(this);
        panelTop = new ExperimentPanelTop(this);
        
        addPanels(panelLeft, panelTop, panelBottom);
        
        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        String strTitle = "Experiment with the System";
        String strText = 
        "On this screen, you can create different experiments to play around " +
        "with the system without actually advancing the game forward.  It costs " +
        "1 Discovery Point to create each experiment, so it is important to create " +
        "your experiments wisely.  When you create a new experiment, it starts from " +
        "the current state of the system on this screen.  This allows you to chain " +
        "experiments together to create a series of turns that you can watch all at once " +
        "using the 'Run All Experiments' button.\r\n\r\n" +
        "Note that when you re-run experiments, you may not always see exactly the same " +
        "result.  The game includes elements of randomness.  However, the results " +
        "will be similar enough that you can still learn a great deal about the system using " +
        "this method.";
        experimentPanelWindow = new OKWindow(400, 500, strTitle, strText);
        
        // And set the helper text for the info area
        panelTop.getInfoArea().setHelpText(strText);
    }

    public void forceShowOKWindow() {
        experimentPanelWindow.showAndWait(true);
    }

    @Override
    public void show() {
        super.show();
        experimentPanelWindow.showAndWait();
    }

    public void setPartner(ExpCreationPanelSet partner) {
        expCreationPartner = partner;
    }

    public void onCreateButton() {
        expCreationPartner.show();
    }

    public void onCreateExperiment(Experiment exp) {
        // Now tie that experiment to one of the buttons.
        if (panelLeft.addExperiment(exp) == true) {
            exp.play(Gos.sim);
        }

        // Now show the right panel.
        show();
    }
    
    public void onRunAllExperiments() {
        // So pass a series of snapshots to the simRunner and have the runner
        // run them back to back.  Just a generic way to run experiments in order.
        Gos.simRunner.runSequence(panelLeft.generateSnapshotList());
    }
}