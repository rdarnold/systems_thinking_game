package gos;

import java.util.ArrayList;

// Try to keep this decoupled from the UI
public class SimRunner implements SimulatorEventListener {
    private Simulator sim;
    private ScenarioMaker maker;

    // I guess there are really three states I'm trying to capture here.
    // You can either be playing, you can just be running the simulator,
    // or you can be observing.  I dont really capture that well with the
    // current design of this class.
    private boolean m_bObserving = false;
    private boolean m_bActualRun = false;
    public boolean isObserving() { return m_bObserving; }

    private int m_nNumTurnsToRun = 1;
    private int m_nTurnsRunSoFar = 0;

    // You can pass it an array of snapshots for it to run in order.
    // If it has this list, it will run the shots.
    ArrayList<SystemSnapshot> sequence = new ArrayList<SystemSnapshot>();

    // We also keep sets of snapshots for observed and actual turns.
    // This is just one snapshot for the starting state of the turn.
    // These are NOT all the frames within a turn.  They are just ONE snapshot
    // for the start of each turn.
    //ArrayList<SystemSnapshot> observeSnapshots = new ArrayList<SystemSnapshot>();
    //ArrayList<SystemSnapshot> actualSnapshots = new ArrayList<SystemSnapshot>();

    // Instead of keeping the snapshots, why don't we keep the turns and then just
    // access the first frame of each turn.  We sort of do this anyway.
    // I don't really like the way this observing thing works at all.  Maybe my
    // strategy should be to record the observation turn by turn, and then
    // access everything from that list, then just blow it away on a new
    // task.  Should I just have an Observation class that keeps track of all
    // this, kind of like I have an Experiment class?  And it has all the turns,
    // the current frame in the observation, the current turn in the observation,
    // all that stuff.
    //ArrayList<Turn> observeTurnList  = new ArrayList<Turn>();
 
    // This should also load up all the external parameters
    // and such.  It's the thing from which the sim can be
    // executed
    public SimRunner(Simulator theSim) {
        sim = theSim;
        maker = new ScenarioMaker(sim);
    }

    public Turn runOneTurn(boolean record) {
        Turn turn = sim.startTurn(record);

        // If we are observing, we tack it onto the player's observation obj
        /*if (m_bObserving == true) {
            Player.getObservation().addTurn(turn);
        }*/

        // This one will generate a turn object for us which we can then playback
        //Turn turn = sim.runNoGui(Data.currentValues.turnSeconds * 60);
        //sim.playTurn(turn);
        return turn;
    }

    ////////////// START OBSERVATION STUFF ///////////////
    // Observe several turns in a row
    public void observeTurns(int nNumTurns, boolean restart) {
        if (restart == true) {
            Gos.sim.restoreBaseSnap();
            //Player.resetCurrentObservationTurnNumber();
            Player.getObservation().reset();
        }

        m_bObserving = true;
        m_nNumTurnsToRun = nNumTurns;
        m_nTurnsRunSoFar = 0;
        advanceObservation();
    }

    public void observeFromCurrent() {
        // Just make sure observing is on, and then keep playing
        // from current.
        m_bObserving = true;
        sim.playFromCurrent();
    }

    public boolean observeOneTurn() {
        m_bObserving = true;
        m_nNumTurnsToRun = 1;
        m_nTurnsRunSoFar = 0;
        return advanceObservation();
    }

    public void observeFirstFrame() {
        m_bObserving = true;
        Turn turn = sim.startTurnAndPause(true);
        Player.getObservation().addTurn(turn);
    }

    public void stopObserving() {
        m_bObserving = false;
    }

    private boolean advanceObservation() {
        TurnSet obs = Player.getObservation();

        // Check if we can observe any further or if we hit our max
        if (obs.getMaxTurns() > 0 && obs.onLastTurn()) {
            return false;
        }
        m_bObserving = true;

        // If we are advancing an observation, what we do is clear out all
        // snapshots after this one, because we've effectively invalidated
        // them all due to randomness at this point.  So they won't seem to 
        // flow from each other.  So clear out all snapshots after the current
        // turn, and add current snapshot. 
        // (this is now done within the class itself when a turn is added)
        Turn turn = runOneTurn(true);
        Player.getObservation().addTurn(turn);
        return true;
    }

