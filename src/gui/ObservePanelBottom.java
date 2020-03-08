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

public class ObservePanelBottom extends PanelBottomBase {

    ObservePanelSet m_ParentPanelSet;

    Label instructionText;
    Label observationTurnText;
    Slider frameSlider;

    MovableButton btnViewAll;
    MovableButton btnPause;
    
    private ObservePanelBottom thisScreen;

    // We need this pointer here to properly bind to the values we need.
    // This value is static on the Player object and the pointer does not change
    // so it is OK to do this.
    private static TurnSet m_Observation;

    public ObservePanelBottom(ObservePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        m_Observation = Player.getObservation();
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
        col.setPercentWidth(10);
        getColumnConstraints().add(col);

        col = new ColumnConstraints();
        col.setPercentWidth(10);
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
        
        RowConstraints row;
        row = new RowConstraints();
        row.setPercentHeight(50);
        getRowConstraints().add(row);

        row = new RowConstraints();
        row.setPercentHeight(50);
        getRowConstraints().add(row);
    }
    
    @Override
    public void onEndTurn() {
        btnPause.setText(">");
    }

    @Override
    public void onStartTurn() {
        btnPause.setText("||");
    }

    @Override
    public void onPause() { 
        btnPause.setText(">");
    }

    public boolean trySkipToNextTurn() {
        // Grab the next turn
        TurnSet obs = Player.getObservation();
        Turn nextTurn = obs.getTurn(obs.getTurnNumber()+1);
        if (nextTurn != null) {
            // OK we have a next turn, so we can skip to it.
            obs.addTurnNumber();
            nextTurn.setCurrentFrameNumber(0);
            Gos.sim.setTurn(nextTurn);
            updateSlider(nextTurn);
            // success
            return true;
        }
        // fail, couldn't skip the turn
        return false;
    }

    public void onForwardPressed() {
        /* So this >> button has to do several things to be intuitive.
        1 - Skip forward in turns if we already have watched several.  
        2 - If we are in the middle or beginning of a turn, skip to the end
            (generating the rest o the turn if we don't have it) and go to
            beginning of next turn.
        3 - If we have no turns left, just advance to the last frame of
            current turn.
        */

        // First check if we can just advance to the next turn.
        // We can if we have a full turn of frames that go all the way
        // to the end.  If we do, we can just skip ahead to the beginning
        // of the next turn.
        TurnSet obs = Player.getObservation();

        Turn turn = obs.getCurrentTurn();
        if (turn != null && turn.isFull()) {
            // We have a full turn, just go ahead and skip to the start of
            // the next turn.

            // Or, if we can't because we are at max, just go to the end of
            // the current turn.
            if (obs.onLastTurn() == true) {
                turn.setToLastFrame();
                Gos.sim.setTurn(turn);
                updateSlider(turn);
                return;
            }

            // Grab the next turn
            if (trySkipToNextTurn() == true) {
                return;
            }
        }

        // If we don't, we FFW from wherever we are.
        Gos.simRunner.observeFFWOneTurn();

        // And if we were on the last turn and are full, we are done.  That
        // means we just fast forwarded to the very last frame of the allowed
        // turns, so we need to stop without moving onto the next turn.
        turn = obs.getCurrentTurn();
        if (obs.onLastTurn() && turn != null && turn.isFull()) {
            return;
        }

        // Now, literally just start up the next turn, and enter it, but
        // only go into the first frame so we are sitting at the beginning
        // paused basically.
        //Gos.simRunner.observeTurns(1, false);
        Gos.simRunner.observeFirstFrame();
        updateSlider(obs.getCurrentTurn());
    }

