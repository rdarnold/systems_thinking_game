
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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.binding.Bindings;

import gos.*;

public class MainPanelLeft extends PanelLeftBase {
    
    MainPanelSet m_ParentPanelSet;

    ArrayList<MovableButton> buttonList = null;
    
    private MainPanelLeft thisScreen;
    private MovableButton observeBtn;
    private MovableButton askBtn;
    private MovableButton expBtn;

    public MainPanelLeft(MainPanelSet panelSet) {
        super();
        thisScreen = this;
        m_ParentPanelSet = panelSet;
        buttonList = new ArrayList<MovableButton>();
        create();
    }

    private void create() {
        MovableButton btn;
        String str;

        // Instead of this, I should really set it up so that I can have stuff
        // in the top, on the bottom, then in the middle for a more consistent
        // user experience.  It should be like,
        // - Name of screen (better yet - this goes at the top of the screen)
        // - Help button
        // - Discovery points
        // - Any other points / turns
        //  < various buttons >
        // - Main Screen
        /*Utils.addVerticalSpace(this, 60);

        discoveryPointText = new Label();
        //discoveryPointText.setPadding(new Insets(5));
        getChildren().add(discoveryPointText);
        discoveryPointText.textProperty().bind(Player.discoveryPointsProperty().asString(
            "Discovery Points: %d"
        ));*/

        //Utils.addVerticalSpace(this, 60);
 
        observeBtn = new MovableButton("Observe");
        btn = observeBtn;
        str = "Observe the default system in action.  Unlike experiments, "+
        "you can view the system operating over multiple turns.  However, you cannot make any changes.";
        Utils.addToolTip(btn, str);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                // Unpause it if we were paused
                Gos.sim.unpause();
                Gos.mainScene.showObservePanelSet();
            }
        });
        buttonList.add(btn);
        getChildren().add(btn);

        //These tooltips should just be part of the help menu.
        askBtn = new MovableButton("Ask");
        btn = askBtn;
        str = "Ask questions about the nature of the scenario and its elements.  This " +
        "is an alternative way to discover information about the system, rather than observing or " +
        "experimenting.";
        Utils.addToolTip(btn, str);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                // Unpause it if we were paused
                Gos.sim.unpause();
                Gos.mainScene.showInquirePanelSet();
                // Do a test for now - TODO REMOVE THIS
                //Gos.sim.frameTest();
            }
        });
        buttonList.add(btn);
        getChildren().add(btn);

        expBtn = new MovableButton("Experiment");
        btn = expBtn;
        str = "Design and carry out experiments on the scenario.  These experiments " +
        "allow you to gain knowledge about the system.  Conducting an experiment will not move the exercise " +
        "to the next turn.";
        Utils.addToolTip(btn, str);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                // Unpause it if we were paused
                Gos.sim.unpause();
                Gos.mainScene.showExperimentPanelSet();
                // Do a test for now - TODO REMOVE THIS
                //Gos.sim.frameTestClear();
            }
        });
        buttonList.add(btn);
        getChildren().add(btn);
    }

    public void enableDefaultButtons() {
        if (buttonList == null) {
            return;
        }
        for (MovableButton btn : buttonList) {
            btn.setDisable(false);
        }
    }

    public void disableDefaultButtons() {
        if (buttonList == null) {
            return;
        }
        for (MovableButton btn : buttonList) {
            btn.setDisable(true);
        }
    }

    public void reset() {
        update();
    }

    public void update() {
        if (Player.inPracticeMode() == true) {
            askBtn.setVisible(false);
            expBtn.setVisible(false);
        }
        else {
            askBtn.setVisible(true);
            expBtn.setVisible(true);
        }
        /*if (discoveryPointText != null) {
            discoveryPointText.setText("Discovery Points: " + Player.getDiscoveryPoints());
        }
        if (maxTurnText != null) {
            maxTurnText.setText("Observation Turns: " + Player.maxObservationTurns);
        }*/
    }
}