    public void observeFFWOneTurn() {
        m_bObserving = true;

        // This is kind of weird.  basically if the turn hasn't started yet,
        // we are creating and ffwing the turn.  But if it has started already,
        // we already have the Turn object so we don't want to start a new
        // turn.  But the sim should take care of that for us...
        Turn turn = sim.fastForwardToNextTurn(Player.getObservation().getCurrentTurn());
        if (turn != null) {
            // If we have a turn, that means we started from scratch, so
            // we need to trim and add it.  Actually we probably don't need to
            // trim because if we had no turn, there is nothing to trim.
            //Player.getObservation().trim();
            Player.getObservation().addTurn(turn);
        }
    }

    /*public void retreatObservation() {
        Observation obs = Player.getObservation();
        if (obs.getTurnNumber() > 0) {
            obs.subTurnNumber();
            // Restore state
            Gos.simRunner.restoreSimState(obs.getCurrentSnap());
        }
    }*/
    ////////////// END OBSERVATION STUFF ///////////////

    public boolean advanceTask() {
        //if (Player.getMaxTurns() > 0 && Player.getCurrentTurnNumber() >= Player.getMaxTurns()) {
          //  return false;
        //}
        TurnSet ts = Player.getPlayedTurns();
        if (ts.getMaxTurns() > 0 && ts.onLastTurn()) {
            return false;
        }
        sim.snapCurrent();
        m_bObserving = false;
        m_bActualRun = true;
        m_nTurnsRunSoFar = 0;
        m_nNumTurnsToRun = 1;
        //SystemSnapshot shot = new SystemSnapshot(sim);
        //actualSnapshots.add(shot);
        //Player.addCurrentTurnNumber();
        //Turn turn = runOneTurn(false); // Typically we don't need to record.  It's very CPU intensive.
        Turn turn = runOneTurn(true); // Now actually we always record so that we can use the new slider
        // But we have to make sure we free all the frames of our old turn so we don't waste memory.
        // We don't need them anymore anyway.
        Turn oldTurn = Player.getPlayedTurns().getCurrentTurn();
        if (oldTurn != null) {
            oldTurn.clearFrames();
        }
        Player.getPlayedTurns().addTurn(turn);
        return true;
    }

    public void skipToExercise(int num) {
        sim.start();
        //Advance the exercise twice past the surveys.
        Player.goToExercise(num);
        startSimulation();
    }

    public void skipSurveys() {
        skipToExercise(2);
    }

    public void startTest() {
        //resetSim();
        // We can just skip the surveys if we want.  
        if (Gos.testing == true) {
            //skipSurveys();
            skipToExercise(2);
        }

        if (Gos.skipSurveys == true) {
            skipSurveys();
            Gos.showStartSimulation();
        }

        // The player used the debugging Skip screen which shouldn't appear in the regular
        // game version
        if (Player.getSkipped() == true && Player.getCurrentExercise().getId() >= 2) {    
            Gos.showStartSimulation();
        }

        // At this point we need to show the stage
        Gos.stage.show();

        Task task = Player.getCurrentTask();
        if (task == null) {
            // We start with the survey so we would normally do this.
            //Gos.gos.showExercisePopup(Player.getCurrentExercise());
            Gos.gos.showAnswerScreen();
        }
        else {
            // But if we skip the survey we just begin like this.
            startTask(task);

            // If we are exercise 4, select the last shape.  Should there
            // be a post-processing for exercise setup?
            /*if (Player.getCurrentExercise().getId() == 4) {
                Gos.selectShape(sim.shapes.get(sim.shapes.size()-1));
            }*/
        }
    }

    public void resetSim() {
        //observeSnapshots.clear();
        //Player.getObservation().reset();
        //actualSnapshots.clear();
        sim.reset();
        maker.prepareExercise(Player.getCurrentExercise());
        
        // If it's the last exercise, we change the helper text a little bit.
        // A better way to do this is to just notify the GUI when we
        // update the exercise and then let it do whatever it wants.
        if (Player.getCurrentExercise().getId() == 4) {
            Gos.mainScene.getChangePanelSet().changeShapeHelperText("You can only change your own shape.  It is the shape with the circle around it.");
        }
    }

