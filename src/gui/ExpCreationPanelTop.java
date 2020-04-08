
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
import javafx.beans.binding.Bindings;

import gos.*;

public class ExpCreationPanelTop extends AdjustOverallValuesPanel {

    ExpCreationPanelSet m_ParentPanelSet;
    
    private ExpCreationPanelTop thisScreen;
    
    public ExpCreationPanelTop(ExpCreationPanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        getInfoArea().setName("Experiment Creation");

        Label lab = new Label("Use the sliders and drop-down selection boxes to " +
        "make changes to the system for this experiment.  Overall system parameters are along the top, " +
        "and shape-specific parameters are on the left side.");
        lab.setPrefWidth(instructionTextWidth);
        lab.setWrapText(true);
        super.m_MainVBox.getChildren().add(lab);
        
        /*Line lin = new Line();
        lin.setStartX(5);
        lin.setEndX(300);
        lin.setStartY(50);
        lin.setEndY(50);
        lin.setStrokeWidth(1);
        super.m_MainVBox.getChildren().add(lin);*/

        Utils.addVerticalSpace(super.m_MainVBox, 10);
    }

    public void submit(Experiment exp) {
        super.submit(exp.getSnap().getValues());
    }
    
    @Override
    public void onChanged(Node node) { 
        m_ParentPanelSet.onValueChanged(node);
    }
}