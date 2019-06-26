
package gos;

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
import javafx.stage.Stage;
import javafx.stage.Modality;
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
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import gos.gui.*;

// Gos - Game of Systems
//public class Gos extends Application implements SimulatorEventListener {
public class Gos extends Application {
    int wid = 1000;
    int hgt = 800;

    ////////////////////////////////////////////
    ///// Testing and debugging variables //////
    ////////////////////////////////////////////
    // All should be false for deployment //////
    ////////////////////////////////////////////
    public static boolean testing = false;
    public static boolean skipSurveys = false;
    public static boolean showSkipWindow = true;
    public static boolean assessmentMode = true;
    ////////////////////////////////////////////
    ////////////////////////////////////////////
    ////////////////////////////////////////////

    public static Stage stage;

    // Some statics that people can access from wherever so we don't have to
    // pass them around.
    public static Gos gos;
    public static Simulator sim;
    public static SimRunner simRunner;

    public static GosSceneBase currentScene;
    public static MainScreen mainScene;
    public static AnswerScreen answerScene;
    public static InfoScreen infoScene;
    public static InquireScreen inquireScene;
    public static WelcomeWindow welcomeWindow;
    public static PlayedBeforeWindow playedBeforeWindow;
    public static ContactConsentWindow contactConsentWindow;
    public static StartSurveyWindow startSurveyWindow;
    public static ExercisePopupWindow exercisePopupWindow;
    public static StartSimulationWindow startSimulationWindow;
    public static StartRealGameWindow startRealGameWindow;
    public static BetaWindow betaWindow;
    public static VariableWindow variableWindow;
    public static OKWindow noDiscoveryPointsWindow;
    public static ScratchPadWindow scratchPadWindow;
    public static SkipToWindow skipToWindow;
    public static AssessmentWindow assessmentWindow;