    public void onBackPressed() {
        if (Gos.sim.isRunning() == true) {
            Gos.sim.pause();
            //return;
        }
        // I don't really like doing it in the runner, I'm going to do it
        // here because it's easier to see.  Maybe it could be moved into 
        // the runner or an observation controller class in the future.
        //Gos.simRunner.retreatObservation();

        // We just want to skip back a turn now.
        TurnSet obs = Player.getObservation();

        // First, if we are on the first turn, just skip back to the beginning.
        if (obs.onFirstTurn()) {
            obs.setTurnNumber(0);  // Should already be the case.
        }
        else {
            // Ok so we are not on the first turn.  In that case, just skip
            // to the beginning of the prior turn.
            obs.subTurnNumber();
        }

        Turn turn = obs.getCurrentTurn();
        if (turn != null) {
            turn.setCurrentFrameNumber(0);
        }
        updateSlider(turn);
        Gos.sim.setTurn(turn);
    }

    public void onPausePressed() {
        // Running means we hit play and didn't hit the end of the turn yet.
        if (Gos.sim.isRunning() == true) {
            if (Gos.sim.isPaused()) {
                btnPause.setText("||");
                Turn turn = Player.getObservation().getCurrentTurn();
                Gos.sim.play(turn);
                //Gos.sim.unpause();
                // Now play from where we unpaused, it's more than just
                // an "unpause" it's actually "unpause and play"
                //Gos.sim.playFromCurrent();
            } 
            else {
                //btnPause.setText(">");
                Gos.sim.pause();
            }
        }
        else {
            // If not running, it just means we finished watching an entire turn.
            // But we could still be somewhere within the turn if we dragged our
            // slider back, so we need to check where we are.
            TurnSet obs = Player.getObservation();
            Turn turn = obs.getCurrentTurn();
            if (turn != null) {
                // Are we at the end of the turn?
                if (turn.onMaxFrame() == true) {
                    if (obs.onLastTurn() == true) {
                        // Sorry, you're out of luck.
                        return;
                    }
                    // We are the end, try to move to next turn.
                    Gos.simRunner.observeTurns(1, false);
                }
                // We are not at the end, just play from where we are.
                else {
                    Gos.simRunner.observeFromCurrent();
                }
            }
            // We have no current turn, so we haven't even started.  Then start.
            else {
                Gos.simRunner.observeTurns(1, true);
            }
            btnPause.setText("||");
        }
    }

    public void onViewAllPressed() {
        // Try to do a gc before we start this, helps with the smoothness
        // during the run as there are lots of allocations.
        // Honestly it doesn't seem to help much / at all
        //System.gc();

        Gos.simRunner.observeTurns(Player.getObservation().getMaxTurns(), true);
    }

