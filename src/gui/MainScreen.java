
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
import javafx.beans.binding.Bindings;
import javafx.application.Platform;

import gos.*;

// Gos - Game of Systems
public class MainScreen extends GosSceneBase {
    private StackPane centerArea;
    private SysPane sysPane;

    private ChangePanelSet changePanelSet;
    private ObservePanelSet observePanelSet;
    private InquirePanelSet inquirePanelSet;
    private MainPanelSet mainPanelSet;
    private ExperimentPanelSet experimentPanelSet;
    private ExpCreationPanelSet expCreationPanelSet;

    public ChangePanelSet getChangePanelSet() { return changePanelSet; }
    public ObservePanelSet getObservePanelSet() { return observePanelSet; }
    public InquirePanelSet getInquirePanelSet() { return inquirePanelSet; }
    public MainPanelSet getMainPanelSet() { return mainPanelSet; }
    public ExperimentPanelSet getExperimentPanelSet() { return experimentPanelSet; }
    public ExpCreationPanelSet getExpCreationPanelSet() { return expCreationPanelSet; }

    private ArrayList<PanelSetBase> panelSetList;
    public ArrayList<PanelSetBase> getPanelSetList() { return panelSetList; }

    private NextTaskWindow nextTaskWindow;
    
    private OKWindow pleaseWaitWindow;

    private MainScreen thisScreen;

    public MainScreen(Gos theGos, BorderPane root, int wid, int hgt) {
        super(theGos, root, wid, hgt);
        thisScreen = this;
    }

    @Override
    public void createBuildingBlocks() {
        getStylesheets().add("css/gos.main.css");

        /*scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                processKeyRelease(keyEvent.getCode());
            }
        });*/

        // Create all the panel sets
        changePanelSet = new ChangePanelSet(this);
        observePanelSet = new ObservePanelSet(this);
        inquirePanelSet = new InquirePanelSet(this);
        mainPanelSet = new MainPanelSet(this);
        experimentPanelSet = new ExperimentPanelSet(this);
        expCreationPanelSet = new ExpCreationPanelSet(this);

        // Add them all to the list
        panelSetList = new ArrayList<PanelSetBase>();
        panelSetList.add(changePanelSet);
        panelSetList.add(observePanelSet);
        panelSetList.add(inquirePanelSet);
        panelSetList.add(mainPanelSet);
        panelSetList.add(experimentPanelSet);
        panelSetList.add(expCreationPanelSet);

        // Set any partners
        experimentPanelSet.setPartner(expCreationPanelSet);
        expCreationPanelSet.setPartner(experimentPanelSet);
    }

    @Override
    public void createMainScene() {

        // button and pane are created
        sysPane = new SysPane();
        //sysPane.setStyle("-fx-border-color: lightgray;");
        sysPane.create();
        
        createCenterArea();
        overallRoot.setCenter(centerArea);
    }

    private void createCenterArea() {
        centerArea = new StackPane();        

        //taskTextVBox.setMaxWidth(Double.MAX_VALUE);
        //HBox.setHgrow(taskTextVBox, Priority.ALWAYS);

        // Transient because this is just a container for the top
        // and bottom parts of the system area
        VBox centerBox = new VBox();
        //centerBox.getChildren().addAll(taskTextVBox, sysPane);
        centerBox.getChildren().add(sysPane);
        centerArea.getChildren().addAll(centerBox);
        centerBox.setStyle("-fx-border-color: darkgray;");
    }

    @Override
    public void finalizeCreation() {
        // Set some pointers and such.
        //keepButtonsOnTop();
        
        // We can create this last.  We don't need it until later.
        nextTaskWindow = new NextTaskWindow(400, 600);

        String strTitle = "Please Wait for Upload";
        String strText = 
        "Please wait while your game play data is uploaded to our research server. " +
        "Periodic uploads during the game help to prevent the loss of data due to unforeseen " +
        "events." ;
        pleaseWaitWindow = new OKWindow(400, 400, strTitle, strText);
    }

    public void processKeyRelease(KeyCode key) {
        switch (key) {
            case ENTER:
                break;
        }
    }

    public void showPleaseWaitForUploadWindow() {
        pleaseWaitWindow.showAndWait(true);
    }

    // Right now the observe panelset is the first one
    // we want to show.  But we only call this the first time
    // we show a panel set, after that we need to call it via
    // the regular methods so that the snapshots are created properly.
    public void showFirstPanelSet() {
        showObservePanelSet();
    }

    public void showObservePanelSet() {
        Gos.sim.snapCurrent();
        Gos.simRunner.restoreObserveState();
        observePanelSet.show(); 
    }

    public void showInquirePanelSet() {
        Gos.sim.snapCurrent();
        inquirePanelSet.show(); 
    }

    public void showExperimentPanelSet() {
        Gos.sim.snapCurrent();
        experimentPanelSet.show();
    }

    public void showMainPanelSet() {
        // I don't think we want to snap current if we are going back
        // to main screen, actually we should usually be restoring
        // the current snap
        //Gos.sim.snapCurrent();
        mainPanelSet.show();
    }

    // Basically so I can check if we're on a screen that allows changing stuff.
    public boolean isShowingChangePanelSet() {
        return changePanelSet.isShown();
    }
    public boolean isShowingExpCreationPanelSet() {
        return expCreationPanelSet.isShown();
    }