    public void startTask(Task task) {
        resetSim();
        Player.resetForNewTask();
        if (task == null) {
            return;
        }
        Player.setMaxTurns(task.getTurns());
        Gos.mainScene.onNewTask();
        // If we are doing the tutorial, we use some special processing to display the goal
        if (task.getName().equals("Tutorial")) {
            Player.updateTaskTrackers();
            //Tutorial.setTextForTutorialTurn(Player.getCurrentTurnNumber());
        }
        sim.start(); // If we haven't started yet, start now.
    }

    public void finishTask() {
        // Every time we finish, upload our data.  That way if something
        // happens with the final upload, at least we have something.  Also
        // we can inform the player if the uploads aren't working so they can
        // email the data or something.
        uploadData();

        Task task = Player.nextTask();
        if (task == null) {
            // Ok so we are out of tasks.  Start the questions
            // session in that case.
            //finishExercise();
            Gos.gos.showAnswerScreen();
        }
        else {
            startTask(task);
        }
    }

    // Start out the actual game part
    public void startSimulation() {
        //resetSim();

        // Now GUI stuff
        Gos.mainScene.onNewExercise();
        Gos.gos.showMainScreen();
        startTask(Player.getCurrentTask());
    }

    public void finishExercise() {
        //sim.stopUsingSuccess();

        // Player stuff
        Player.resetForNewTask();
        Player.nextExercise();
        if (Player.getCurrentExercise() != null) {
            // The Insanity task where things gain levels.  Maybe
            // an exercise should specify in its parameters whether to
            // use success or not.
            //if (Player.getCurrentExercise().getId() == 4) {
              //  sim.useSuccess();
            //}
            // If we have any pop-up text, show that
            // to the user first.
            Gos.gos.showExercisePopup(Player.getCurrentExercise());
        }

        if (Player.getCurrentExercise() == null) {
            // No more exercises, we are done.
            finishSim();
        }
        else if (Player.getCurrentTask() == null) {
            // No tasks, that means we just have questions.
            Gos.gos.showAnswerScreen();
        }
        else {
            // This is before we've shown the main panel set
            if (Player.getCurrentExercise().getId() == 2) {
                // Ok now we are starting the actual sim.
                
                // If we've completed the surveys, we should immediately upload the data because now
                // the player receives an ID and can use that to skip the surveys later.
                if (Gos.skipSurveys == false) {
                    uploadData();
                }
                Gos.showStartSimulation();
            }

            startSimulation();

            // This is after we've shown the main panel set
            if (Player.getCurrentExercise().getId() == 3) {
                // Ok now we are starting the real game after tutorial mode
                Gos.showStartRealGame();
            }

            // And now, give them their task text.  
            if (Player.getCurrentExercise().getId() >= 3) {
                Gos.showTaskWindow();
            }
        }
    }

    public void finishSim() {
        // Temporarily do the next few steps until the game is expanded.
        Player.endTime = System.currentTimeMillis();

        // And now record all our data.
        uploadData();
        
        Gos.betaWindow.showAndWait();
    }

    // Right now being done with questions is the same as being
    // done with the exercise.
    public void finishQuestions() {
        finishExercise();
    }

    public void uploadData() {
        //Utils.log("Upload temporarily disabled");
        //return;
        // Show some kind of "please wait while your data is uploaded
        // to the research server"
        // This works very strangely for some reason.
        //Gos.mainScene.showPleaseWaitForUploadWindow();

        // This is it, pretty simple call from here.
        FileTransfer.runUploadThread();
    }

   /* public void finishQuestion() {
        if (Player.nextQuestion() == null) {
            // Ok move to the next part.
            //Utils.log("Out of questions.");
            finishExercise();
        }
    }*/

    public void restorePreviousSimState() {
        sim.restorePreviousState();
    }

    public void restoreSimState(SystemSnapshot shot) {
        sim.restoreState(shot);
    }
    public void restoreSimState(SystemSnapshot shot, int frameNum) {
        sim.restoreState(shot, frameNum);
    }

