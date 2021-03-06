
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

public class MainPanelSet extends PanelSetBase {

    MainPanelBottom panelBottom;
    MainPanelLeft panelLeft;
    MainPanelRight panelRight;
    MainPanelTop panelTop;
    
    public static OKWindow mainPanelWindow;
    public static OKWindow completeWindow;

    public MainPanelSet(MainScreen main) {
        super(main);

        panelLeft = new MainPanelLeft(this);
        panelRight = new MainPanelRight(this);
        panelTop = new MainPanelTop(this);
        panelBottom = new MainPanelBottom(this);
        
        addPanels(panelLeft, panelRight, panelTop, panelBottom);

        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        String strTitle = "Main Screen";
        String strText = 
        // Or should this explain about the scenarios vs. the tasks, etc.?  I 
        // should explain that somewhere.
        "This is the main screen from which you can navigate to other screens " + 
        "to gather information about the system, or proceed with the game by " + 
        "clicking the 'Change System' button.  The 'Info' button at the top left shows a list of " + 
        "all different types of information about the game.  the 'Notepad' button opens a " +
        "notepad window in which you can write anything you want throughout the game.\r\n\r\n" + 
        "Your goal for this task is shown in bold on the top panel of the screen.";
        mainPanelWindow = new OKWindow(400, 500, strTitle, strText);

        // And set the helper text for the info area
        panelTop.getInfoArea().setHelpText(strText);

        // And the "task complete" window
        /*strTitle = "Task Complete";
        strText = 
        // Or should this explain about the scenarios vs. the tasks, etc.?  I 
        // should explain that somewhere.
        "Congratulations, you've reached the goal of 50 shapes!";
        completeWindow = new OKWindow(400, 200, strTitle, strText);*/
    }

    public void forceShowOKWindow() {
        mainPanelWindow.showAndWait(true);
    }
  
    @Override 
    public void show() {
        super.show();
        mainPanelWindow.showAndWait();
    }

    @Override
    public void onNumberOfShapesChanged(int numberLiveShapes) { 
        // In some tasks, reaching a certain number of shapes is our
        // goal.  So we have to be paying attention to the current
        // number of shapes.

        // But we have to be on the right scenario, the right task, 
        // and we have to be in the right mode - not observing or 
        // experimenting.
        if (isShown() == false) {
            return;
        }

        // Do some different things depending on what scenario and task we're in
        // Or should we do this at the end of the turn instead of immediately when the
        // shapes are removed or added?
        switch (Player.getCurrentExerciseNumberTracker()) {
            case 3:
                if (numberLiveShapes >= 50) {
                    // Win
                    Gos.mainScene.forceCompleteTask(true);
                }

                // We don't want to check living shapes to see if a player loses,
                // because we can have situations where we clear the sim, and are rebuilding
                // it from a snapshot, and haven't fully rebuilt it yet but earlier shapes
                // are actually dead and don't register as living even though we have
                // a lot of shapes in the system
                // So, we use total shapes instead
                if (Gos.sim.getNumberTotalShapes() <= 0) {
                    // Lose
                    Gos.mainScene.forceCompleteTask(false);
                }
                break;
        }
    }
    
    @Override
    public void updateOneFrame(boolean running, Turn currentTurn) {
        if (panelBottom != null) {
            panelBottom.updateOneFrame(running, currentTurn);
        }
    }

    public void enableDefaultButtons() {
        panelLeft.enableDefaultButtons();
        panelTop.enableDefaultButtons();
        panelBottom.enableDefaultButtons();
    }

    public void disableDefaultButtons() {
        panelLeft.disableDefaultButtons();
        panelTop.disableDefaultButtons();
        panelBottom.disableDefaultButtons();
    }
}