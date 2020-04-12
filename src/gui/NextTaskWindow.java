
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
import javafx.stage.Stage;
import javafx.stage.Screen; 
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
    
import gos.*;

public class NextTaskWindow extends DialogWindow {

    MovableButton proceedBtn;
    
    private NextTaskWindow thisScreen;

    public NextTaskWindow(int wid, int hgt) {
        super(wid, hgt);
        thisScreen = this;
    }

    // Won of true means we won the turn, false means we lost so we'd want to
    // display something a little different
    private void build(boolean won) {
        removeTempControls();

        int wid = m_nWidth;
        int hgt = m_nHeight;
        int space = 10;
        int labelWid = wid - 20;

        MovableButton btn;
        String str = "";
        VBox box = m_MainVBox;

        // Just clear the entire thing off before we do anything further.
        box.getChildren().clear();

        Task task = Player.getCurrentTask();
        if (task == null) {
            close();
            return;
        }

        //str = "Congratulations, you have completed " + task.toStringTitle() + "!";
        // Maybe I shouldn't be so enthusiastic - what if they sucked and lost all their shapes.
        if (won == true) {
            str = "You have completed " + task.toStringTitle() + ".";
        }
        else {
            str = "All of your shapes have been destroyed so you cannot continue. " + task.toStringTitle() + " is over.";
        }
        addCenteredLabel(str);
        Utils.addVerticalSpace(box, space);

        //str = "Here's how you did: ";
        //addCenteredLabel(str);
        //Utils.addVerticalSpace(box, space);

        Score score = new Score();
        // We assign 50 the second we hit it, even if the number "busts"
        // 50, we don't really care
        if (won == true) {
            score.get(Gos.sim, 50);
        }
        else {
            score.get(Gos.sim);
        }
        Data.scores.add(score);
        str = score.toString();
        addCenteredLabel(str);
        Utils.addVerticalSpace(box, space);

        task = Player.getNextTask();
        if (task != null) {
            str = "Time for the next task.";
            addCenteredLabel(str);
            Utils.addVerticalSpace(box, space);

            str = task.toString();
            addCenteredLabel(str);
            Utils.addVerticalSpace(box, space);
        } else {
            Exercise e = Player.getCurrentExercise();
            // If we are further than the tutorial, ask our questions
            if (e.getId() > 2) {
                str = //"You've completed the entire " + e.getName() + " scenario! " +
                "You'll now be given a set of " +
                "questions that relate to the scenario.";
                addCenteredLabel(str);
            }
            Utils.addVerticalSpace(box, space);
        }

        proceedBtn = new MovableButton("OK");
        btn = proceedBtn;
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Player.recordButtonAction(event, thisScreen.className());
                Gos.simRunner.finishTask();
                close();
            }
        });
        addTempControl(btn);
    }
    
    @Override
    public void showAndWait() {
        if (isShowing() == true) {
            return;
        }
        build(true);
        super.showAndWait();
    }

    public void showAndWait(boolean won) {
        if (isShowing() == true) {
            return;
        }
        build(won);
        super.showAndWait();
    }
}