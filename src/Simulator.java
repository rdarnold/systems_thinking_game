package gos;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.Animation;

// Try to keep this decoupled from the UI
public class Simulator {

    // This could be defined by the outer constructs as well
    int width = Constants.SIM_WIDTH;
    int height = Constants.SIM_HEIGHT;

    //used to store the current time to calculate fps
    private long currentTime = 0;
    //used to store the last time to calculate fps
    private long lastTime = 0;
    private long lastUpdate = 0;
    //fps counter
    private int fps = 0;//text to display fps
    //acumulated difference between current time and last time
    private double delta = 0;
    private AnimationTimer mainLoop;
    private boolean running = false;
    private boolean paused = false;
    private boolean started = false;
    private boolean usingSuccess = false;
    //private int numFramesPassed = 0;

    public ArrayList<SysShape> shapes;
    public ArrayList<Raindrop> rainDrops;
    public ArrayList<Spike> spikes;
    public ArrayList<Earthpatch> patches;

    public ArrayList<SysShape> getShapes() { return shapes; }
    public ArrayList<Raindrop> getDrops() { return rainDrops; }
    public ArrayList<Spike> getSpikes() { return spikes; }
    public ArrayList<Earthpatch> getPatches() { return patches; }

    public ArrayList<SystemSnapshot> snapshots;
    public SystemSnapshot currentSnapshot;
    public SystemSnapshot baseSnapshot; // The original system

    // This can be set by an external entity so that it can
    // be notified of various changes to the simulation data
    // actually we are going to have a list of these.  We can add as many
    // as we need.  But they should only activate if they are visible and
    // being shown as the current screen.
    //private SimulatorEventListener simulatorListener;
    private ArrayList<SimulatorEventListener> simulatorListeners;

    public Turn playingTurn;  // If we're playing from a previously recorded set.
    public Turn currentTurn;  // Always the current turn being played no matter what.

    public boolean isRunning() { return running; }
    public boolean isPaused() { return paused; }
    public void pause() { 
        paused = true; 
        signalPause();
    }
    public void unpause() { paused = false; }

    // Are we using a success rating?  If so, instead of dividing we
    // add and subtract success
    public boolean isUsingSuccess() { 
        if (Player.getCurrentExercise() == null)
            return false;
        return Player.getCurrentExercise().getUsesLevels(); 
    }
    public boolean noChangeSelection() { 
        if (Player.getCurrentExercise() == null)
            return false;
        return Player.getCurrentExercise().getNoSelect(); 
    }
    public boolean noPlayerDeath() { 
        if (Player.getCurrentExercise() == null)
            return false;
        return Player.getCurrentExercise().getNoDeath(); 
    }
    //public boolean isUsingSuccess() { return usingSuccess; }
    //public void useSuccess() { usingSuccess = true; }
    //public void stopUsingSuccess() { usingSuccess = false; }

    public Simulator() {
        simulatorListeners = new ArrayList<SimulatorEventListener>();
        init();
    }

    // We can pass it anything that implements the event listener,
    // like a GosSceneBase
    public Simulator(SimulatorEventListener listener) {
        simulatorListeners = new ArrayList<SimulatorEventListener>();
        addSimulatorEventListener(listener);
        init();
    }

    public void init() {
        initMainLoop();
        shapes = new ArrayList<SysShape>();
        rainDrops = new ArrayList<Raindrop>();
        spikes = new ArrayList<Spike>();
        patches = new ArrayList<Earthpatch>();
        snapshots = new ArrayList<SystemSnapshot>();
        currentSnapshot = new SystemSnapshot();
        baseSnapshot = new SystemSnapshot();
        reset();
    }

    public void initMainLoop() {
        //lastTime = System.nanoTime();
        mainLoop = new AnimationTimer() {
            @Override
                public void handle(long now) {
                /*currentTime = now;
                fps++;
                delta += currentTime-lastTime;

                if (delta > Constants.ONE_SECOND_IN_NANOSECONDS) {
                    //updateFpsText(fps);
                    delta -= Constants.ONE_SECOND_IN_NANOSECONDS;
                    fps = 0;
                }
                
                //updateTimeText();
                lastTime = currentTime;*/

                if (paused == true)
                    return;

                updateOneFrame(running);
                if (running == true) {
                    if (currentTurn.onMaxFrame() == true) {
                        endTurn();
                    }
                    else {
                        currentTurn.advanceFrameNumber(); //numFramesPassed++;
                    }
                    //if (currentTurn.getCurrentFrameNumber() >= Data.getFramesPerTurn()) {
                      //  endTurn();
                    //}
                }

                //updateOneFrame(running);
            }
        };
    }

