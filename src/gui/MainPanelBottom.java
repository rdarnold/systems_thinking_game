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
import javafx.beans.binding.Bindings;

import gos.*;

public class MainPanelBottom extends PanelBottomBase {

    MainPanelSet m_ParentPanelSet;

    ArrayList<MovableButton> buttonList = null;
    Label turnText;
    Slider frameSlider;

    private MainPanelBottom thisScreen;

    public MainPanelBottom(MainPanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        buttonList = new ArrayList<MovableButton>();
        create();
    }

    private void setupGridPane() {
        ColumnConstraints col;
        
        col = new ColumnConstraints();
        col.setPercentWidth(20);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(10);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(10);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(20);
        getColumnConstraints().add(col);

        /*col = new ColumnConstraints();
        col.setPercentWidth(10);
        getColumnConstraints().add(col);*/

        col = new ColumnConstraints();
        col.setPercentWidth(10);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(10);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(20);
        getColumnConstraints().add(col);
        
        RowConstraints row;
        row = new RowConstraints();
        row.setPercentHeight(50);
        getRowConstraints().add(row);

        row = new RowConstraints();
        row.setPercentHeight(50);
        getRowConstraints().add(row);

        /*ColumnConstraints col;
        
        col = new ColumnConstraints();
        col.setPercentWidth(25);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(50);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(25);
        getColumnConstraints().add(col);*/
    }