    public void updateOneFrame(boolean running, Turn currentTurn) {
        // It's a little weird to use the simRunner to check to see
        // if we are observing but there's actually no other way.  The simulator
        // itself doesnt have a concept of observing or not, it just runs
        // a turn of the simulation.  The runner is what controls it, so the
        // runner is what knows whether or not an observation is actually ongoing.
        // Technically the runner is actually the observer of the sim.  So that
        // is the only place to check unless I redesign.
        if (Gos.simRunner.isObserving() == true) {
            
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
        if (running == false || Gos.simRunner.isObserving() == false)
            return;
        updateSlider(turn);
    }

    public void restoreLastObservationFrame() {
        //int frameNum = Player.getObservation().getFrameNumber();
        // We need to set the right turn on the sim too probably, not just frame.
        //Gos.sim.goToFrame(frameNum);
        Turn turn = Player.getObservation().getCurrentTurn();
        Gos.sim.setTurn(turn);
        if (turn != null) {
            updateSlider(turn);
        }
        else {
            frameSlider.setValue(0); 
        }
    }

    private void restoreFrameFromSlider(int sliderPos, boolean force) {
        /*if (frameSlider.getUserData() == null) {
            return;
        }*/
        // Actually we should keep track of the index on the frameSlider maybe,
        // and then use the observation turns.
        //Turn turn = (Turn)frameSlider.getUserData();
        Turn turn = Player.getObservation().getCurrentTurn();
        if (turn == null || turn.getFrames() == null || turn.getFrames().size() <= 1)
            return;

        if (force == false && Gos.sim.isPaused() == false) {
            return;
        }
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
            btnPause.setText(">");
            restoreFrameFromSlider(new_val.intValue(), false);
        }
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
        frameSlider.getStyleClass().add("gos-frame-slider");
        GridPane.setRowIndex(frameSlider, 0);

        // Only happens when user is dragging the slider, I think
        /*frameSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasChanging, Boolean isNowChanging) {
                if (isNowChanging) {
                    Gos.sim.pause();
                    //sliderDragging = true;
                    //sliderClicked = false;
                    btnPause.setText(">");
                } else {
                    //Gos.sim.unpause();
                    //sliderDragging = false;
                    //sliderClicked = false;
                }
            }
        });*/

        // Happens no matter what changed the value
        frameSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    onSliderMoved(old_val, new_val);
            }
        });

        frameSlider.setOnMousePressed((MouseEvent event) -> {
            Gos.sim.pause();
            btnPause.setText(">");
        });
        
        // Click seems to fire same time as release
        /*frameSlider.setOnMouseClicked((MouseEvent event) -> {
            sliderClicked = false;
        });*/


        setVgap(10);
        // GridPane has an add, which takes parameters:
        // col, row, colspan, rowspan
        add(frameSlider, 1, 0, 6, 1);

        MovableButton btn;
        String str;
        Tooltip toolTip;
        
        btn = new MovableButton("Main Screen");
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                // In case we had paused it by dragging the slider, make sure we
                // unpause it.
                Gos.sim.unpause();

                // And get us out of observation state
                Gos.simRunner.stopObserving();

                // And call cancel to get us back to the main scene
                Gos.mainScene.onCancelButton();
            }
        });
        add(btn, 0, 1);
        GridPane.setHalignment(btn, HPos.CENTER);

        btn = new MovableButton("<<");
        btn.setPrefWidth(btn.getPrefWidth() * 0.60);
        str = "Move the observation backward by one turn.";
        Utils.addToolTip(btn, str);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onBackPressed();
            }
        });
        add(btn, 2, 1);
        GridPane.setHalignment(btn, HPos.RIGHT);

        btnViewAll = new MovableButton("Play All");
        btn = btnViewAll;
        btn.setPrefWidth(btn.getPrefWidth() * 0.60);
        str = "View the entire series of turns from beginning to end.  This costs nothing and " +
        "you may do it at any time.";
        Utils.addToolTip(btn, str);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onViewAllPressed();
            }
        });
        add(btn, 3, 1);
        GridPane.setHalignment(btn, HPos.CENTER);

        
        btnPause = new MovableButton(">");
        btn = btnPause;
        btn.setPrefWidth(btn.getPrefWidth() * 0.60);
        str = "Play or pause the observation.";
        Utils.addToolTip(btn, str);
        //btn.setVisible(false);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onPausePressed();
            }
        });
        add(btn, 4, 1);
        GridPane.setHalignment(btn, HPos.CENTER);

        btn = new MovableButton(">>");
        btn.setPrefWidth(btn.getPrefWidth() * 0.60);
        str = "Move the observation forward by one turn.";
        Utils.addToolTip(btn, str);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                onForwardPressed();
            }
        });
        add(btn, 5, 1);
        GridPane.setHalignment(btn, HPos.LEFT);
        
        observationTurnText = new Label();
        observationTurnText.setMinWidth(Constants.BUTTON_WIDTH / 2);
        observationTurnText.setAlignment(Pos.CENTER);
        observationTurnText.setStyle("-fx-font-weight: bold; ");

        observationTurnText.textProperty().bind(Bindings.createStringBinding(() -> 
            "Turn " + (m_Observation.getTurnNumber() + 1) + " / " + 
            m_Observation.getMaxTurns(),
            m_Observation.turnNumberProperty(), 
            m_Observation.maxTurnsProperty()));
            
        /*observationTurnText.textProperty().bind(Bindings.createStringBinding(() -> 
            "Turn " + Player.getCurrentObservationTurnNumber() + " / " + Player.getMaxObservationTurns(),
            Player.currentObservationTurnNumberProperty(), Player.maxObservationTurnsProperty()));
            */
        add(observationTurnText, 7, 1);
        GridPane.setHalignment(observationTurnText, HPos.RIGHT);
    }
}