package gos.gui;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.*;
import javafx.beans.value.*;
import javafx.scene.input.*;
import javafx.scene.Scene;
import javafx.scene.shape.*; 
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.web.HTMLEditor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import gos.*;

public class ActionPanelBase extends VBox implements ClassInfo {

    protected VBox m_MainVBox;
    protected void setMainVBox(VBox newBox) { m_MainVBox = newBox; }

    protected ArrayList<VBox> controlsList = new ArrayList<VBox>();
    // The controlsList is actually all just VBoxes but we also 
    // want a list of the specific controls here like sliders,
    // etc.
    protected ArrayList<Node> localControlsList = new ArrayList<Node>();

    public ActionPanelBase() {
        super();

        // We set the main vbox to this obj by default, so that we
        // add everything directly to this node.  But we can change
        // that if we want to when we override this class, in case
        // we want to still add stuff to the actionpanel like normal
        // but also have it wrapped it some other construct.
        m_MainVBox = this;
    }

    // This allows the subclass  to do something with the 
    // specific control node, not just the VBox as in the controlsList.
    // I should probably rename these to be better in the future.
    public void addLocalControl(Node node) { 
        localControlsList.add(node);
    }

    public int getIndexForLocalControl(Node control) {
        for (int i = 0; i < localControlsList.size(); i++) {
            if (localControlsList.get(i).equals(control) == true) {
                return i;
            }
        }
        return 0;
    }

    // Take one of our controls as input and check for the corresponding
    // VBox container.
    public VBox getVBoxForLocalControl(Node control) {
        int num = getIndexForLocalControl(control);
        return controlsList.get(num);
    }

    public void addControl(VBox box) {
        controlsList.add(box);
        m_MainVBox.getChildren().add(box);
    }

    public void removeControls() {
        m_MainVBox.getChildren().removeAll(controlsList);
        controlsList.clear();
        localControlsList.clear();
    }

    public Slider createSlider() {
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(50);
        //slider.setShowTickLabels(true);
        //slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);
        slider.setMaxWidth(Constants.BUTTON_WIDTH + 10); // Why doesn't it work with just button width??
        return slider;
    }

    public static void setStandardVBoxParameters(VBox box) {
        box.setPadding(new Insets(2));
        box.setAlignment(Pos.CENTER);
        box.setSpacing(3);
    }

    private ActionPanelVBox createStandardVBox() {
        ActionPanelVBox box = new ActionPanelVBox();
        setStandardVBoxParameters(box);
        return box;
    }
    
    public ActionPanelVBox addSliderControl(Slider slider, String str) {
        ActionPanelVBox box = createStandardVBox();
        addControl(box);
        addLocalControl(slider);
        box.addLabel(str);
        box.addControl(slider);
        //Text text = new Text(str);
        //box.m_MainVBox.getChildren().add(text);
        //box.m_MainVBox.getChildren().add(slider);
        return box;
    }

    public ActionPanelVBox addColorControl(ColorPicker picker, String str) {
        ActionPanelVBox box = createStandardVBox();
        addControl(box);
        addLocalControl(picker);
        box.addLabel(str);
        box.addControl(picker);
        //Text text = new Text(str);
        //box.m_MainVBox.getChildren().add(text);
        //box.m_MainVBox.getChildren().add(picker);
        return box;
    }

    public ActionPanelVBox addChoiceBox(ChoiceBox cb, String str) {
        ActionPanelVBox box = createStandardVBox();
        addControl(box);
        addLocalControl(cb);
        cb.setPrefWidth(Constants.BUTTON_WIDTH);
        box.addLabel(str);
        box.addControl(cb);
        //Text text = new Text(str);
        //box.m_MainVBox.getChildren().add(text);
        //box.m_MainVBox.getChildren().add(cb);
        return box;
    }

    public ActionPanelVBox addCheckBox(CheckBox cb, String str) {
        ActionPanelVBox box = createStandardVBox();
        addControl(box);
        addLocalControl(cb);
        box.addControl(cb);
        //Text text = new Text(str);
        //box.m_MainVBox.getChildren().add(text);
        //cb.setPrefWidth(Constants.BUTTON_WIDTH);
        cb.setText(str);
        //box.m_MainVBox.getChildren().add(cb);
        return box;
    }

    public ActionPanelVBox addButton(Button btn) {
        ActionPanelVBox box = createStandardVBox();
        addControl(box);
        box.addControl(btn);
        //box.m_MainVBox.getChildren().add(btn);
        addLocalControl(btn);
        return box;
    }

    // Just for development stuff like placeholders
    public ActionPanelVBox addTempText(String str) {
        ActionPanelVBox box = createStandardVBox();
        addControl(box);
        box.addLabel(str);
        //Text text = new Text(str);
        //box.m_MainVBox.getChildren().add(text);
        addLocalControl(box.getLabel());
        return box;
    }

    // A RevealButton is a VBox
    public RevealButton addRevealButton(RevealButton rvb) {
        // Set it up to the same parameters as the standard VBox
        setStandardVBoxParameters(rvb);
        addControl(rvb);
        // The local control is the changing text
        addLocalControl(rvb.getLabel());
        return rvb;
    }

    // Apply changes.
    public void submit() { }
}