    private void create() {
        setupGridPane();
        
        frameSlider = new Slider();
        frameSlider.setMin(0);
        frameSlider.setMax(100);  // Let's update this to the number of frames, make it easy.
        frameSlider.setValue(0);
        //slider.setShowTickLabels(true);
        //slider.setShowTickMarks(true);
        frameSlider.setMajorTickUnit(50);
        frameSlider.setMinorTickCount(1);
        frameSlider.setBlockIncrement(10);
        GridPane.setRowIndex(frameSlider, 0);
        
        // Happens no matter what changed the value
        frameSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    onSliderMoved(old_val, new_val);
            }
        });

        frameSlider.setOnMousePressed((MouseEvent event) -> {
            //Gos.sim.pause();
            //btnPause.setText(">");
        });

        setVgap(10);
        // GridPane has an add, which takes parameters:
        // col, row, colspan, rowspan
        add(frameSlider, 1, 0, 5, 1);
        

        MovableButton btn;
        String str;

        btn = new MovableButton("Change System");
        str = "Take action by adjusting values and moving on to the next turn." +
        "  You must do this in order to move forward in the exercise.";
        Utils.addToolTip(btn, str);
        //btn.moveTo(0, 200);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());

                // Now see if we had been dragging the slider around, if so we need to reset
                // to the latest frame to show the actual state of the system.
                Turn turn = Player.getPlayedTurns().getCurrentTurn();
                if (turn != null) {
                    turn.setToLastFrame();
                    Gos.sim.setTurn(turn);
                    updateSlider(turn);  // This pauses it so unpause it after
                }
                // If we were paused for any reason, let's unpause now so players can
                // see the shapes spinning.
                Gos.sim.unpause();
                Gos.sim.snapCurrent();
                //if (Player.getCurrentTurnNumber() >= Player.getMaxTurns()) {
                if (Player.getPlayedTurns().onLastTurn()) {
                    Gos.mainScene.showNextTaskWindow();
                }
                else {
                    Gos.mainScene.showChangePanelSet(); 
                    //Player.setSelectedShape(sim.shapes.get(0));
                    //onSelectedShapeUpdated();
                }
            }
        });
        buttonList.add(btn);
        add(btn, 3, 1);
        GridPane.setHalignment(btn, HPos.CENTER);

        turnText = new Label();
        turnText.setMinWidth(Constants.BUTTON_WIDTH / 2);
        turnText.setAlignment(Pos.CENTER);
        turnText.setStyle("-fx-font-weight: bold; ");
        /*getChildren().add(turnText);
        GridPane.setRowIndex(turnText, 0);
        GridPane.setColumnIndex(turnText, 2);
        GridPane.setHalignment(turnText, HPos.RIGHT);*/

        turnText.textProperty().bind(Bindings.createStringBinding(() -> 
            "Turn " + Player.getCurrentTurnNumberDisplay() + " / " + Player.getMaxTurns(),
            Player.currentTurnNumberProperty(), 
            Player.getPlayedTurns().maxTurnsProperty()));
        
        add(turnText, 6, 1);
        GridPane.setHalignment(turnText, HPos.RIGHT);
        /*turnText.textProperty().bind(Bindings.createStringBinding(() -> 
            "Turn " + (Player.getPlayedTurns().getTurnNumber() + 1) + " / " + 
            Player.getPlayedTurns().getMaxTurns(),
            Player.getPlayedTurns().turnNumberProperty(), 
            Player.getPlayedTurns().maxTurnsProperty()));*/
    }

    public void enableDefaultButtons() {
        if (buttonList == null) {
            return;
        }
        frameSlider.setDisable(false);
        for (MovableButton btn : buttonList) {
            btn.setDisable(false);
        }
    }

    public void disableDefaultButtons() {
        if (buttonList == null) {
            return;
        }
        frameSlider.setDisable(true);
        for (MovableButton btn : buttonList) {
            btn.setDisable(true);
        }
    }
    
    public void updateOneFrame(boolean running, Turn currentTurn) {
        // It's a little weird to use the simRunner to check to see
        // if we are observing but there's actually no other way.  The simulator
        // itself doesnt have a concept of observing or not, it just runs
        // a turn of the simulation.  The runner is what controls it, so the
        // runner is what knows whether or not an observation is actually ongoing.
        // Technically the runner is actually the observer of the sim.  So that
        // is the only place to check unless I redesign.
        if (Gos.simRunner.isObserving() == false) {
            
            // Use our user data on the slider to tell whether this is user input or not.
            frameSlider.setUserData(true);
            updateSlider(running, currentTurn);
            frameSlider.setUserData(null);
            //Player.getObservation().setFrameNumber(currentTurn.getLastFrameNumber());
        }
    }

    public void updateSlider(Turn turn) {
        if (frameSlider == null)
            return;
        if (turn == null) {
            frameSlider.setValue(0); 
            return;
        }
        frameSlider.setMax(turn.getMaxFrames()); 
        frameSlider.setValue(turn.getCurrentFrameNumber()); 
    }

    // This is called continuously so we don't want to update if the
    // sim isn't running
    public void updateSlider(boolean running, Turn turn) {
        // We don't update this when we're observing since this is not the observe mode.
        // We only update if observing in the observe mode.
        if (running == false || Gos.simRunner.isObserving() == true)
            return;
        updateSlider(turn);
    }

    private void restoreFrameFromSlider(int sliderPos, boolean force) {
        /*if (frameSlider.getUserData() == null) {
            return;
        }*/
        // Actually we should keep track of the index on the frameSlider maybe,
        // and then use the observation turns.
        //Turn turn = (Turn)frameSlider.getUserData();
        Turn turn = Player.getPlayedTurns().getCurrentTurn();
        if (turn == null || turn.getFrames() == null || turn.getFrames().size() <= 1)
            return;

        /*if (force == false && Gos.sim.isPaused() == false) {
            return;
        }*/
        int frameNum = sliderPos;
        if (frameNum > turn.getFrames().size() - 1) {
            frameNum = turn.getFrames().size() - 1;
        }
        // Maybe I should have like a viewingFrame in the Turn object,
        // and it sets that, and then just does like viewFrame, which 
        // sets the viewing frame to that frame and restores, and then
        // when you hit playFrom(frameNum) or playFromViewing and then
        // it'll trim the rest of the thing.
        //Gos.sim.restoreState(turn.getFrame(frame)); //, frame);
        // Make sure we are on the right turn though, we may have left the screen,
        // done a "change system" and come back here later.  Maybe I should just
        // reset the whole observation stuff when system is changed.
        // Actually that's what I ended up doing
        turn.setCurrentFrameNumber(frameNum);
        // Do I need a goto like this, shouldn't I just be able to play the turn
        // and it would automatically see that the frame is at X position and just
        // play from there?
        //Gos.sim.goToFrame(frameNum); //, frame);
        Gos.sim.setTurn(turn);
    }

    //boolean sliderDragging = false;
    //boolean sliderClicked = false;
    private void onSliderMoved(Number old_val, Number new_val) {
        //if (sliderDragging == true || sliderClicked == true) {
        // We should only be doing this when the PLAYER moved the slider,
        // not when it gets updated by the system.  Hence the use of userdata
        if (frameSlider.getUserData() == null) {
            Gos.sim.pause();
            //btnPause.setText(">");
            restoreFrameFromSlider(new_val.intValue(), false);
        }
    }
}