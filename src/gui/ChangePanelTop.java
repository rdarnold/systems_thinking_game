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
import javafx.beans.property.*;
import javafx.beans.binding.Bindings;
 
import gos.*;

public class ChangePanelTop extends AdjustOverallValuesPanel {

    ChangePanelSet m_ParentPanelSet;

    private ChangePanelTop thisScreen;

    Label infoLabel;

    public ChangePanelTop(ChangePanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        getInfoArea().setName("Change System"); //\r\nScreen");

        /*Label lab = new Label("Use the sliders and drop-down selection boxes to " +
        "make changes to the system.  Overall system parameters are along the top, " +
        "and shape-specific parameters are on the left side.  Click the 'Submit' button " +
        "when you are ready to submit your changes.");
        lab.setPrefWidth(instructionTextWidth);
        lab.setWrapText(true);
        super.m_MainVBox.getChildren().add(lab);

        Utils.addVerticalSpace(super.m_MainVBox, 20);*/

        infoLabel = new Label();
        infoLabel.setWrapText(true);
        infoLabel.setPrefWidth(instructionTextWidth);
        infoLabel.setStyle("-fx-font-weight: bold; ");
        infoLabel.textProperty().bind(Bindings.createStringBinding(() -> 
            /*"Task " + (Player.getCurrentTaskNumberTracker() + 1) +
            ", " + Player.getCurrentTaskNameTracker() +
            ": " + Player.getCurrentTaskTextTracker(),
            Player.currentTaskNumberTrackerProperty(),
            Player.currentTaskNameTrackerProperty(),
            Player.currentTaskTextTrackerProperty()*/
            Player.getCurrentTaskTextTracker(),
            Player.currentTaskTextTrackerProperty()
        ));
        super.m_MainVBox.getChildren().add(infoLabel);

        //Utils.addVerticalSpace(super.m_MainVBox, 60);
        Utils.addVerticalSpace(super.m_MainVBox, 5);
    }

    public void submit() {
        super.submit(Data.currentValues);
    }
    
    @Override
    public void onChanged(Node node) { 
        m_ParentPanelSet.onValueChanged(node);
    }
}