package gos.gui;
import java.util.ArrayList;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Tooltip;
import javafx.beans.binding.Bindings;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import gos.*;

public class ScreenInfoArea extends VBox implements ClassInfo {

    private Label lblName;
    private Label lblScenario;
    private Label lblDiscoveryPoints;
    private String m_strHelpText = "";
    private String m_strCondensedName = "";

    private ArrayList<MovableButton> buttonList;

    private OKWindow m_HelpWindow;
    private OKWindow m_ScoreWindow;

    public ScreenInfoArea(String strName) {
        super();
        buttonList = new ArrayList<MovableButton>();

        //#F0F8FF - alice blue, lighter
        setStyle("-fx-background-color: #C1CDCD; " + 
                 "-fx-border-color: black; " +
                 "-fx-border-radius: 3; " +
                 "-fx-border-width: 2;");
 
        setPrefWidth(220);       
        setPrefHeight(220);          
        //setMinWidth(200);       
        //setMinHeight(200);              
        setAlignment(Pos.CENTER);
        //setPadding(new Insets(5));
        setSpacing(5);

        /*Label label;
        label = new Label("Player ID: " + Player.getId());
        label.setAlignment(Pos.CENTER_LEFT);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setPrefWidth(180);
        //lblName.setFont(new Font(20));
        label.setStyle("-fx-font-size: 12px;");
        getChildren().add(label);*/

        /*TextField copyable = new TextField("ID: " + Player.getId());
        copyable.setEditable(false);
        copyable.getStyleClass().add("copyable-label");
        copyable.setPadding(new Insets(0));
        getChildren().add(copyable);
        Utils.addToolTip(copyable, "Please use this ID if you restart or replay the game.");*/


        lblName = new Label(strName);
        lblName.setAlignment(Pos.CENTER);
        lblName.setTextAlignment(TextAlignment.CENTER);
        //lblName.setFont(new Font(20));
        lblName.setStyle("-fx-font-weight: bold; "+ 
                         "-fx-font-size: 20px;");
        getChildren().add(lblName);

        lblScenario = new Label();
        lblScenario.setAlignment(Pos.CENTER);
        lblScenario.setTextAlignment(TextAlignment.CENTER);
        //label.textProperty().bind(Bindings.createStringBinding(() -> 
          //  available.income.get() + " Income for " + now.getValue().getMonth(),
            //available.income, now));
        /*lblScenario.textProperty().bind(Bindings.createStringBinding(() -> 
            "Scenario " + Player.getCurrentExerciseDisplayIdTracker() +
            "." + 
            (Player.getCurrentTaskNumberTracker() + 1) +
            ": " + Player.getCurrentExerciseNameTracker(),
            Player.currentExerciseDisplayIdTrackerProperty(), 
            Player.currentTaskNumberTrackerProperty(),
            Player.currentExerciseDisplayIdTrackerProperty(),
            Player.currentExerciseNameTrackerProperty()
        ));*/
        lblScenario.textProperty().bind(Bindings.createStringBinding(() -> 
            "Scenario " + Player.getCurrentExerciseDisplayIdTracker() +
            ": " + Player.getCurrentExerciseNameTracker(),
            Player.currentExerciseDisplayIdTrackerProperty(), 
            Player.currentExerciseDisplayIdTrackerProperty(),
            Player.currentExerciseNameTrackerProperty()
        ));
        getChildren().add(lblScenario);
        
        lblDiscoveryPoints = new Label();
        /*lblDiscoveryPoints.setAlignment(Pos.CENTER);
        lblDiscoveryPoints.setTextAlignment(TextAlignment.CENTER);
        lblDiscoveryPoints.textProperty().bind(Player.discoveryPointsProperty().asString(
            "Discovery Points: %d"
        ));
        getChildren().add(lblDiscoveryPoints);*/


        VBox buttonVBox = new VBox();
        buttonVBox.setSpacing(2);
        buttonVBox.setAlignment(Pos.CENTER);
        getChildren().add(buttonVBox);

        // So we will create an HBox so we can put these buttons next to each other
        // and save some space.
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(2);
        buttonBox.setAlignment(Pos.CENTER);
        buttonVBox.getChildren().add(buttonBox);
        
        HBox buttonBox2 = new HBox();
        buttonBox2.setSpacing(2);
        buttonBox2.setAlignment(Pos.CENTER);
        buttonVBox.getChildren().add(buttonBox2);

        MovableButton btn = null;

        btn = new MovableButton("Help");
        btn.setPrefWidth(60);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, className() + " " + m_strCondensedName);
                onClickHelp();
            }
        });
        buttonList.add(btn);
        buttonBox.getChildren().add(btn);

        btn = new MovableButton("Score");
        Utils.addToolTip(btn, "Shows your scores for each stage.");
        btn.setPrefWidth(60);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, className() + " " + m_strCondensedName);
                onClickScore();
            }
        });
        buttonList.add(btn);
        buttonBox.getChildren().add(btn);

        btn = new MovableButton("Info");
        Utils.addToolTip(btn, "Access some text information about the system.");
        btn.setPrefWidth(60);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, className() + " " + m_strCondensedName);
                onClickInfo();
            }
        });
        buttonList.add(btn);
        buttonBox.getChildren().add(btn);

        btn = new MovableButton("Notes");
        Utils.addToolTip(btn, "Notes is an area for you to record notes and observations about the system.  You can write " +
          "anything in it, and it will stick around for the entire game.");
        btn.setPrefWidth(184);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, className() + " " + m_strCondensedName);
                onClickScratchPad();
            }
        });
        buttonList.add(btn);
        buttonBox2.getChildren().add(btn);

        m_HelpWindow = new OKWindow(400, 500, "", "");
        m_ScoreWindow = new OKWindow(400, 500, "Score Record", "No scores yet.");
        m_ScoreWindow.centerText();
    }

    public void setName(String strName) {
        lblName.setText(strName);
        String s1 = strName.replaceAll("[\r\n]", "");
        m_strCondensedName = s1.replaceAll(" ", "");
    }

    public void setHelpText(String strText) {
        m_strHelpText = strText;

        // Should maybe have a helper.xml file where all the helper text is,
        // the stuff that isn't in the info area
        String strTitle = "Help: " + lblName.getText();
        m_HelpWindow.setup(strTitle, m_strHelpText);
    }

    protected void onClickInfo() {
        Gos.sim.snapCurrent();
        Gos.gos.showInfoScreen();
    }

    protected void onClickHelp() {
        // Pop up a window with help text.
        m_HelpWindow.showAndWait(true);
    }

    protected void onClickScratchPad() {
        // Pop up the scratch pad window
        Gos.gos.showScratchPad();
    }

    // Establish a string that lists all of our
    // scores for each scenario and task
    public String printScoreString() {
        String str = "";
        // This num allows us to put an extra carriage return between
        // scores from different scenarios.
        int num = 0;
        if (Data.scores.size() > 0) {
            num = Data.scores.get(0).getExId();
        }
        for (Score score : Data.scores) {
            if (score.getExId() > num) {
                num++;
                str += "\r\n";
            }
            str += score.getExTaskString();
            str += ": ";
            str += score.getScoreString();
            str += "\r\n";
        }

        return str;
    }

    protected void onClickScore() {
        String str = printScoreString();
        
        // Update the label
        if (str.equals("") == false) {
            Label lab = m_ScoreWindow.getTextLabel();
            lab.setText(str);
        }

        m_ScoreWindow.showAndWait(true);
    }

    public void enable() {
        if (buttonList == null) {
            return;
        }
        for (MovableButton btn : buttonList) {
            btn.setDisable(false);
        }
    }

    public void disable() {
        if (buttonList == null) {
            return;
        }
        for (MovableButton btn : buttonList) {
            btn.setDisable(true);
        }
    }

    public void update() {
        if (Player.inPracticeMode() == true) {
            lblDiscoveryPoints.setVisible(false);
        }
        else {
            lblDiscoveryPoints.setVisible(true);
        }
    }
}