    public void showChangePanelSet() {
        changePanelSet.show();
    }

    public void showNextTaskWindow() {
        nextTaskWindow.showAndWait(true);
    }

    public void showNextTaskWindow(boolean won) {
        nextTaskWindow.showAndWait(won);
    }

    public void showDefaultAreas() {
        mainPanelSet.show();
    }

    private void moveText(Text text, double x, double y)
    {
        text.setLayoutX(x);
        text.setLayoutY(y);
    }

    public void onCancelButton() {
        Gos.sim.restoreCurrentSnap();
        showDefaultAreas();
        // Best re-enable all buttons too.
        Gos.mainScene.enableDefaultButtons();
    }

    public void enableDefaultButtons() {
        mainPanelSet.enableDefaultButtons();
    }

    private void disableDefaultButtons() {
        mainPanelSet.disableDefaultButtons();
    }
    
    public void onSelectedShapeUpdated() {
        for (PanelSetBase panelset : panelSetList) {
            panelset.onSelectedShapeUpdated();
        }
    }

    // Just stop what we are doing and complete the task, maybe we reached
    // some kind of goal.
    public void forceCompleteTask(boolean won) {
        //sim.pause();
        sim.endTurn();
        enableDefaultButtons();
        update();
        Player.getPlayedTurns().setToLastTurn();

        // But we also have to update all the other stuff like advancing to the end of
        // the task and resetting the simulator

        // Wait, is this also being called from sim.endTurn()?  Which should trigger
        // the onEndTurn function in this class?
        Platform.runLater(() -> {
            Gos.mainScene.showNextTaskWindow(won);
        });
    }

    public void onNewTask() {
        reset();
        //updateTaskText();
        update();
        if (sim.shapes != null && sim.shapes.size() > 0) {
            Gos.selectShape(sim.shapes.get(0));
        }
    }

    public void onNewExercise() {
        onNewTask();
        //updateExerciseText();
    }

    @Override
    public void setSim(Simulator s, SimRunner runner) {
        super.setSim(s, runner);
        //updateTaskText();
        //updateExerciseText();
    }

    // Called from the simulator
    @Override
    public void onGuiUpdateRequired() {
        update();
    }

    // Called from the simulator
    @Override
    public void onSimReset() {
        // This is called every time the sim resets, which happens
        // every time it restores a snap, so ALL the time, every time
        // we switch screens, etc. It's not like an on-turn reset thing
        sysPane.reset();
        update();
    }

    // Called from the simulator
    @Override
    public void onEndTurn() {
        enableDefaultButtons();
        update();

        // If we have ended our change system turn here, we should automatically
        // end the task instead of making them click another button.
        //if (Player.getCurrentTurnNumber() >= Player.getMaxTurns()) {
        if (Player.getPlayedTurns().onLastTurn()) {
            // Have to wrap it in a runLater so that it isn't technically
            // inside the AnimationTimer loop.  If it is, the dialog
            // would actually block the main loop which is bad.
            Platform.runLater(() -> {
                Gos.mainScene.showNextTaskWindow();
            });
        }
    }

    @Override
    public void onStartTurn() {
        disableDefaultButtons();
        update();
    }

    @Override
    public void onShapeAdded(SysShape shape) {
        // Update our shapes.
        sysPane.addShape(shape);
    }

    @Override
    public void onShapeRemoved(SysShape shape) {
        // Update our shapes.
        sysPane.removeShape(shape);
    }

    @Override
    public void onRaindropAdded(Raindrop drop) {
        // Update our shapes.
        sysPane.addRaindrop(drop);
    }

    @Override
    public void onRaindropRemoved(Raindrop drop) {
        // Update our shapes.
        sysPane.removeRaindrop(drop);
    }

    @Override
    public void onSpikeAdded(Spike spike) {
        // Update our shapes.
        sysPane.addSpike(spike);
    }

    @Override
    public void onSpikeRemoved(Spike spike) {
        // Update our shapes.
        sysPane.removeSpike(spike);
    }

    @Override
    public void onEarthpatchAdded(Earthpatch patch) {
        // Update our shapes.
        sysPane.addEarthpatch(patch);
    }

    @Override
    public void onEarthpatchRemoved(Earthpatch patch) {
        // Update our shapes.
        sysPane.removeEarthpatch(patch);
    }

    @Override
    public void onGravityWellAdded(GravityWell item) {
        // Update our shapes.
        sysPane.addGravityWell(item);
    }

    @Override
    public void onGravityWellRemoved(GravityWell item) {
        // Update our shapes.
        sysPane.removeGravityWell(item);
    }

    @Override
    public void update() {
        for (PanelSetBase panelset : panelSetList) {
            panelset.update();
        }
    }

    @Override
    public void updateOneFrame(boolean running, Turn currentTurn) {
        // Make the player's selected shape rotate or do whatever
        // in terms of visual qualities

        // Gave all these their own event listeners and hooked them up to the
        // main simulaton event listener instead of going through here.
    }

    @Override
    public void reset() {
        for (PanelSetBase panelset : panelSetList) {
            panelset.reset();
        }
    }
}