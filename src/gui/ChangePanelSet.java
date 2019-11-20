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

public class ChangePanelSet extends PanelSetBase {

    ChangePanelBottom panelBottom;
    ChangePanelLeft panelLeft;
    ChangePanelTop panelTop;

    public static OKWindow changePanelWindow;

    // Allow us to grab a copy of the top and left panels so that we can check
    // the valus for the Tutorial mode
    public ChangePanelLeft getPanelLeft() { return panelLeft; }
    public ChangePanelTop getPanelTop() { return panelTop; }

    public ChangePanelSet(MainScreen main) {
        super(main);

        panelBottom = new ChangePanelBottom(this);
        panelLeft = new ChangePanelLeft(this);
        panelTop = new ChangePanelTop(this);
        
        addPanels(panelLeft, panelTop, panelBottom);
        
        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        String strTitle = "Change the System";
        String strText = 
        "Before you move on to the next turn, you have the opportunity to " +
        "make changes to the system.  You can do that on this screen.  On the left " +
        "side, you'll notice some changes that you can make to the currently selected " +
        "shape.  Click a different shape to select it.  On the top are some general, " +
        "overall settings that you can adjust.  Please make whatever changes you feel " +
        "will help to achieve your objective for the current task, then click the 'Submit' button at the " + 
        "bottom of the screen.";
        changePanelWindow = new OKWindow(400, 500, strTitle, strText);

        // And set the helper text for the info area
        panelTop.getInfoArea().setHelpText(strText);
    }

    public void forceShowOKWindow() {
        changePanelWindow.showAndWait(true);
    }

    @Override
    public void show() {
        panelLeft.setup(AdjustShapeValuesPanel.Configs.Default);
        panelTop.setup(AdjustOverallValuesPanel.Configs.Default);
        super.show();
        changePanelWindow.showAndWait();
    }

    @Override
    public void onSelectedShapeUpdated() {
        if (panelLeft != null && panelLeft.isVisible() == true) {
            panelLeft.setSelectedPanelShape();
        }
    }

    @Override
    public void updateOneFrame(boolean running, Turn currentTurn) {
        if (panelLeft != null && panelLeft.isVisible() == true) {
            panelLeft.updateOneFrame(running, currentTurn);
        }
    }

    // Something changed from either the top or left panel, and since
    // we want to keep the two in sync, signal both to update their values.
    public void onValueChanged(Node node) { 
        panelTop.setValues(node);
        panelLeft.setValues(node);
    }

    public void changeShapeHelperText(String strNewText) {
        panelLeft.changeHelperText(strNewText);
    }

    public void onSubmitButton(String fromClassName) {
        String strSubmitDesc = "";

        // First record any tutorial data
        Task task = Player.getCurrentTask();
        if (task != null && task.getName().equals("Tutorial")) {
            Player.appendTutorialData(Tutorial.getTutorialCorrectForTurn(Player.getCurrentTurnNumber()));
        }

        // First, let's put the current values on the desc so we have the "before" and don't have to
        // hunt it down
        strSubmitDesc += "BEFORE_VALUES " + Data.currentValues.toString() + "\r\n";

        if (Player.getSelectedShape() != null) {
            strSubmitDesc += "BEFORE_SHAPE " + Player.getSelectedShape().toString() + "\r\n";
        }

        ChangeSet changeSet = new ChangeSet(Data.currentValues, Player.getSelectedShape());

        // Apply our changes
        panelLeft.submit();
        panelTop.submit();

        // Now these things have changed so update the changeset with the new values.
        changeSet.setNew(Data.currentValues, Player.getSelectedShape());
        //String strSubmitDesc = changeSet.toString();
        strSubmitDesc += "CHANGED_VALUES " + changeSet.toString();

        // And record the "after" values too
        // This is all just for convenience as it's really not much data 
        strSubmitDesc += "AFTER_VALUES " + Data.currentValues.toString() + "\r\n";
        
        if (Player.getSelectedShape() != null) {
            strSubmitDesc += "AFTER_SHAPE " + Player.getSelectedShape().toString() + "\r\n";
        }

        // Now take a snapshot of system as it is now and add to the string since we want to save that too
        SystemSnapshot snapshot = new SystemSnapshot();
        snapshot.snap(Gos.sim);
        strSubmitDesc += snapshot.toString();

        // Now record the action along with the entirety of the change set including the system snapshot
        Player.recordAction(Action.Type.SubmitChange, strSubmitDesc, fromClassName);

        // TODO remove this later
        Utils.log(Player.getPlayerData());

        // Show default areas again.
 	    parent.showDefaultAreas();

        // At this point we always want to clear out the frames from all previous turns from anything
        // the player has done because they're all lost now.  This should actually be clear already
        // when we get here but just in case.  We know once we hit the Submit button here, we
        // are moving forward with no way to go back.
        TurnSet ts = Player.getPlayedTurns();
        if (ts.getTurns() != null) {
            for (Turn turn : ts.getTurns()) {
                turn.clearFrames();
            }
        }

        // This should not be necessary...
        //System.gc();
        /*try {
            Thread.sleep(100);
        } catch (Exception e) {}*/

        // Do next turn.
        Gos.simRunner.advanceTask();
        
        Player.addDiscoveryPoints(1);
    }

    public void onCancelButton() {
        parent.onCancelButton();
    }
}