    public void restoreObserveState() {
        // So, basically, if our current observation turn fits within the
        // turns we've recorded, restore it.  Otherwise, 
        // we can't restore it because that entry doesn't exist.  If
        // that's the case, that's fine, just restore the base snap.
        /*if (observeSnapshots.size() > Player.getCurrentObservationTurnNumber()) {
            sim.restoreState(observeSnapshots.get(Player.getCurrentObservationTurnNumber()));
        } 
        else {
            sim.restoreBaseSnap();
        }*/
        SystemSnapshot snap = Player.getObservation().getCurrentSnap();
        if (snap != null) {
            sim.restoreState(snap);
        }
        else {
            sim.restoreBaseSnap();
        }
    }

    public void test() {
        if (m_nNumTurnsToRun == 0) {
            m_nNumTurnsToRun = 1;
            return;
        }
        m_nNumTurnsToRun = 0;
        sim.startTurn();
    }

    @Override
    public void onEndTurn() {
        // If we are running a sequence just do that
        if (sequence.size() > 0) {
            // Pop one off and run the next.
            SystemSnapshot shot = sequence.get(0);
            sequence.remove(0);
            restoreSimState(shot);
            sim.startTurn();
            return;
        }

        if (m_bObserving == true) {
            // Snap an observe state from right now, so we can get back
            // here if we cancel.  We only do this for observations since
            // you can move back and forward.  We also cream this state
            // next time we advance anyway so it is ok to have it here.

            // I don't think I need this anymore since the observation object
            // keeps track of the turns, which already have the snap.
            //snapCurrentObserveState();
        }

        if (m_bActualRun == true) {
            Player.addCurrentTurnNumber();
            // If it's the tutorial we need some special processing
            Task task = Player.getCurrentTask();
            if (task != null && task.getName().equals("Tutorial")) {
                Player.updateTaskTrackers();
                //Tutorial.setTextForTutorialTurn(Player.getCurrentTurnNumber());
            }
            m_bActualRun = false;
        }

        // Otherwise see if we are running a set number of turns.
        // This is generally 1, but it could be more.
        m_nTurnsRunSoFar++;
        if (m_nNumTurnsToRun == 0) {
            // This means its just continously running
            sim.startTurn();
        }
        else if (m_nTurnsRunSoFar < m_nNumTurnsToRun) {
            if (m_bObserving == true) {
                advanceObservation();
            }
            else {
                sim.startTurn();
            }
        }
        else {
            // Done, ran all the turns we were asked to run.
            m_nTurnsRunSoFar = 0;
            m_nNumTurnsToRun = 1;
        }
    }

    public void runSequence(ArrayList<SystemSnapshot> shots) {
        if (shots == null || shots.size() <= 0) {
            return;
        }
        m_nTurnsRunSoFar = 0;
        sequence.clear();
        sequence.addAll(shots);
        SystemSnapshot shot = sequence.get(0);
        sequence.remove(0);
        restoreSimState(shot);
        sim.startTurn();
    }
    
    // All the stuff it gets from inheriting from SimulatorEventListener, most
    // of which we don't need.  But we do need the end turn event, which is implemented
    // above, and we may need other items in here in the future if we want to use the
    // runner to drive other aspects of the sim as well.
    public void onGuiUpdateRequired() { }
    public void updateOneFrame(boolean running, Turn currentTurn) { }
    public void onSimReset() { }
    public void onStartTurn() { }
    public void onPause() { }
    public void onShapeAdded(SysShape item) { }
    public void onShapeRemoved(SysShape item) { }
    public void onRaindropAdded(Raindrop item) { }
    public void onRaindropRemoved(Raindrop item) { }
    public void onSpikeAdded(Spike item) { }
    public void onSpikeRemoved(Spike item) { }
    public void onEarthpatchAdded(Earthpatch item) { }
    public void onEarthpatchRemoved(Earthpatch item) { }
    public void onGravityWellAdded(GravityWell item) { }
    public void onGravityWellRemoved(GravityWell item) { }
    public void onNumberOfShapesChanged(int numberShapes) { }
}