    @Override
    public void start(Stage primaryStage) {
        gos = this;

        // Initialize some static classes up front
        Utils.init();
        Player.init();

        // We do this first to load all our XML data and have it ready.
        Data.load();

        // Randomly assign an ID
        Player.generateId(); 

        if (Player.getCurrentExercise() == null) {
            Utils.log("Error, no exercises found");
        }

        mainScene = new MainScreen(this, new BorderPane(), wid, hgt);
        answerScene = new AnswerScreen(this, new BorderPane(), wid, hgt);
        infoScene = new InfoScreen(this, new BorderPane(), wid, hgt);
        //inquireScene = new InquireScreen(this, new BorderPane(), wid, hgt);
        //createExperimentScreen = new CreateExperimentScreen(this, new BorderPane(), wid, hgt);

        welcomeWindow = new WelcomeWindow(500, 350);
        contactConsentWindow = new ContactConsentWindow(500, 450);
        playedBeforeWindow = new PlayedBeforeWindow(500, 350);
        startSurveyWindow = new StartSurveyWindow(500, 350);
        betaWindow = new BetaWindow(400, 530);
        exercisePopupWindow = new ExercisePopupWindow(400, 400);
        startSimulationWindow = new StartSimulationWindow(600, 500);
        startRealGameWindow = new StartRealGameWindow(600, 700);
        variableWindow = new VariableWindow(400, 650);
        scratchPadWindow = new ScratchPadWindow(600, 600);
        skipToWindow = new SkipToWindow(400, 600);
        assessmentWindow = new AssessmentWindow(600, 600);

        String str = "Sorry, you don't have enough Discovery Points to do that.  You will " +
        "get another one next turn.";
        noDiscoveryPointsWindow = new OKWindow(250, 250, "Out of Discovery Points", str);
        noDiscoveryPointsWindow.centerText();

        // Simulator should be created last because it will send updates
        // to the various parts of the screen as it creates stuff.
        sim = new Simulator(mainScene); // Pass it a scene to hook up the event listener.
        simRunner = new SimRunner(sim);

        // Now set the sim properly
        mainScene.setSim(sim, simRunner);
        answerScene.setSim(sim, simRunner);
        infoScene.setSim(sim, simRunner);

        // Actually we are only going to do the event listener on the main scene.
        // none of the other scenes need to know about events from the system right now.
        //sim.setSimulatorEventListener(mainScene);
        sim.addSimulatorEventListener(mainScene);
        for (SimulatorEventListener listener : mainScene.getPanelSetList()) {
            sim.addSimulatorEventListener(listener);
        }

        // Add the runner last because it has the ability to start a new turn when
        // its end turn is triggered, so we want all the other screens and listeners
        // to have had a chance to process their end turns before we launch a new
        // one.
        sim.addSimulatorEventListener(simRunner);

        stage = primaryStage;
        stage.setTitle("The Systems Thinking Game v" + Constants.VERSION_NUMBER_STRING);

        showMainScreen();

        // So, how do we do this.  Well, the stage width and height aren't calculated
        // until after it is shown, so if we try to center it before we show it, it won't
        // work.  But if we try to center it after, it'll visibly move.  This aspect of
        // JavaFX seems to suffer from poor design.  so the way we get around it is this weird
        // little hack.  basically, the width is always the width we pass to the scene plus
        // 16.  The height is what we pass in for the scene plus 38.  So we just add that
        // to our original values and those are the actuals.  Feels squirrely but it seems
        // to be the way to get this to work right.
        int nStageWid = wid + 16;
        int nStageHgt = hgt + 38;

        // This centers the stage on the screen when it is shown but we still can't
        // get its x, y and width and height until it is shown, even though we've seemingly
        // set it here.
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() / 2) - (nStageWid / 2));
        stage.setY((primScreenBounds.getHeight() / 2) - (nStageHgt / 2));

        // Don't actually show the stage yet since we have some
        // initial welcome windows to show.  Although how does this
        // hold up with the demographic surveys?  It might not.
        //  Indeed it does not.
        //stage.show();
        
        // These were just for testing.
        //FileTransfer.runUploadThread();
        //betaWindow.showAndWait();

        Player.startTime = System.currentTimeMillis();
        welcomeWindow.showAndWait();
        
        if (assessmentMode == true) {
            showAssessmentWindow();
        }

        // Now let them skip to whereever they want
        if (showSkipWindow == true) {
            skipToWindow.showAndWait();
            if (Player.getSkipped() == true) { 
                simRunner.startTest();
                return;
            }
        }

        // They will have indicated on the prior screen if they played before or not
        if (Player.getPlayedBefore() == true) {
            playedBeforeWindow.showAndWait();
        }
        contactConsentWindow.showAndWait();
        startSurveyWindow.showAndWait();

        simRunner.startTest();
    }

    public static void main(String[] args) {
        Constants.init();
        Application.launch(args);
    }

    public static void changeScene(GosSceneBase newScene) {
        currentScene = newScene;
        //sim.setSimulatorEventListener(currentScene);
        stage.setScene(currentScene);
    }

    public static void showMainScreen() {
        changeScene(mainScene);
    }

    public static void showAnswerScreen() {
        answerScene.rebuild();
        changeScene(answerScene);
    }

    public static void showInfoScreen() {
        changeScene(infoScene);
    }

    public static void showInquireScreen() {
        changeScene(inquireScene);
    }

    public static void showExercisePopup(Exercise exer) {
        if (exer.getPopupText() != "") {
            exercisePopupWindow.showAndWait(exer);
        }
    }

    public static void showStartSimulation() {
        startSimulationWindow.showAndWait();
    }

    public static void showStartRealGame() {
        startRealGameWindow.showAndWait();
    }

    public static void showTaskWindow() {
        OKWindow wind = new OKWindow(250, 250, 
            "New Task: " + Player.getCurrentTaskNameTracker() + "!", 
            Player.getCurrentTaskTextTracker());
        wind.showAndWait(true);
    }

    public static void showScratchPad() {
        scratchPadWindow.show();
    }

    public static void showAssessmentWindow() {
        assessmentWindow.show();
    }

    public static boolean checkDiscoveryPoints() {
        if (Player.getDiscoveryPoints() <= 0) {
            noDiscoveryPointsWindow.showAndWait(true);
            return false;
        }
        return true;
    }

    /*public void showExperimentScreen() {
        createExperimentScreen.rebuild();
        changeScene(createExperimentScreen);
    }*/

    public static void completeTask() {
        simRunner.finishTask();
        changeScene(mainScene);
        //mainScene.updateTaskText();
        //mainScene.updateExerciseText();
    }

    /*public void completeQuestion() {
        simRunner.finishQuestion();
    }*/

    public static void selectShape(SysShape shape) {
        Player.setSelectedShape(shape);
        Gos.mainScene.onSelectedShapeUpdated();
    }

    public void onClickShape(SysShape newShape) {
        if (Player.getSelectedShape() != null && sim.noChangeSelection() == true)
            return;
        selectShape(newShape);
    }

    // Is the system in a state that allows the player to make changes?
    public static boolean playerCanChangeSystem() {
        if (mainScene.isShowingChangePanelSet() == false &&
            mainScene.isShowingExpCreationPanelSet() == false) {
            return false;
        }
        return true;
    }

    // This is not being used but I'm leaving it here in case I need it
    public void showModalWindow() {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.WINDOW_MODAL); 
        dialog.showAndWait();
    }
}