    // Artificially advance the sim to the end.  If we had to create a new
    // turn because the one passed in was null, return that.
    public Turn fastForwardToNextTurn(Turn turn) {
        Turn createdTurn = null;

        // We need to know if we are starting from scratch, i.e. we need to add
        // the new turn to some array, or if we are continuing from an existing
        // turn. 
        if (turn == null) {
            createdTurn = startTurn(true);
        }
        else {
            setTurn(turn);
        }

        while (true) { //currentTurn.onMaxFrame() == false) {
            updateOneFrame(true);
            if (currentTurn.onMaxFrame() == true) {
                break;
            }
            currentTurn.advanceFrameNumber();
        }
        endTurn();
        return createdTurn;
    }

    public SysShape spawnRandomSysShape(double x, double y) {
        SysShape shape = addShape((int)x, (int)y, Utils.number(3, 8));
        shape.setSize(Utils.number(25, (int)shape.getMaxSize()));
        shape.setRandomSpinSpeed(); //Utils.random(1, 5));
        if (Utils.number(0, 1) == 1) {
            shape.setSpinLeft();
        }
        return shape;
    }

    public SysShape addShape(SysShape shape) {
        shapes.add(shape);

        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        int size = getNumberLiveShapes();
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onShapeAdded(shape);
            simulatorListeners.get(i).onNumberOfShapesChanged(size);
        }
        // And we should indicate to the Gui that another shape was added
        // so that it can be added to the scene graph if necessary
        /*if (simulatorListener != null) {
            // This actually calls the MainScreen.onGuiUpdateRequired method,
            // which then does all the GUI updating that we need.  If we dont
            // have a gui wen can just not hook up this event and it should all
            // work fine.
            simulatorListener.onShapeAdded(shape);
        }*/
        return shape;
    }

    public SysShape addShape(int centerX, int centerY, int numCorners) {
        SysShape shape = new SysShape(this);
        shape.makeShape(centerX, centerY, numCorners);
        return addShape(shape);
    }

    // Run with no GUI, essentially generating a Turn object (list of snapshots) that we can 
    // then play back or save or do whatever we want with.
    // Create templist as member var so we don't have to constantly re-allocate memory.
    private ArrayList<SimulatorEventListener> tempListeners = new ArrayList<SimulatorEventListener>();
    public Turn runNoGui(int nNumFrames) {
        tempListeners.addAll(simulatorListeners);
        //SimulatorEventListener tempListener = simulatorListener;
        // Turn off the listener so nothing updates
        simulatorListeners.clear();
        //simulatorListener = null;
        Turn turn = new Turn();
        turn.addFrame(this);
        playingTurn = null;
        for (int i = 0; i < nNumFrames; i++) {
            updateOneFrame(true);
            turn.addFrame(this);
        }
        // Put the listener back in place.
        //simulatorListener = tempListener;
        simulatorListeners.addAll(tempListeners);
        tempListeners.clear();
        return turn;
    }

    public void playTurn(Turn turn) {
        playingTurn = turn;
    }

    Turn testTurn; // = new Turn();
    // This is a test to see if our deallocation is working properly for frames
    public void frameTest() {
        testTurn = new Turn();
        for (int i = 0; i < 1000; i++) {
            testTurn.addFrame(this);
        }
        Utils.log("Allocated...");
    }
    public void frameTestClear() {
        testTurn.clearFrames();
        testTurn = null;
        Utils.log("Cleared...");
    }

    public void updateOneFrame(boolean running) {
        // Ok in this case we are playing through a turn,
        // we've pre-generated everything already.
        if (playingTurn != null) {
            SystemSnapshot snap = playingTurn.getNext();
            // Check if we're done
            if (snap == null) {
                playingTurn = null;
                return;
            } 
            // Not done, restore the snap
            snap.restore(this);
            signalGuiUpdateRequired();
            return;
        }

        if (running == true) {
            updatePatches();
            updateSpikes();
            updateRain();
            updateGravity();
            updateShapes(running);
            cullTheDead();
            //playingTurn.addFrame(this);

            // We should always be recording our turn no matter what.
            // Because if you run the sim you always run an entire turn.
            // Well what if you want to play from a certain point, that should
            // work too, almost like you're watching the frames of a video.
            // You might want to scroll back a bit and then hit play and play the
            // "rest of the turn" from where you are.
            // No that's not true, I changed it to not always record because it
            // was really slow with lots of shapes on screen at once.
            if (currentTurn != null && currentTurn.recordFrames == true) {
                currentTurn.addFrame(this);
            }

            // Update any GUI elements we might have tied to the sim, and pass
            // it our turn object.
            /*if (simulatorListener != null) {
                simulatorListener.updateOneFrame(running, currentTurn);
            }*/
        }
        else {
            // We can make them spin even if the turn isn't
            // running just for visual effect.  This might be helpful
            // when answering questions because it's a way to visualize
            // the rates of things to help predict the future.
            updateShapes(running);
        }

        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).updateOneFrame(running, currentTurn);
        }

        if (running == true) {
            signalGuiUpdateRequired();
        }
    }

    public void updateShapes(boolean running) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            SysShape shape = shapes.get(i);
            shape.updateOneFrame(running, this);
        }

        // Make sure we always have something selected if there
        // is something to select.
        if (shapes != null && shapes.size() > 0 && Player.getSelectedShape() == null) {
            Gos.selectShape(shapes.get(0));
        }
    }

    public void tryAbsorbRain(SysShape shape, Raindrop drop) {
        shape.absorbRain(drop, this);
        // But now check shape collision.  If it can't expand,
        // set size back.
        //if (checkShapeCollisions(shape) == true) {
          //  shape.setSizeBack();
       // }
        removeRaindrop(drop);
    }

    public boolean checkRaindropCollisions(Raindrop drop) {
        //for (SysShape shape : shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            SysShape shape = shapes.get(i); 
            if (drop.intersects(shape) == true) {
                tryAbsorbRain(shape, drop);
                return true;
            }
        }
        
        //for (Earthpatch patch : patches) {
        for (int i = 0; i < patches.size(); i++) {
            Earthpatch patch = patches.get(i); 
            if (drop.intersects(patch) == true) {
                patch.absorbRain(drop);
                removeRaindrop(drop);
                return true;
            }
        }

        // We will have spikes ignore rain actually...
        /*for (Spike spike : spikes) {
            if (drop.intersects(spike) == true) {
                spike.absorbRain(drop);
                removeRaindrop(drop);
                return true;
            }
        }*/

        return false;
    }

    public void addRaindrop(Raindrop drop) {
        rainDrops.add(drop);
        // Signal that it was added
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onRaindropAdded(drop);
        }
    }

    public void addRandomRaindrop() {
        Raindrop drop = new Raindrop(this);
        double speed = Data.currentValues.rainSpeedVar * Utils.random(2, 4);
        if (speed < 0) {
            speed = 0.1; // Minimum speed, they have to move a little at least.
        }
        switch (Data.currentValues.rainOrigin) {
            case 0: // Top
                // Starting from top means we want to move down towards bottom.
                drop.moveTo(Utils.number(0, width), 0);
                drop.setFall(Constants.Dir.Bottom, speed);
                break;
            case 1: // Bottom
                drop.moveTo(Utils.number(0, width), height);
                drop.setFall(Constants.Dir.Top, speed);
                break;
            case 2: // Left
                drop.moveTo(0, Utils.number(0, height));
                drop.setFall(Constants.Dir.Right, speed);
                break;
            case 3: // Right
                drop.moveTo(width, Utils.number(0, height));
                drop.setFall(Constants.Dir.Left, speed);
                break;
        }
        addRaindrop(drop);
    }

    public void removeRaindrop(Raindrop drop) {
        rainDrops.remove(drop);
        // Signal that it was removed
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onRaindropRemoved(drop);
        }
        /*if (simulatorListener != null) {
            simulatorListener.onRaindropRemoved(drop);
        }*/
    }

    // See where this shape is in our shapes array
    public int getShapeIndex(SysShape shape) {
        if (shape == null)
            return 0;
        for (int i = 0; i < shapes.size(); i++) {
            if (shape.equals(shapes.get(i))) {
                return i;
            }
        }
        return 0;
    }

    public void removeShape(SysShape shape) {
        shapes.remove(shape);
        if (Player.getSelectedShape() == shape) {
            // What if we have no shapes left?
            if (shapes.size() > 0) {
                Gos.selectShape(shapes.get(0));
            }
            else {
                Gos.selectShape(null);
            }
        }
        // Signal that it was removed
        /*if (simulatorListener != null) {
            simulatorListener.onShapeRemoved(shape);
        }*/
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        int size = getNumberLiveShapes();
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onShapeRemoved(shape);
            simulatorListeners.get(i).onNumberOfShapesChanged(size);
        }
    }

    public void removeSpike(Spike spike) {
        spikes.remove(spike);
        // Signal that it was removed
        /*if (simulatorListener != null) {
            simulatorListener.onSpikeRemoved(spike);
        }*/
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onSpikeRemoved(spike);
        }
    }

    // Destroy a shape to destroy all spikes in the system
    public void sacrificeShapeForSpikes(SysShape shape) {
        for (int i = spikes.size() - 1; i >= 0; i--) {
            removeSpike(spikes.get(i));
        }
        removeShape(shape);
    }

    public double calcRainDropsPerSecond() {
        return Data.currentValues.rainRate * 60;
    }

    public void updateRain() {
        for (int i = rainDrops.size() - 1; i >= 0; i--) {
            Raindrop drop = rainDrops.get(i);

            // Rain falls
            if (drop.update() == false) {
                removeRaindrop(drop);
                continue;
            }

            // If a Raindrop hits a shape it tries to grow the shape
            // But shapes can't grow to the point where their borders 
            // collide, so if there is a collision, prevent the growth.
            checkRaindropCollisions(drop);
        }

        // Create new rain randomly; but not every frame
        // Max rate is one a frame.
        // Min rate is one a second.
        //double var = Data.currentValues.rainRate * 60;
        double var = calcRainDropsPerSecond();
        if (Utils.number(0, 60) > (int)var) {
            return;
        }

        addRandomRaindrop();
    }

    public void removeEarthpatch(Earthpatch item) {
        patches.remove(item);
        // Signal that it was removed
        /*if (simulatorListener != null) {
            simulatorListener.onEarthpatchRemoved(item);
        }*/
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onEarthpatchRemoved(item);
        }
    }

    public void addEarthpatch(Earthpatch patch) {
        // We really do need to limit this just to help the program.
        // But I want it to be a little higher.
        if (patches.size() > 200) {
            return;
        }
        patches.add(patch);
        // Signal that it was added
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onEarthpatchAdded(patch);
        }
    }

    public void updatePatches() {
        for (int i = patches.size() - 1; i >= 0; i--) {
            Earthpatch patch = patches.get(i);
            
            patch.update();

            if (patch.getSize() <= 0) {
                if (patch.getTurningToSpike() == true) {
                    // Spawn a spike
                    addSpike(patch.spawnSpike());
                }
                removeEarthpatch(patch);
                continue;
            }
        }
        
        // Create new rain randomly; but not every frame;
        // this is based on the "irrigation rate"
        if (Utils.number(0, 15) != 1) {
            return;
        }
    }

    public void addSpike(Spike spike) {
        spikes.add(spike);
        // Signal that it was added
        /*if (simulatorListener != null) {
            simulatorListener.onSpikeAdded(spike);
        }*/
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onSpikeAdded(spike);
        }
    }

    public void addRandomSpike() {
        Spike spike = new Spike(this);
        int num = (int)(spike.getSize()/2.0);
        spike.moveTo(Utils.number(num, width - num), Utils.number(num, height - num));
        addSpike(spike);
    }

    public void updateSpikes() {
        for (int i = spikes.size() - 1; i >= 0; i--) {
            Spike spike = spikes.get(i);
            
            spike.update();

            if (spike.getSize() <= 0) {
                removeSpike(spike);
                continue;
            }
        }

        // Based on the amount of earth, or some external rate
        // of something, generate spikes.  For now just do it randomly.

        // Create new rain randomly; but not every frame
        /*if (Utils.number(0, 30) != 1) {
            return;
        }

        addRandomSpike();*/
    }

    public void updateGravity() {
        if (Data.currentValues.gravityRules == Constants.GravityRules.Off.getValue()) {
            return;
        }
        // For each shape, exercise its gravitational pull
        // on all other shapes.
        //for (SysShape shape : shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            SysShape shape = shapes.get(i); 
            //for (SysShape otherShape : shapes) {
            for (int j = 0; j < shapes.size(); j++) {
                SysShape otherShape = shapes.get(j); 
                if (shape == otherShape || shape.equals(otherShape)) {
                    continue;
                }

                // Apply gravity changes from each other shape
                shape.receiveGravity(otherShape, getGravity(otherShape));
            }

            // Also all shapes receive pull or "gravity" from
            // earth patches as well.
            //for (Earthpatch patch : patches) {
            for (int j = 0; j < patches.size(); j++) {
                Earthpatch patch = patches.get(j); 
                // Apply gravity changes from each other shape
                shape.receiveGravity(patch, (getGravity(patch) * 4));
            }

            // Now evenly apply it.
            shape.applyGravity();
        }
    }

    // Functionalize these in case we want to muck with gravity
    // at all.
    public double getGravity(MovablePolygon item) {
        return item.getSize();
    }

    public double getGravity(MovableCircle item) {
        return item.getSize();
    }

    public int calcTotalSizeStolen() {
        int num = 0;
        //for (SysShape shape : shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            SysShape shape = shapes.get(i); 
            num += shape.getSizeStolen();
        }
        return num;
    }

    public int calcTotalSizeGiven() {
        int num = 0;
        //for (SysShape shape : shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            SysShape shape = shapes.get(i); 
            num += shape.getSizeGiven();
        }
        return num;
    }

    public double calculateTotalGravity() {
        double totalGravity = 0;
        //for (SysShape shape : shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            SysShape shape = shapes.get(i); 
            totalGravity += getGravity(shape);
        }
        for (int i = 0; i < patches.size(); i++) {
            Earthpatch patch = patches.get(i); 
            totalGravity += getGravity(patch);
        }
        return totalGravity;
    }

    public int getNumberLiveShapes() {
        // Some might be dead and we don't want to count them
        // so we can't just use the sizes of the array.
        int num = 0;
        for (int i = 0; i < shapes.size(); i++) {
            SysShape shape = shapes.get(i); 
            if (shape.isDead() == false) {
                num++;
            }
        }
        return num;
    }

    public void cullTheDead() {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            SysShape shape = shapes.get(i);
            if (shape.isDead() == true && shape.getSize() <= shape.getMinSize()) {
                removeShape(shape);
            }
        }

        for (int i = patches.size() - 1; i >= 0; i--) {
            Earthpatch item = patches.get(i);
            if (item.getSize() <= 0) {
                removeEarthpatch(item);
            }
        }

        for (int i = spikes.size() - 1; i >= 0; i--) {
            Spike item = spikes.get(i);
            if (item.getSize() <= item.getMinSize()) {
                removeSpike(item);
            }
        }
    }

    public void reset(Values values) {
        Data.currentValues.setTo(values);

        //Player.resetCurrentTurnNumber();
        signalReset();
        //setupShapes();
        //setupPatches();
        shapes.clear();
        patches.clear();
        rainDrops.clear();
        spikes.clear();

        //environment = values.environment;

        //matter = addResource(Constants.ResourceType.Matter, values.overallMatter);
        //energy = addResource(Constants.ResourceType.Energy, values.overallEnergy);
    }

    public void reset() {
        reset(Data.startingValues);
    }

    /*public void setSimulatorEventListener(SimulatorEventListener listener) {
        this.simulatorListener = listener;
    }*/
    public void addSimulatorEventListener(SimulatorEventListener listener) {
        if (simulatorListeners == null) {
            simulatorListeners = new ArrayList<SimulatorEventListener>();
        }
        if (simulatorListeners.contains(listener)) {
            return;
        }
        simulatorListeners.add(listener);
    }
    
    public void snapCurrent() {
        currentSnapshot.snap(this);
    }

    public void snapBase() {
        baseSnapshot.snap(this);
    }

    public void restoreCurrentSnap() {
        running = false;
        currentSnapshot.restore(this);
        signalGuiUpdateRequired();
    }

    public void restoreBaseSnap() {
        running = false;
        baseSnapshot.restore(this);
        signalGuiUpdateRequired();
    }

    public void startTurn() {
        // On a standard turn, we won't record.  So must specify it.'
        startTurn(false);
    }

    // This shouldnt keep track of anything at all, it literally just launches
    // the sim from its current state and runs one turn based on the current
    // length of a turn.  It doesnt check anything, it just works when called.
    public Turn startTurn(boolean record) {
        unpause(); // Unpause just in case somehow something messed up
        currentTurn = new Turn(); 
        currentTurn.setMaxFrames(Data.getFramesPerTurn());
        // If we want to record the turn, we can.  This allows us to scroll back
        // through every frame with precise detail, but it is very CPU and memory-intensive
        // to the point where it is quite noticeable on some machiens so we do'nt
        // always do it.
        currentTurn.recordFrames = record;
        signalStartTurn();
        running = true;
        return currentTurn;
    }

    // Start up the turn but just run the first frame and then wait while paused.
    public Turn startTurnAndPause(boolean record) {
        Turn turn = startTurn(record);
        pause();

        // Just update and advance to the first frame
        updateOneFrame(true); 
        currentTurn.advanceFrameNumber();
        return turn;
    }

    public void restorePreviousState() {
        if (snapshots.size() > 0) {
            SystemSnapshot shot = snapshots.get(snapshots.size() - 1);
            restoreState(shot);
            snapshots.remove(shot);
        }
    }

    public int getCurrentFrameNumber() { 
        if (currentTurn == null) 
            return 0;
        return currentTurn.getCurrentFrameNumber(); 
    }

    public int getMaxFrames() { 
        if (currentTurn == null) 
            return 0;
        return currentTurn.getMaxFrames(); 
    }
    

    public void goToFrame(int frameNum) {
        if (currentTurn == null)
            return;
        int num = frameNum;
        num = Utils.clamp(num, 0, currentTurn.getFrames().size() - 1);
        SystemSnapshot shot = currentTurn.getFrame(num);
        if (shot == null)
            return;
        shot.restore(this);
        currentTurn.setCurrentFrameNumber(num);
    }

    // Set to the passed in turn and go to wherever the current frame is
    // within that turn.
    public void setTurn(Turn turn) {
        currentTurn = turn;
        if (turn != null) {
            goToFrame(turn.getCurrentFrameNumber());
        }
    }
    
    // This will play from the current frame in the passed in turn
    public void play(Turn turn) {
        currentTurn = turn;
        playFromCurrent();
    }

    public void playFromCurrent() {
        if (currentTurn == null)
            return;
        playFrom(currentTurn.getCurrentFrameNumber());
    }

    public void playFrom(int nFrameNum) {
        if (currentTurn == null)
            return;
        running = true; // We are playing so running will have to be true.
        int num = currentTurn.getCurrentFrameNumber();
        num = Utils.clamp(num, 0, (currentTurn.getFrames().size() - 1));
        SystemSnapshot shot = currentTurn.getFrame(num);
        shot.restore(this);
        currentTurn.setCurrentFrameNumber(num);
        currentTurn.trimAfterCurrent();
        unpause();
    }

    public void restoreState(SystemSnapshot shot) {
        shot.restore(this);
        signalGuiUpdateRequired();
    }
    public void restoreState(SystemSnapshot shot, int frameNum) {
        shot.restore(this);
        signalGuiUpdateRequired();

        // When we restore to an earlier state, we also reset the
        // turn that we're recording so we start from the right place.
        if (currentTurn != null) {
            currentTurn.setCurrentFrameNumber(frameNum);
            currentTurn.trimAfterCurrent();
        }
    }

    public void endTurn() {
        running = false;
        Data.saveCurrentValues();
        signalGuiUpdateRequired();
        signalEndTurn();
    }

    public void start() {
        //running = true;
        //numFramesPassed = 0;
        if (started == true) {
            return;
        }
        started = true;
        mainLoop.start();
        signalGuiUpdateRequired();
    }

    public void stop() {
        //running = false;
	    mainLoop.stop();
        //Data.saveCurrentValues();
        //signalGuiUpdateRequired();
    }

    public void signalEndTurn() {
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onEndTurn();
        }
    }

    public void signalStartTurn() {
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onStartTurn();
        }
    }

    public void signalReset() {
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onSimReset();
        }
    }

    public void signalPause() {
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onPause();
        }
    }

    public void signalGuiUpdateRequired() {
        //for (SimulatorEventListener simulatorListener : simulatorListeners) {
        for (int i = 0; i < simulatorListeners.size(); i++) {
            simulatorListeners.get(i).onGuiUpdateRequired();
        }
        // This triggers an event bound from an outside class that handles the GUI
       /* if (simulatorListener != null) {
            // This actually calls the MainScreen.onGuiUpdateRequired method,
            // which then does all the GUI updating that we need.  If we dont
            // have a gui wen can just not hook up this event and it should all
            // work fine.
            simulatorListener.onGuiUpdateRequired();
        }*/
    }
}