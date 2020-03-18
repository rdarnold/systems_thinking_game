
package gos.gui;

import javafx.scene.Scene;
import javafx.scene.Node;
//import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import gos.*;

// Basically just an interface...
public abstract class PanelSetBase implements SimulatorEventListener, ClassInfo   {
    
    MainScreen parent;

    PanelLeftBase m_LeftPanelBase;
    PanelTopBase m_TopPanelBase;
    PanelBottomBase m_BottomPanelBase;

    private Timeline fadeInTimeline;
    private Timeline fadeOutTimeline;

    public PanelSetBase(MainScreen main) {
        parent = main;
    }

    public void addPanels(PanelLeftBase left, PanelTopBase top, PanelBottomBase bottom) {
        m_LeftPanelBase = left;
        m_TopPanelBase = top;
        m_BottomPanelBase = bottom;
        
        // And set up the fade stuff
        fadeInTimeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(300),
                        new KeyValue (m_LeftPanelBase.opacityProperty(), 1),
                        new KeyValue (m_TopPanelBase.opacityProperty(), 1),
                        new KeyValue (m_BottomPanelBase.opacityProperty(), 1)); 
        fadeInTimeline.getKeyFrames().add(key);   
        fadeInTimeline.setOnFinished((ae) -> onFadeInFinished());

        /*fadeOutTimeline = new Timeline();
        key = new KeyFrame(Duration.millis(300),
                        new KeyValue (m_LeftPanelBase.opacityProperty(), 0),
                        new KeyValue (m_TopPanelBase.opacityProperty(), 0),
                        new KeyValue (m_BottomPanelBase.opacityProperty(), 0)); 
        fadeOutTimeline.getKeyFrames().add(key);   */
        //fadeOutTimeline.setOnFinished((ae) -> super.close());
    }

    // Meant to be overridden, this is called when the fadein is done.
    // This allows us to do things when the fade in is complete rather than before.
    public void onFadeInFinished() { }

    // These will call back up to the subclasses if they implement overrides
    public void reset() {
        if (m_LeftPanelBase != null) {
            m_LeftPanelBase.reset();
        }

        if (m_TopPanelBase != null) {
            m_TopPanelBase.reset();
        }
    }

    public void update() {
        if (m_LeftPanelBase != null) {
            m_LeftPanelBase.update();
        }

        if (m_TopPanelBase != null) {
            m_TopPanelBase.update();
        }
    }

    public boolean isShown() {
        if (m_BottomPanelBase == null || m_LeftPanelBase == null || m_TopPanelBase == null)
            return false;
        
        if (parent.overallRoot.getBottom() == null ||
            parent.overallRoot.getLeft() == null ||
            parent.overallRoot.getTop() == null)
            return false;

        if (parent.overallRoot.getBottom().equals(m_BottomPanelBase) == false) {
            return false;
        }
        if (parent.overallRoot.getLeft().equals(m_LeftPanelBase) == false) {
            return false;
        }
        if (parent.overallRoot.getTop().equals(m_TopPanelBase) == false) {
            return false;
        }

        return true;
    }

    public void show(boolean bFade) {
        boolean bSet = false;

        // Sometimes we may want to just set it with no fade, although I don't
        // think I use this anywhere.
        if (bFade == false) {
            m_BottomPanelBase.setOpacity(1);
            m_LeftPanelBase.setOpacity(1);
            m_TopPanelBase.setOpacity(1);
            parent.overallRoot.setBottom(m_BottomPanelBase);
            parent.overallRoot.setLeft(m_LeftPanelBase);
            parent.overallRoot.setTop(m_TopPanelBase);
            return;
        }
        
        // Check and set each thing... 
        if (parent.overallRoot.getBottom() == null ||
            parent.overallRoot.getBottom().equals(m_BottomPanelBase) == false) {
            m_BottomPanelBase.setOpacity(0);
            parent.overallRoot.setBottom(m_BottomPanelBase);
            bSet = true;
        }

        if (parent.overallRoot.getLeft() == null ||
            parent.overallRoot.getLeft().equals(m_LeftPanelBase) == false) {
            m_LeftPanelBase.setOpacity(0);
            parent.overallRoot.setLeft(m_LeftPanelBase);
            bSet = true;
        }

        if (parent.overallRoot.getTop() == null ||
            parent.overallRoot.getTop().equals(m_TopPanelBase) == false) {
            m_TopPanelBase.setOpacity(0);
            parent.overallRoot.setTop(m_TopPanelBase);
            bSet = true;
        }

        // And because this panel set is what everything inherits from,
        // it's so easy to do a fade transition here for all panelset screens.
        // woop.
        // But only play it if we actually changed something.
        if (bSet == true) {
            fadeInTimeline.play();
        }

        //parent.overallRoot.setBottom(m_BottomPanelBase);
        //parent.overallRoot.setLeft(m_LeftPanelBase);
        //parent.overallRoot.setTop(m_TopPanelBase);
    }

    public void show() {
        this.show(true);
    }

    // Override if the set has a SelectableShapePanel in it
    public void onSelectedShapeUpdated() { }

    /*public abstract void createBuildingBlocks();
    public abstract void createMainScene();
    public abstract void finalizeCreation();
    public abstract void onGuiUpdateRequired();
    public abstract void reset();
    public abstract void update();*/

    public void onGuiUpdateRequired() { }
    public void updateOneFrame(boolean running, Turn currentTurn) { }
    public void onSimReset() { }
    public void onStartTurn() { }
    public void onEndTurn() { }
    public void onPause() { }
    /*public void onShapeAdded(SysShape item) { }
    public void onShapeRemoved(SysShape item) { }
    public void onRaindropAdded(Raindrop item) { }
    public void onRaindropRemoved(Raindrop item) { }
    public void onSpikeAdded(Spike item) { }
    public void onSpikeRemoved(Spike item) { }
    public void onEarthpatchAdded(Earthpatch item) { }
    public void onEarthpatchRemoved(Earthpatch item) { }
    public void onGravityWellAdded(GravityWell item) { }
    public void onGravityWellRemoved(GravityWell item) { }*/
    public void onNumberOfShapesChanged(int numberShapes) { }
}