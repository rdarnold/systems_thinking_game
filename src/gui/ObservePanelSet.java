
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
/*import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.application.Platform;

import gos.*;

public class ObservePanelSet extends PanelSetBase {
    // Keep track of a snapshot list so we can move between turns.
    
    // For observing
    ObservePanelBottom panelBottom;
    ObservePanelLeft panelLeft;
    ObservePanelTop panelTop;
    
    public static OKWindow observePanelWindow;

    public ObservePanelSet(MainScreen main) {
        super(main);

        panelLeft = new ObservePanelLeft(this);
        panelTop = new ObservePanelTop(this);
        panelBottom = new ObservePanelBottom(this);
        
        addPanels(panelLeft, panelTop, panelBottom);

        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        String strTitle = "Observe the System";
        String strText = 
        "You'll start with a tutorial just to get to know how the game " +
        "works." +
        "\r\n\r\n" +
        "You start the tutorial on the Observe Screen. " +
        "On this screen, you can watch the system evolve over time without " +
        "using up any turns. " +
        "You can observe the system in action using the controls " +
        "at the bottom of the screen. " +
        "\r\n\r\n" +
        "The physics of this system are identical to the system you will be working with in the game." +
        "\r\n\r\n" +
        "Note that when you observe a turn several times over, you may not always see the same " +
        "exact result.  Elements of randomness in the game can cause differences in outcomes." +
         "\r\n\r\nThe 'Play All' button is a good place to start!";
        observePanelWindow = new OKWindow(400, 600, strTitle, strText);

        // And set the helper text for the info area
        panelTop.getInfoArea().setHelpText(strText);
    }

    public void forceShowOKWindow() {
        observePanelWindow.showAndWait(true);
    }

    @Override 
    public void show() {
        super.show();

        // Also put the right frame back in place
        panelBottom.restoreLastObservationFrame();
        //observePanelWindow.showAndWait();
    }

    @Override 
    public void onFadeInFinished() {
        // Do this when the fadein is done actually, after the show is called.
        //observePanelWindow.showAndWait();
        Platform.runLater(() -> {
            // We have to use a runLater to avoid this playing during "layout processing"
            // or whatever bs, even though it's called by the onFinished event of the 
            // fadein.  It's like, if onFinished doesn't mean finished, I dont know wtf does.
            observePanelWindow.showAndWait();
        });
    }

    @Override
    public void updateOneFrame(boolean running, Turn currentTurn) {
        if (panelBottom != null) {
            panelBottom.updateOneFrame(running, currentTurn);
        }
    }

    @Override
    public void onEndTurn() {
        panelBottom.onEndTurn();
    }

    @Override
    public void onStartTurn() {
        panelBottom.onStartTurn();
    }

    @Override
    public void onPause() {
        panelBottom.onPause();
    }

    @Override 
    public void update() {
        super.update();

        // Change the Help window text when we leave practice mode
        if (Player.inPracticeMode() == false) {
            String strText = 
            "On this screen, you can observe the system in action using the controls " +
            "at the bottom of the screen." +
            "\r\n\r\nAt first, you may only observe the system for " + Player.getObservation().getMaxTurns() + 
            " turns.  You may increase the number " +
            "of turns by using Discovery Points.  Just remember that you only have a limited number of " +
            "Discovery Points, and you may want to save some to create experiments in the Experiment screen, or " +
            "to discover other information about the system through the Ask screen.\r\n\r\n" +
            "Note that when you observe a turn several times over, you may not always see the same " +
            "exact result.  Elements of randomness in the game can cause differences in outcomes.";
            observePanelWindow.setText(strText);
        }
    }
}