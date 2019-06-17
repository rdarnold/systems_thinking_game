
package gos.gui;

import javafx.scene.Scene;
import javafx.scene.Node;
//import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import gos.*;

// Basically just an interface...
public abstract class GosSceneBase extends Scene implements SimulatorEventListener, ClassInfo  {
    public Gos gos; // Keep a ref to our main app
    public Simulator sim;
    public SimRunner simRunner;
    public BorderPane overallRoot;

    public GosSceneBase(Gos theGos, BorderPane root, int wid, int hgt) {
        super(root, wid, hgt);
        overallRoot = root;
        gos = theGos;
        sim = gos.sim;
        simRunner = gos.simRunner;
        createBuildingBlocks();
        createMainScene();
        finalizeCreation();
    }

    public void setSim(Simulator s, SimRunner runner) {
        sim = s;
        simRunner = runner;
    }

    public abstract void createBuildingBlocks();
    public abstract void createMainScene();
    public abstract void finalizeCreation();
    public abstract void onGuiUpdateRequired();
    public abstract void reset();
    public abstract void update();

    public void updateOneFrame(boolean running, Turn currentTurn) { }
    public void onSimReset() { }
    public void onStartTurn() { }
    public void onEndTurn() { }
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