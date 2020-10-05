
package gos.analyzer;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;
import java.time.ZoneOffset;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import gos.*;
import gos.gui.*;

// Basically a holder and calculator of static data that contains the data pertaining to the data analyzer and player assessment.
public final class AssessmentData {
    
    private AssessmentData () { // private constructor
    }

    // This is a list of all the submit actions that the player took, so from them,
    // we can look at the system state at any point in time during play for any of our
    // data points and for trends.
    public static ArrayList<Action> submitActions = new ArrayList<Action>();

    // The various time slices
    public static GameTimeSlice demographicSlice = new GameTimeSlice();
    public static ArrayList<GameTimeSlice> tutorialSlices = new ArrayList<GameTimeSlice>();
    public static ArrayList<GameTimeSlice> fourShapesSlices = new ArrayList<GameTimeSlice>();
    public static ArrayList<GameTimeSlice> fourShapesReplaySlices = new ArrayList<GameTimeSlice>();
    public static ArrayList<GameTimeSlice> chaosSlices = new ArrayList<GameTimeSlice>();
    public static ArrayList<GameTimeSlice> chaosReplaySlices = new ArrayList<GameTimeSlice>();
    public static GameTimeSlice selfAssessSlice = new GameTimeSlice();

    public static ArrayList<GameTimeSlice> observeSlices = new ArrayList<GameTimeSlice>();
    public static ArrayList<GameTimeSlice> expSlices = new ArrayList<GameTimeSlice>();
    public static ArrayList<GameTimeSlice> playSlices = new ArrayList<GameTimeSlice>();
    public static ArrayList<GameTimeSlice> quesSlices = new ArrayList<GameTimeSlice>();

    public static void buildAllObserveSlices() {
        GameTimeSlice slice;
        //observeSlices
        // Keep in mind we START in observe mode so we need to start from the very beginning
        Action startAction = findNextButtonPress(0, "OK", "StartSimulationWindow");
        if (startAction == null) {
            return;
        }
        long start = startAction.timestamp;

        slice = new GameTimeSlice();
        slice.sliceType = GameTimeSlice.Type.Obs;
        slice.startTimeMS = startAction.timestamp;

        Action endAction = findNextButtonPress(0, "Main Screen", "ObservePanelBottom");
        if (endAction == null) {
            return;
        }
        slice.endTimeMS = endAction.timestamp;
        
        observeSlices.add(slice);

        start = slice.endTimeMS + 1;
        // Now just iterate through the rest of the game looking for observe button presses
        while (startAction != null) {
            startAction = findNextButtonPress(start, "Observe", "MainPanelLeft");
            endAction = findNextButtonPress(start, "Main Screen", "ObservePanelBottom");

            if (startAction == null || endAction == null) {
                break;
            }

            //if (startAction != null && endAction != null) {
            slice = new GameTimeSlice();
            slice.sliceType = GameTimeSlice.Type.Obs;
            slice.startTimeMS = startAction.timestamp;
            slice.endTimeMS = endAction.timestamp;
            observeSlices.add(slice);
            
            start = slice.endTimeMS + 1;
            //}
        }
    }

    public static void buildAllExpSlices() {
        long start = 0;
        GameTimeSlice slice;
        Action startAction = findNextButtonPress(0, "Experiment", "MainPanelLeft");;
        Action endAction;

        // Now just iterate through the rest of the game looking for observe button presses
        while (startAction != null) {
            startAction = findNextButtonPress(start, "Experiment", "MainPanelLeft");
            endAction = findNextButtonPress(start, "Main Screen", "ExperimentPanelBottom");

            if (startAction != null && endAction != null) {slice = new GameTimeSlice();
                slice.sliceType = GameTimeSlice.Type.Exp;
                slice.startTimeMS = startAction.timestamp;
                slice.endTimeMS = endAction.timestamp;
                expSlices.add(slice);
                
                start = slice.endTimeMS + 1;
            }
        }
    }

    public static void buildAllPlaySlices() {
        //playSlices
    }

    public static void buildAllQuesSlices() {
        //quesSlices
    }

    public static void buildTutorialTimeSlices() {
        GameTimeSlice.Segment seg = GameTimeSlice.Segment.Tutorial;
        // Exp, Obs, Play, Ques, Scen;
        // So, find when the tutorial began
        //Action firstAction = findNextButtonPress(0, "OK", "StartSimulationWindow");
        Action fa = findFirstActionForExercise(2, 0);  // ex 2 is Tutorial
        if (fa == null) {
            return;
        }
        long startTimeMS = fa.timestamp;

        //Action la = findNextButtonPress(0, "OK", "StartRealGameWindow");
        Action la = findFirstActionForExercise(3, 0);  // ex 3 is Four Shapes
        if (la == null) {
            return;
        }
        long endTimeMS = la.timestamp-1;

        // So we have the start and end time, so now we want to look at how much time
        // the person spent observing vs. playing vs. questions at the end

        // So what we could do is a generate "build segments" functions that essentially
        // build out all of your observe, experiment, play, question, etc., slices,
        // then we just pick the ones out that are inside this timeframe and shove them
        // in here, then organize by time.

        GameTimeSlice slice = new GameTimeSlice(seg);
        tutorialSlices.add(slice);
    }

    public static void buildFourShapesTimeSlices() {
        GameTimeSlice.Segment seg = GameTimeSlice.Segment.FourShapes;
        // Exp, Obs, Play, Ques, Scen;
        //Action firstAction = findNextButtonPress(0, "OK", "StartRealGameWindow");
        Action fa = findFirstActionForExercise(3, 0);  // ex 3.0 is Four Shapes
        if (fa == null)
            return;
        long startTimeMS = fa.timestamp;

        //Action lastAction = findNextButtonPress(startTimeMS, "OK", "StartRealGameWindow");
        Action la = findFirstActionForExercise(3, 1);  // ex 3.1 is four shapes round 2
        if (la == null)
            return;
        long endTimeMS = la.timestamp-1;

        GameTimeSlice slice = new GameTimeSlice(seg);
        fourShapesSlices.add(slice);
    }

    public static void buildFourShapesReplayTimeSlices() {
        GameTimeSlice.Segment seg = GameTimeSlice.Segment.FourShapesReplay;

        Action fa = findFirstActionForExercise(3, 1);  // four shapes r2
        if (fa == null)
            return;
        long startTimeMS = fa.timestamp;

        Action la = findFirstActionForExercise(4, 0);  // chaos
        if (la == null)
            return;
        long endTimeMS = la.timestamp-1;

        // Exp, Obs, Play, Ques, Scen;
        GameTimeSlice slice = new GameTimeSlice(seg);
        fourShapesReplaySlices.add(slice);
    }

    public static void buildChaosTimeSlices() {
        GameTimeSlice.Segment seg = GameTimeSlice.Segment.Chaos;

        Action fa = findFirstActionForExercise(4, 0);  // chaos
        if (fa == null)
            return;
        long startTimeMS = fa.timestamp;

        Action la = findFirstActionForExercise(4, 1);  // chaos r2
        if (la == null)
            return;
        long endTimeMS = la.timestamp-1;

        // Exp, Obs, Play, Ques, Scen;
        GameTimeSlice slice = new GameTimeSlice(seg);
        chaosSlices.add(slice);
    }

    public static void buildChaosReplayTimeSlices() {
        GameTimeSlice.Segment seg = GameTimeSlice.Segment.ChaosReplay;

        Action fa = findFirstActionForExercise(4, 1);  // chaos r2
        if (fa == null)
            return;
        long startTimeMS = fa.timestamp;

        Action la = findFirstActionForExercise(5, 0);  // assessment
        if (la == null)
            return;
        long endTimeMS = la.timestamp-1;

        // Exp, Obs, Play, Ques, Scen;
        GameTimeSlice slice = new GameTimeSlice(seg);
        chaosReplaySlices.add(slice);
    }

    public static void buildTimeSlices() {
        buildAllObserveSlices();
        buildAllExpSlices();
        buildAllPlaySlices();
        buildAllQuesSlices();

        // Build demographics slice
        demographicSlice.sliceType = GameTimeSlice.Type.Ques;
        demographicSlice.sliceSeg = GameTimeSlice.Segment.Demographic;

        // Build tutorial slices
        buildTutorialTimeSlices();

        // Build four shapes slices
        buildFourShapesTimeSlices();
        
        // Build four shapes replay slices
        buildFourShapesReplayTimeSlices();

        // Build chaos slices
        buildChaosTimeSlices();

        // Build chaos replay slices
        buildChaosReplayTimeSlices();

        // Build self-assess slice
        selfAssessSlice.sliceType = GameTimeSlice.Type.Ques;
        selfAssessSlice.sliceSeg = GameTimeSlice.Segment.SelfAssess;
    }

    public static int countButtonPresses(String strBtnName, String strScreenName) {
        int num = 0;
        // Go through action list for any corresponding actions and count them
        for (Action action : Player.actions) {
            if (action.actionType != Action.Type.Button)
                continue;
            if (action.fromScreen.equals(strScreenName) == false)
                continue;
            if (action.getDesc().equals(strBtnName) == false)
                continue;
            num++;
        }
        return num;
    }

    // ex is stage, task is round
    public static Action findFirstActionForExercise(int exnum, int tasknum) {
        // Go through action list and find the first action of the specified exercise/task
        for (Action action : Player.actions) {
            if (action.exNum == exnum && action.taskNum == tasknum)
                return action;
        }
        return null;
    }

    // These are all zero-based indeces
    public static Action findButtonActionForStageRoundTurn(int stage, int round, int turn, String strButtonName) {
        // Go through action list and find the associated SubmitChange action for a stage, it's the
        // one that was pressed ON that turn; so you're ALREADY ON turn 1, and then press submitchange
        for (Action action : Player.actions) {
            if (action.actionType != Action.Type.Button) {
                continue;
            }
            if (action.getDesc().equals(strButtonName) == false) {
                continue;
            }
            if (action.exNum == stage && action.taskNum == round && action.turnNum == turn) {
                return action;
            }
        }
        return null;
    }

    // These are all zero-based indeces
    public static int countSubmitExpChangeActionForStageRoundTurn(int stage, int round, int turn) {
        // Go through action list and find the associated SubmitChange action for a stage, it's the
        // one that was pressed ON that turn; so you're ALREADY ON turn 1, and then press submitchange
        int count = 0;
        for (Action action : Player.actions) {
            if (action.actionType != Action.Type.SubmitExpChange)
                continue;
            if (action.exNum == stage && action.taskNum == round && action.turnNum == turn)
                count++;
        }
        return count;
    }
    
    // These are all zero-based indeces
    public static Action findSubmitChangeActionForStageRoundTurn(int stage, int round, int turn) {
        // Go through action list and find the associated SubmitChange action for a stage, it's the
        // one that was pressed ON that turn; so you're ALREADY ON turn 1, and then press submitchange
        for (Action action : Player.actions) {
            if (action.actionType != Action.Type.SubmitChange)
                continue;
            if (action.exNum == stage && action.taskNum == round && action.turnNum == turn)
                return action;
        }
        return null;
    }

    // These are all zero-based indeces
    public static Action findTurnEndActionForStageRoundTurn(int stage, int round, int turn) {
        // Go through action list and find the associated EndTurn action 
        for (Action action : Player.actions) {
            if (action.actionType != Action.Type.TurnEnd)
                continue;
            if (action.exNum == stage && action.taskNum == round && action.turnNum == turn)
                return action;
        }
        return null;
    }

    // These are all zero-based indeces
    public static Action findSubmitVarRatingActionForStageRoundTurn(int stage, int round, int turn) {
        // Go through action list and find the associated EndTurn action 
        for (Action action : Player.actions) {
            if (action.actionType != Action.Type.SubmitVarRating)
                continue;
            if (action.exNum == stage && action.taskNum == round && action.turnNum == turn)
                return action;
        }
        return null;
    }


    public static Action findNextButtonPress(long fromTimeMS, String strBtnName, String strScreenName) {
        return findNextButtonPress(fromTimeMS, 0, strBtnName, strScreenName);    
    }

    // If maxTimeMS is 0 then it means we have no max, we look through all the actions
    public static Action findNextButtonPress(long fromTimeMS, long maxTimeMS, String strBtnName, String strScreenName) {
        // Go through action list and find the next action of the specified button
        for (Action action : Player.actions) {
            if (action.actionType != Action.Type.Button)
                continue;
            if (action.fromScreen.equals(strScreenName) == false)
                continue;
            if (action.getDesc().equals(strBtnName) == false)
                continue;
            if (action.timestamp <= fromTimeMS)
                continue;
            if ((maxTimeMS > 0) && (action.timestamp > maxTimeMS))
                continue;
            return action;
        }
        return null;
    }

    // I could make this automatically tell what the next_ex is in the future
    public static int calcTimeForTask(int ex, int tnum, int next_ex, int next_tnum) {
        Action fa = findFirstActionForExercise(ex, tnum); 
        if (fa == null) {
            return 0;
        }
        long startTimeMS = fa.timestamp;

        Action la = findFirstActionForExercise(next_ex, next_tnum);
        if (la == null) {
            return 0;
        }
        long endTimeMS = 0;
        if (la == null) {
            endTimeMS = Player.getEndTime();
        }
        else {
            endTimeMS = la.timestamp-1;
        } 
        return (int)(endTimeMS - startTimeMS);
    }

    public static int calcSelfAssessmentTime() {
        Action fa = findFirstActionForExercise(6, 0);  // self-assessment
        if (fa == null) {
            return 0;
        }
        return (int)(Player.getEndTime() - fa.timestamp);
    }

    public static int calcTotalTimeExp() {
        return calcTotalTimeExp(-1);
    }

    // scen is not using the normal scheme, it's an intuitive 0 for Tutorial, 1 & 2 for Four Shapes, and 3 & 4 for Chaos
    public static int calcTotalTimeExp(int scen) {
        // We'll calculate total experiment time by how much time a player spent from clicking the
        // Experiment button to then clicking the Main Screen button again. So we find a timestamp for
        // an Experiment press on main screen, and then find the next timestamp after that for a Main Screen press from
        // Experiment screen.

        // -1 as scenario means just calculate the total of all scenarios
        if (scen == -1) {
            long time = 0;
            for (GameTimeSlice slice : expSlices) {
                time += slice.getTotalTimeMS();
            }
            return (int)time;
            //return calcTotalTimeExp(1) + calcTotalTimeExp(2) + calcTotalTimeExp(3) + calcTotalTimeExp(4);
        }

        // So we need to time-block each scenario, so depending on which one we want, we'll find
        // the start and end time of that scenario, and then only grab button presses from within that time
        long startTimeMS = 0;
        long endTimeMS = 0;

        switch (scen) {
            case 0:
                // There is no experiment option in the Tutorial
                break;
            case 1:
                // So StartRealGameWindow we can clock the start of the game in general after the Tutorial
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }

        return 0;
    }
    
    public static int calcTotalTimeObs() {
        return calcTotalTimeObs(-1);
    }

    public static int calcTotalTimeObs(int scen) {
        // We'll calculate total observation time by how much time a player spent from clicking the
        // Observe button to then clicking the Main Screen button again.  So we find a timestamp for
        // an Observe press, and then find the next timestamp after that for a Main Screen press.
        // -1 as scenario means just calculate the total of all scenarios
        if (scen == -1) {
            long time = 0;
            for (GameTimeSlice slice : observeSlices) {
                time += slice.getTotalTimeMS();
            }
            return (int)time;
        }
        return 0;
    }

    public static int countNumNewExps() {
        return countButtonPresses("Create", "ExpCreationPanelBottom");
    }

    public static int countNumRerunExps() {
        return countButtonPresses("Re-Run Experiment", "ExperimentPanelBottom");
    }

    // It's both experiments and reruns
    public static String printNumExps() {
        // We'll calculate number of total experiments by saying how many times have we pressed 
        // the Create button on the Exp Creation screen.  Or we could just count the number of times
        // a SubmitExpChange event has occurred.
        // We have NEW experiments (Create New -> Create button)
        // And then RE-RUN experiments (Re-Run Experiment button)
        return ("" + countNumNewExps() + ", Re-Runs: " + countNumRerunExps());
    }

    public static String printTutorialDataString() {
        String ret = "";

        // Figure out how to display tutorial data
        String tdata = Player.getTutorialData();

        //Utils.log(tdata);

        if (tdata.indexOf("TURN 1: GOOD") != -1) { ret += "Y, "; } else { ret += "N, "; }
        if (tdata.indexOf("TURN 2: GOOD") != -1) { ret += "Y, "; } else { ret += "N, "; }
        if (tdata.indexOf("TURN 3: GOOD") != -1) { ret += "Y, "; } else { ret += "N, "; }
        if (tdata.indexOf("TURN 4: GOOD") != -1) { ret += "Y, "; } else { ret += "N, "; }
        if (tdata.indexOf("TURN 5: GOOD") != -1) { ret += "Y"; } else { ret += "N"; }

        return ret;
    }

    // The BEFORE values are what the 8 variables are before the player changes them,
    // so we can look at the differences
    public static String findBeforeValuesWithinString(String str) {
        int start = str.indexOf("BEFORE_VALUES");
        int end = str.substring(start).indexOf('\n') + start;
        return str.substring(start, end);
    }

    // The AFTER values are what the 8 variables are after the player changes them,
    // so we can look at how they changed
    public static String findAfterValuesWithinString(String str) {
        int start = str.indexOf("AFTER_VALUES");
        int end = str.substring(start).indexOf('\n') + start;
        return str.substring(start, end);
    }

    // The BEFORE values are what the 8 variables are before the player changes them,
    // so we can look at the differences
    public static String findBeforeShapeWithinString(String str) {
        int start = str.indexOf("BEFORE_SHAPE");
        int end = str.substring(start).indexOf('\n') + start;
        return str.substring(start, end);
    }

    // The AFTER values are what the 8 variables are after the player changes them,
    // so we can look at how they changed
    public static String findAfterShapeWithinString(String str) {
        int start = str.indexOf("AFTER_SHAPE");
        int end = str.substring(start).indexOf('\n') + start;
        return str.substring(start, end);
    }

    public static String findSnapshotWithinString(String str) {
        // Find !START_SNAPSHOT
        int start = str.indexOf("!START_SNAPSHOT");
        int end = str.indexOf("!END_SNAPSHOT");
        return str.substring(start, end);
    }

    public static Values findBeforeValuesForSubmitAction(Action action) {
        String valStr = findBeforeValuesWithinString(action.getDesc());
        Values vals = new Values(valStr);
        return vals;
    }

    public static Values findAfterValuesForSubmitAction(Action action) {
        String valStr = findAfterValuesWithinString(action.getDesc());
        Values vals = new Values(valStr);
        return vals;
    }

    public static SysShape findBeforeShapeForSubmitAction(Action action) {
        String shapeStr = findBeforeShapeWithinString(action.getDesc());
        SysShape shape = new SysShape(Gos.sim, shapeStr);
        return shape;
    }

    public static SysShape findAfterShapeForSubmitAction(Action action) {
        String shapeStr = findAfterShapeWithinString(action.getDesc());
        SysShape shape = new SysShape(Gos.sim, shapeStr);
        return shape;
    }

    // This assumes the action was a "Submit" action and thus contains the (extensive) strings we're
    // looking for
    public static SystemSnapshot createSnapshotFromSubmitAction(Action action, boolean before) {
        // We load it up from the existing string using a snapshot construct which
        // has the capability to load from string

        // First create a snapshot of current system (which we're going to overwrite)
        SystemSnapshot snap = new SystemSnapshot(Gos.sim);

        // Now find the values string to use, either before or after the action was taken
        if (before == true) {
            snap.setValuesTo(findBeforeValuesForSubmitAction(action));
        }
        else {
            snap.setValuesTo(findAfterValuesForSubmitAction(action));
        }

        // Now find the string to load...
        // It only needs the sim as a parameter so that it can set up the objects such that
        // they have a reference to the sim object.
        String snapStr = findSnapshotWithinString(action.getDesc()); 
        snap.setFromString(Gos.sim, snapStr);

        return snap;
    }

    public static String printScoreStringFromTurnEndAction(Action turnEnd) {
        String str = turnEnd.getDesc();

        // It looks like this:
        // TurnEnd 2:0:1 Tutorial, SCORE: There is no score for the tutorial.

        // Maybe for now just print out the full string.
        return str;
    }

    public static String printScoreForStageRoundTurn(int stageNum, int roundNum, int turnNum) {
        // Get the TurnEnd action which has the score string int
        Action action = findSubmitChangeActionForStageRoundTurn(stageNum, roundNum, turnNum);
        if (action == null) {
            return "";
        }

        // Now get the score string out of that.
        String score = printScoreStringFromTurnEndAction(action);
        return score;
    }

    // This function is zero-indeces based and is based on the stages in the game data.  So, 
    // demographic is stage 0, career survey is stage 1, tutorial is stage 2, four shapes is stage 3, etc.
    public static String printChangedVarsForStageRoundTurn(int stageNum, int roundNum, int turnNum) {
        String ret = "";

        // So what we want to do is, find the "Submit" action that corresponds to a specific
        // stage, round, and turn.
        Action submitAction = findSubmitChangeActionForStageRoundTurn(stageNum, roundNum, turnNum);
        if (submitAction == null) {
            return "";
        }

        // Now we can find the before and after vars for it.  We actually have a CHANGED VALUES
        // set, but we don't know what they changed FROM, so we need the before and after.
        Values beforeVals = findBeforeValuesForSubmitAction(submitAction);
        Values afterVals = findAfterValuesForSubmitAction(submitAction);
        if (beforeVals == null || afterVals == null) {
            return "";
        }

        // Now, to keep things simple we can literally just print both of them side by side to
        // see if anything changed visually.
        if (beforeVals.rainRate.equals(afterVals.rainRate) == false) {
            ret += "Rain Rate: " + beforeVals.rainRate + " -> " + afterVals.rainRate + "\r\n";
        }
        if (beforeVals.paradigm != afterVals.paradigm) {
            ret += "Paradigm: " + beforeVals.paradigm + " -> " + afterVals.paradigm + "\r\n";
        }
        if (beforeVals.gravityRules != afterVals.gravityRules) {
            ret += "GravityRules: " + beforeVals.gravityRules + " -> " + afterVals.gravityRules + "\r\n";
        }
        if (beforeVals.growthRules != afterVals.growthRules) {
            ret += "GrowthRules: " + beforeVals.growthRules + " -> " + afterVals.growthRules + "\r\n";
        }
        if (beforeVals.gravityWellCenterX.equals(afterVals.gravityWellCenterX) == false) {
            ret += "GravityX: " + beforeVals.gravityWellCenterX + " -> " + afterVals.gravityWellCenterX + "\r\n";
        }
        if (beforeVals.gravityWellCenterY.equals(afterVals.gravityWellCenterY) == false) {
            ret += "GravityY: " + beforeVals.gravityWellCenterY + " -> " + afterVals.gravityWellCenterY + "\r\n";
        }
        
        // And now the shape variables.  We need the before/after here too, even though we do have just
        // the changes already saved in a changeset on this submit action.  But we want to know what they
        // were before.
        SysShape beforeShape = findBeforeShapeForSubmitAction(submitAction);
        SysShape afterShape = findAfterShapeForSubmitAction(submitAction);
        if (beforeShape == null || afterShape == null) {
            return ret;
        }
        // Unfortunately we don't actually have the id saved out, so we don't know if it's
        // a different shape, BUT, if the starting values are different than the ending values
        // from last turn, it has to be a different shape
        /*if (beforeShape.getId() != afterShape.getId()) {
            ret += "  NEW SHAPE\r\n";
        }*/

        if (beforeShape.getSpin() != afterShape.getSpin()) {
            ret += "ShapeSpinDir: " + beforeShape.getSpin() + " -> " + afterShape.getSpin() + "\r\n";
        }
        if (beforeShape.getSpinSpeed() != afterShape.getSpinSpeed()) {
            ret += "ShapeSpinSpd: " + beforeShape.getSpinSpeed() + " -> " + afterShape.getSpinSpeed() + "\r\n";
        }
        if (beforeShape.getNumCorners() != afterShape.getNumCorners()) {
            ret += "ShapeType: " + beforeShape.getNumCorners() + " -> " + afterShape.getNumCorners() + "\r\n";
        }
        if (beforeShape.getFill() != afterShape.getFill()) {
            //ret += "ShapeColor: " + beforeShape.getFill() + " -> " + afterShape.getFill() + "\r\n";
            // We really don't care what it changed to, just that it was changed, since the color itself
            // doesn't matter at all
            ret += "ShapeColor: Changed";
        }

        return ret;
    }

    public static String printVarRatingsForStageRoundTurn(int stageNum, int roundNum, int turnNum) {
        // Literally just grab the SubmitVarRating action and print the string which is exactly what we want
        // already.
        Action action = findSubmitVarRatingActionForStageRoundTurn(stageNum, roundNum, turnNum);
        if (action == null) {
            return "";
        }
        return action.getDesc();
    }

    public static int getTotalCorrectAnswersForStage1() {
        int totalCorrect = 0;
        int totalPartial = 0;
        int totalWrong = 0;
        for (int uid = 14; uid <= 23; uid++) {
            // And show total correct and not correct
            Answer ans = Player.getAnswerForQuestionUID(uid);
            if (ans == null) {
                continue;
            }
            int numCorrect = ans.getTotalCorrect();
            int numPartial = ans.getTotalPartial();
            int numWrong = ans.getTotalIncorrect();
    
            totalCorrect += numCorrect;
            totalPartial += numPartial;
            totalWrong += numWrong;
        }
        return totalCorrect;
    }

    public static int getTotalCorrectAnswersForStage2() {
        int totalCorrect = 0;
        int totalPartial = 0;
        int totalWrong = 0;
        for (int uid = 24; uid <= 35; uid++) {
            // And show total correct and not correct
            Answer ans = Player.getAnswerForQuestionUID(uid);
            if (ans == null) {
                continue;
            }
            int numCorrect = ans.getTotalCorrect();
            int numPartial = ans.getTotalPartial();
            int numWrong = ans.getTotalIncorrect();
    
            totalCorrect += numCorrect;
            totalPartial += numPartial;
            totalWrong += numWrong;
        }
        return totalCorrect;
    }
    
    public static int addScoreRatingForSkill_Stage1(Row rowHeader, Row row, int colNum, int domainNum, int skillNum) {
        return addScoreRatingForSkill_Stage1(rowHeader, row, colNum, domainNum, skillNum, STUtils.ScoreType.Full);
    }

    // The score ratings take the percentiles and convert them to 0-4 ratings basically
    public static int addScoreRatingForSkill_Stage1(Row rowHeader, Row row, int colNum, int domainNum, int skillNum, STUtils.ScoreType type) {
        int col = colNum;
        if (rowHeader != null) {
            rowHeader.createCell(col).setCellValue("" + domainNum + "." + skillNum);
        }
        int score = STUtils.calcIntScoreForSkill_Stage1(domainNum, skillNum, type);
        addDataPoint(row, col, STUtils.convertPercentScoreToRating(score));
        return col;
    }

    
    public static int addScoreRatingForSkill(Row rowHeader, Row row, int colNum, int domainNum, int skillNum) {
        return addScoreRatingForSkill(rowHeader, row, colNum, domainNum, skillNum, STUtils.ScoreType.Full);
    }

    public static int addScoreRatingForSkill(Row rowHeader, Row row, int colNum, int domainNum, int skillNum, STUtils.ScoreType type) {
        int col = colNum;
        if (rowHeader != null) {
            rowHeader.createCell(col).setCellValue("" + domainNum + "." + skillNum);
        }
        int score = STUtils.calcIntScoreForSkill(domainNum, skillNum, type);
        addDataPoint(row, col, STUtils.convertPercentScoreToRating(score));
        return col;
    }

    public static int addScoreForSkill_Stage1(Row rowHeader, Row row, int colNum, int domainNum, int skillNum) {
        return addScoreForSkill_Stage1(rowHeader, row, colNum, domainNum, skillNum, false);
    }

    public static int addScoreForSkill_Stage1(Row rowHeader, Row row, int colNum, int domainNum, int skillNum, boolean numberOnly) {
        int col = colNum;
        if (rowHeader != null) {
            rowHeader.createCell(col).setCellValue("" + domainNum + "." + skillNum + " Total");
        }
        String scoreStr = STUtils.calcScoreForSkill_Stage1(domainNum, skillNum);
        if (numberOnly == true) {
            int score = STUtils.getIntForScoreStr(scoreStr);
            addDataPoint(row, col, (int)score);
        }
        else {
            addDataPoint(row, col, scoreStr);
        }
        return col;
    }

    public static int addScoreForSkill(Row rowHeader, Row row, int colNum, int domainNum, int skillNum) {
        return addScoreForSkill(rowHeader, row, colNum, domainNum, skillNum, false);
    }

    public static int addScoreForSkill(Row rowHeader, Row row, int colNum, int domainNum, int skillNum, boolean numberOnly) {
        int col = colNum;
        if (rowHeader != null) {
            rowHeader.createCell(col).setCellValue("" + domainNum + "." + skillNum + " Total");
        }
        String scoreStr = STUtils.calcScoreForSkill(domainNum, skillNum);
        if (numberOnly == true) {
            int score = STUtils.getIntForScoreStr(scoreStr);
            addDataPoint(row, col, (int)score);
        }
        else {
            addDataPoint(row, col, scoreStr);
        }
        // I could put game data points here but I don't have a separate calculator for that right now,
        // it's just baked into each skill
        //rowHeader.createCell(col).setCellValue("" + domainNum + "." + skillNum + " PlayRating");
        //addDataPoint(row, col, STUtils.getCorrectForSkill(domainNum, skillNum));
        /*col++;
        rowHeader.createCell(col).setCellValue("" + domainNum + "." + skillNum + "C");
        addDataPoint(row, col, STUtils.getCorrectForSkill(domainNum, skillNum));
        col++;
        rowHeader.createCell(col).setCellValue("" + domainNum + "." + skillNum + "P");
        addDataPoint(row, col, STUtils.getPartialForSkill(domainNum, skillNum));
        col++;
        rowHeader.createCell(col).setCellValue("" + domainNum + "." + skillNum + "I");
        addDataPoint(row, col, STUtils.getIncorrectForSkill(domainNum, skillNum));*/

        // I could put the percentiles from each thing here, like X percentage from dp / ?s,
        // and X percentage from total game score
        
        return col;
    }

    
    // I guess most of these weren't necessary...
    public static void addDataPoint(Row row, int colNum, int val) {
        row.createCell(colNum).setCellValue(val);
    }

    public static void addDataPoint(Row row, int colNum, String val) {
        row.createCell(colNum).setCellValue(val);
    }

    public static void addDataPoint(Row row, int colNum, double val) {
        row.createCell(colNum).setCellValue(val);
    }

    public static void addDataPoint(Row row, int colNum, boolean val) {
        row.createCell(colNum).setCellValue(val);
    }

    public static void addDataPoint(Row row, int colNum, Answer val) {
        if (val == null) {
            row.createCell(colNum).setCellValue("");
        }
        else {
            row.createCell(colNum).setCellValue(val.getStrAnswer());
        }
    }

    private static String addRatings(Row row, int colNum, String prevRatings, int stageNum, int roundNum, int turnNum) {
        String ratings = printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum);
        if (ratings.equals(prevRatings) == true) {
            addDataPoint(row, colNum, "Same");
            return prevRatings;
        }
        addDataPoint(row, colNum, ratings);
        return ratings;
    }

    public static Workbook createExcelFile() {
        Workbook wb = new HSSFWorkbook();
        //Workbook wb = new XSSFWorkbook();
        //CreationHelper createHelper = wb.getCreationHelper();
        wb.createSheet("ST Results");
        wb.createSheet("Demographics");
        wb.createSheet("Feedback");
        wb.createSheet("Notepad");
        wb.createSheet("ST Exposure");
        wb.createSheet("Scores (Overall)");
        wb.createSheet("Scores (Stg1)");
        wb.createSheet("Score #s (Overall)");
        wb.createSheet("Score #s (Stg1)");
        wb.createSheet("Score R#s (Overall)"); // Ratings not percentiles
        wb.createSheet("Score R#s (Stg1)"); // Ratings not percentiles
        wb.createSheet("Score DP (Overall)"); // DP only
        wb.createSheet("Score DP (Stg1)"); // DP only
        return wb;
    }

    public static void writeExcelData(Workbook wb, int rowNum) {
        writeExcelSheet1(wb.getSheetAt(0), rowNum);
        writeExcelSheet2(wb.getSheetAt(1), rowNum);
        writeExcelSheet3(wb.getSheetAt(2), rowNum);
        writeExcelSheet4(wb.getSheetAt(3), rowNum);
        writeExcelSheet5(wb.getSheetAt(4), rowNum);
        writeExcelSheet6(wb.getSheetAt(5), rowNum);
        writeExcelSheet7(wb.getSheetAt(6), rowNum);
        writeExcelSheet8(wb.getSheetAt(7), rowNum);
        writeExcelSheet9(wb.getSheetAt(8), rowNum);
        writeExcelSheet10(wb.getSheetAt(9), rowNum);
        writeExcelSheet11(wb.getSheetAt(10), rowNum);
        writeExcelSheet12(wb.getSheetAt(11), rowNum);
        writeExcelSheet13(wb.getSheetAt(12), rowNum);

        // Do some aligning
        ExcelUtils.centerAlignColumns(wb.getSheetAt(7), 2);
        ExcelUtils.centerAlignColumns(wb.getSheetAt(8), 2);
        ExcelUtils.centerAlignColumns(wb.getSheetAt(9), 2);
        ExcelUtils.centerAlignColumns(wb.getSheetAt(10), 2);
        ExcelUtils.centerAlignColumns(wb.getSheetAt(11), 2);
        ExcelUtils.centerAlignColumns(wb.getSheetAt(12), 2);

        // Make them fit.
        ExcelUtils.autoSizeColumns(wb);

        // Limit their widths
        //ExcelUtils.limitColumnWidths(wb, 50);
        ExcelUtils.limitColumnWidths(wb.getSheetAt(0), 75);
        ExcelUtils.limitColumnWidths(wb.getSheetAt(1), 50);
        ExcelUtils.limitColumnWidths(wb.getSheetAt(2), 50);
        ExcelUtils.limitColumnWidths(wb.getSheetAt(3), 300);
        ExcelUtils.limitColumnWidths(wb.getSheetAt(4), 50);

        // Wrap them all
        ExcelUtils.wrapAllCells(wb);
    }

    public static void finalizeExcelData(Workbook wb) {
        finalizeExcelData(wb, false);
    }

    public static void finalizeExcelData(Workbook wb, boolean usePlayerName) {
        String fileNameAndPath = "";
        if (usePlayerName == false) {
            fileNameAndPath = "C:/Ross/Work/Japan/Drones/Code/systems_thinking_game_evolved/data/using/_Combined_All_Data";
        }
        else {
            fileNameAndPath = "C:/Ross/Work/Japan/Drones/Code/systems_thinking_game_evolved/data/using/" + Player.getName() + "_" + Player.getId();
        } 
        ExcelUtils.writeExcelFile(wb, fileNameAndPath);
        Utils.log("Excel file written.");
    }

    public static void writeRowHeader(Row rowHeader, int colNum, String str) {
        if (rowHeader == null) {
            return;
        }
        /*Cell cell = row.getCell(0);
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return;
        }*/
        rowHeader.createCell(colNum).setCellValue(str);
    }
    
    // So in here we want things like the player's scores, calculated total times of various activities, etc.
    // And here we will write out the data to Excel just as a more straight-forward data dump for analysis
    public static void writeExcelSheet1(Sheet sheet, int rowNum) {
        // So, write the data out to excel from the currently loaded player.  So we'll have to do these one at a time
        // and hand-merge the excel data for now but it shouldn't be too bad.  If we get a lot more results I can
        // automate that whole portion too.

        // Here is the general column organization I want:
        /*
        ID / Name / BG narrative / tutorial / scores / obs tim / exp time / # exp / stg1 time / stg2 time / var1/2/3/4/5 / var1/2/3/4/5/6/7/8/9/10 / 
          CONTINUED: stg1 strat / stg1 ans / stg2 strat / stg2 ans / self-assess / feedback ans / scratchpad
        */

        // So the way I'll set it up so that I can change the order of the columns if I want to is write out 
        // each thing.

        // So to save time, all I'm going to do is, when I click to write excel data I just write the currently
        // loaded person.  Then I'll just copy and paste the sheets together since there really aren't so many.
        /*Workbook wb = new HSSFWorkbook();
        //Workbook wb = new XSSFWorkbook();
        //CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("ST Results");
        Sheet sheet = wb.createSheet("Feedback");
        Sheet sheet = wb.createSheet("Notepad");
        Sheet sheet = wb.createSheet("Scores (Overall)");
        Sheet sheet = wb.createSheet("Scores (Stage 1 Only)");*/

        //row.createCell(colNum).setCellValue(createHelper.createRichTextString("This is a string"));
        
        // Create a row and put some cells in it. Rows are 0 based.
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);

        // Create a cell and put a value in it.
        //Cell cell = row.createCell(0);
        //cell.setCellValue(1);
        int colNum = 0;

        // question uid
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        // Email 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Email");
        addDataPoint(row, colNum, Player.getEmail());

        // Now do the demographics
        // Residence place
        colNum++;
        writeRowHeader(rowHeader, colNum, "Residence");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(2));

        // Age
        colNum++;
        writeRowHeader(rowHeader, colNum, "Age");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(3));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Age Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(3));

        // Gender
        colNum++;
        writeRowHeader(rowHeader, colNum, "Gender");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(1));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Gender Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(1));

        // Language
        colNum++;
        writeRowHeader(rowHeader, colNum, "English");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(4));

        colNum++;
        writeRowHeader(rowHeader, colNum, "English Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(4));

        // Gaming exp
        colNum++;
        writeRowHeader(rowHeader, colNum, "Gaming XP");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(8));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Gaming XP Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(8));

        // Learning Style
        colNum++;
        writeRowHeader(rowHeader, colNum, "Learning Styles");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(9));

        // Education
        colNum++;
        writeRowHeader(rowHeader, colNum, "Education");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(5));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Education Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(5));

        // ST XP
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stated ST Exp");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(7));

        // Study areas
        colNum++;
        writeRowHeader(rowHeader, colNum, "Study Areas");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(6));

        // Occupational Area
        colNum++;
        writeRowHeader(rowHeader, colNum, "Occupational Area");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(10));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Job Titles");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(11));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Career Experience");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(12));

        // This is my own rating of them based on their prior experience, I just
        // want a column for it so I can fill it in myself
        colNum++;
        writeRowHeader(rowHeader, colNum, "ST Level");
        addDataPoint(row, colNum, 1);

        // A "score" based on yrs of experience from background
        colNum++;
        writeRowHeader(rowHeader, colNum, "ST XP Yrs");
        addDataPoint(row, colNum, 0);

        // scores 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Scores");
        addDataPoint(row, colNum, Player.getScoreText());

        // Now I need the numerical scores
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg1.1 Score");
        addDataPoint(row, colNum, Player.getStrScoreForStageRound(1, 1));
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg1.2 Score");
        addDataPoint(row, colNum, Player.getStrScoreForStageRound(1, 2));
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg2.1 Score");
        addDataPoint(row, colNum, Player.getStrScoreForStageRound(2, 1));
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg2.2 Score");
        addDataPoint(row, colNum, Player.getStrScoreForStageRound(2, 2));

        // tut 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Tutorial");
        addDataPoint(row, colNum, printTutorialDataString());

        // times played 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Times Played");
        addDataPoint(row, colNum, Player.getTimesPlayed());

        // total time
        colNum++;
        writeRowHeader(rowHeader, colNum, "Total Time");
        addDataPoint(row, colNum, Utils.printHoursMinsSecsFromMS(Player.getEndTime() - Player.getStartTime()));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Time Obs");
        addDataPoint(row, colNum, Utils.printHoursMinsSecsFromMS(calcTotalTimeObs()));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Time Exp");
        addDataPoint(row, colNum, Utils.printHoursMinsSecsFromMS(calcTotalTimeExp()));
        
        // # exp 
        colNum++;
        writeRowHeader(rowHeader, colNum, "# Exp");
        addDataPoint(row, colNum, printNumExps());
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Time Self-A");
        addDataPoint(row, colNum, Utils.printHoursMinsSecsFromMS(calcSelfAssessmentTime()));

        // time in each section
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg1.1 Time");
        addDataPoint(row, colNum, Utils.printHoursMinsSecsFromMS(calcTimeForTask(3, 0, 3, 1)));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg1.2 Time");
        addDataPoint(row, colNum, Utils.printHoursMinsSecsFromMS(calcTimeForTask(3, 1, 4, 0)));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg2.1 Time");
        addDataPoint(row, colNum, Utils.printHoursMinsSecsFromMS(calcTimeForTask(4, 0, 4, 1)));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg2.2 Time");
        addDataPoint(row, colNum, Utils.printHoursMinsSecsFromMS(calcTimeForTask(4, 1, 5, 0)));
        
        int stageNum = 0; 
        int roundNum = 0;
        int turnNum = 0;

        String prevRatings = "";

        // var1/2/3/4/5 
        // So what I want to know here is, what they changed in stg 1.1
        // using printChangedVarsForStageRoundTurn(3, 0, 0)
        stageNum = 3; 
        roundNum = 0;
        turnNum = 0;
        for (turnNum = 0; turnNum < 5; turnNum++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Vars:1.1." + (turnNum+1));  // The readable text is different than the internal #s
            addDataPoint(row, colNum, printChangedVarsForStageRoundTurn(stageNum, roundNum, turnNum));
        }

        // var ratings 1/2/3/4/5
        // Now just write out the ratings for each round
        // using printVarRatingsForStageRoundTurn(3, 0, 0)
        stageNum = 3; 
        roundNum = 0;
        turnNum = 0;
        prevRatings = "";
        for (turnNum = 0; turnNum < 5; turnNum++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Ratings:1.1." + (turnNum+1));  // The readable text is different than the internal #s
            prevRatings = addRatings(row, colNum, prevRatings, stageNum, roundNum, turnNum);
            /*String ratings = printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum);
            if (ratings.equals(prevRatings) == true) {
                addDataPoint(row, colNum, "Same");
            }
            addDataPoint(row, colNum, printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum));*/
        }

        stageNum = 3; 
        roundNum = 1;
        turnNum = 0;
        for (turnNum = 0; turnNum < 5; turnNum++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Vars:1.2." + (turnNum+1));  // The readable text is different than the internal #s
            addDataPoint(row, colNum, printChangedVarsForStageRoundTurn(stageNum, roundNum, turnNum));
        }

        stageNum = 3; 
        roundNum = 1;
        turnNum = 0;
        prevRatings = "";
        for (turnNum = 0; turnNum < 5; turnNum++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Ratings:1.2." + (turnNum+1));  // The readable text is different than the internal #s
            //addDataPoint(row, colNum, printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum));
            prevRatings = addRatings(row, colNum, prevRatings, stageNum, roundNum, turnNum);
        }

        stageNum = 4; 
        roundNum = 0;
        turnNum = 0;
        for (turnNum = 0; turnNum < 10; turnNum++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Vars:2.1." + (turnNum+1));  // The readable text is different than the internal #s
            addDataPoint(row, colNum, printChangedVarsForStageRoundTurn(stageNum, roundNum, turnNum));
        }

        // var ratings 1/2/3/4/5
        // Now just write out the ratings for each round
        // using printVarRatingsForStageRoundTurn(3, 0, 0)
        stageNum = 4; 
        roundNum = 0;
        turnNum = 0;
        prevRatings = "";
        for (turnNum = 0; turnNum < 10; turnNum++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Ratings:2.1." + (turnNum+1));  // The readable text is different than the internal #s
            prevRatings = addRatings(row, colNum, prevRatings, stageNum, roundNum, turnNum);
            /*String ratings = printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum);
            if (ratings.equals(prevRatings) == true) {
                addDataPoint(row, colNum, "Same");
            }
            prevRatings = ratings;
            addDataPoint(row, colNum, printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum));*/
        }
        
        stageNum = 4; 
        roundNum = 1;
        turnNum = 0;
        for (turnNum = 0; turnNum < 10; turnNum++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Vars:2.2." + (turnNum+1));  // The readable text is different than the internal #s
            addDataPoint(row, colNum, printChangedVarsForStageRoundTurn(stageNum, roundNum, turnNum));
        }

        // var ratings 1/2/3/4/5
        // Now just write out the ratings for each round
        // using printVarRatingsForStageRoundTurn(3, 0, 0)
        stageNum = 4; 
        roundNum = 1;
        turnNum = 0;
        prevRatings = "";
        for (turnNum = 0; turnNum < 10; turnNum++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Ratings:2.2." + (turnNum+1));  // The readable text is different than the internal #s
            prevRatings = addRatings(row, colNum, prevRatings, stageNum, roundNum, turnNum);
            //addDataPoint(row, colNum, printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum));
        }

        int totalCorrect = 0;
        int totalPartial = 0;
        int totalWrong = 0;
        
        // stg1 answers
        // 14-23
        for (uid = 14; uid <= 23; uid++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Stg1: " + Data.getQuestionByUID(uid).getText().replace("\n", "").replace("\r", ""));
            addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(uid));

            // And show total correct and not correct
            Answer ans = Player.getAnswerForQuestionUID(uid);
            if (ans == null) {
                // If they didn't answer these, just leave it all blank
                // so that I will know that there isn't an answer and not try to 
                // grade it as a zero
                colNum++;
                writeRowHeader(rowHeader, colNum, "C" + uid);
                colNum++;
                writeRowHeader(rowHeader, colNum, "P" + uid);
                colNum++;
                writeRowHeader(rowHeader, colNum, "I" + uid);
                continue;
            }
            int numCorrect = ans.getTotalCorrect();
            int numPartial = ans.getTotalPartial();
            int numWrong = ans.getTotalIncorrect();

            totalCorrect += numCorrect;
            totalPartial += numPartial;
            totalWrong += numWrong;
            
            colNum++;
            writeRowHeader(rowHeader, colNum, "C" + uid);
            addDataPoint(row, colNum, numCorrect);

            colNum++;
            writeRowHeader(rowHeader, colNum, "P" + uid);
            addDataPoint(row, colNum, numPartial);

            colNum++;
            writeRowHeader(rowHeader, colNum, "I" + uid);
            addDataPoint(row, colNum, numCorrect);
        }
        
        // stg2 answers 
        // 24-35
        for (uid = 24; uid <= 35; uid++) {
            colNum++;
            writeRowHeader(rowHeader, colNum, "Stg2: " + Data.getQuestionByUID(uid).getText().replace("\n", "").replace("\r", ""));
            addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(uid));

            // And show total correct and not correct
            Answer ans = Player.getAnswerForQuestionUID(uid);
            if (ans == null) {
                // If they didn't answer these, just leave it all blank
                // so that I will know that there isn't an answer and not try to 
                // grade it as a zero
                colNum++;
                writeRowHeader(rowHeader, colNum, "C" + uid);
                colNum++;
                writeRowHeader(rowHeader, colNum, "P" + uid);
                colNum++;
                writeRowHeader(rowHeader, colNum, "I" + uid);
                continue;
            }
            int numCorrect = ans.getTotalCorrect();
            int numPartial = ans.getTotalPartial();
            int numWrong = ans.getTotalIncorrect();

            numCorrect = ans.getTotalCorrect();
            numPartial = ans.getTotalPartial();
            numWrong = ans.getTotalIncorrect();

            totalCorrect += numCorrect;
            totalPartial += numPartial;
            totalWrong += numWrong;
            
            colNum++;
            writeRowHeader(rowHeader, colNum, "C" + uid);
            addDataPoint(row, colNum, numCorrect);

            colNum++;
            writeRowHeader(rowHeader, colNum, "P" + uid);
            addDataPoint(row, colNum, numPartial);

            colNum++;
            writeRowHeader(rowHeader, colNum, "I" + uid);
            addDataPoint(row, colNum, numWrong);
        }
        
        // Now total the rights/wrongs/partials for all the questions
        colNum++;
        writeRowHeader(rowHeader, colNum, "Total C");
        addDataPoint(row, colNum, totalCorrect);

        colNum++;
        writeRowHeader(rowHeader, colNum, "Total P");
        addDataPoint(row, colNum, totalPartial);

        colNum++;
        writeRowHeader(rowHeader, colNum, "Total I");
        addDataPoint(row, colNum, totalWrong);

        // Now do totals for the ratings, just for turn 0 of each stage- add up the ratings for specific
        // vs. overall and see where everyone stands
        String ratings = "";
        RatingData ratings11 = new RatingData(3, 0, 0);
        RatingData ratings12 = new RatingData(3, 1, 0);
        RatingData ratings21 = new RatingData(4, 0, 0);
        RatingData ratings22 = new RatingData(4, 1, 0);

        colNum++;
        writeRowHeader(rowHeader, colNum, "1.1 Total O Rating");
        if (ratings11 != null) {
            addDataPoint(row, colNum, ratings11.getWeightedSumOverallRatings());
        }
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "1.1 Total S Rating");
        if (ratings11 != null) {
            addDataPoint(row, colNum, ratings11.getWeightedSumSpecificRatings());
        }

        colNum++;
        writeRowHeader(rowHeader, colNum, "1.2 Total O Rating");
        if (ratings12 != null) {
            addDataPoint(row, colNum, ratings12.getWeightedSumOverallRatings());
        }
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "1.2 Total S Rating");
        if (ratings12 != null) {
            addDataPoint(row, colNum, ratings12.getWeightedSumSpecificRatings());
        }

        colNum++;
        writeRowHeader(rowHeader, colNum, "2.1 Total O Rating");
        if (ratings21 != null) {
            addDataPoint(row, colNum, ratings21.getWeightedSumOverallRatings());
        }
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "2.1 Total S Rating");
        if (ratings21 != null) {
            addDataPoint(row, colNum, ratings21.getWeightedSumSpecificRatings());
        }
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "2.2 Total O Rating");
        if (ratings22 != null) {
            addDataPoint(row, colNum, ratings22.getWeightedSumOverallRatings());
        }
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "2.2 Total S Rating");
        if (ratings22 != null) {
            addDataPoint(row, colNum, ratings22.getWeightedSumSpecificRatings());
        }
        
        // self-assess questions
        // starting with uid 42
        uid = 42;
        colNum++;
        writeRowHeader(rowHeader, colNum, "1.1");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "1.2");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "1.3");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "1.4");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "1.5");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "2.1");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "2.2");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "2.3");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "3.1");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "3.2");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "3.3");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "3.4");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "4.1");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "4.2");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "4.3");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "4.4");
        addDataPoint(row, colNum, Player.getFirstCharForAnswerUID(uid++));
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    // Demographics only
    public static void writeExcelSheet2(Sheet sheet, int rowNum) {
        // Create a row and put some cells in it. Rows are 0 based.
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());

        // Now do the demographics
        // Residence place
        colNum++;
        writeRowHeader(rowHeader, colNum, "Residence");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(2));

        // Age
        colNum++;
        writeRowHeader(rowHeader, colNum, "Age");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(3));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Age Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(3));

        // Gender
        colNum++;
        writeRowHeader(rowHeader, colNum, "Gender");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(1));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Gender Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(1));

        // Language
        colNum++;
        writeRowHeader(rowHeader, colNum, "English");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(4));

        colNum++;
        writeRowHeader(rowHeader, colNum, "English Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(4));

        // Gaming exp
        colNum++;
        writeRowHeader(rowHeader, colNum, "Gaming XP");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(8));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Gaming XP Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(8));

        // Learning Style
        colNum++;
        writeRowHeader(rowHeader, colNum, "Learning Styles");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(9));

        // Education
        colNum++;
        writeRowHeader(rowHeader, colNum, "Education");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(5));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Education Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(5));

        // Study areas
        colNum++;
        writeRowHeader(rowHeader, colNum, "Study Areas");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(6));

        // Occupational Area
        colNum++;
        writeRowHeader(rowHeader, colNum, "Occupational Area");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(10));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Job Titles");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(11));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Career Experience");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(12));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Career Code");
        addDataPoint(row, colNum, Player.getCodeAnswerForQuestionUID(12));

        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    public static void writeExcelSheet3(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());

        // feedback ans 
        // starts with uid 36
        uid = 36;
        colNum++;
        writeRowHeader(rowHeader, colNum, "FB1: " + Data.getQuestionByUID(uid).getText());
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "FB2: " + Data.getQuestionByUID(uid).getText());
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "FB3: " + Data.getQuestionByUID(uid).getText());
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "FB4: " + Data.getQuestionByUID(uid).getText());
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "FB5: " + Data.getQuestionByUID(uid).getText());
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(uid++));
        colNum++;
        writeRowHeader(rowHeader, colNum, "FB6: " + Data.getQuestionByUID(uid).getText());
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(uid++));
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }
    
    public static void writeExcelSheet4(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Notepad");
        addDataPoint(row, colNum, Player.getScratchPadData());
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    
    public static void writeExcelSheet5(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Career Experience");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(12));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Job Titles");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(11));

        // Education
        colNum++;
        writeRowHeader(rowHeader, colNum, "Education");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(5));

        // ST XP
        colNum++;
        writeRowHeader(rowHeader, colNum, "Stated ST Exp");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(7));

        // Study areas
        colNum++;
        writeRowHeader(rowHeader, colNum, "Study Areas");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(6));

        // Occupational Area
        colNum++;
        writeRowHeader(rowHeader, colNum, "Occupational Area");
        addDataPoint(row, colNum, Player.getStrAnswerForQuestionUID(10));
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    public static void writeExcelSheet6(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        // Now do the grading based on my rubric for what skills need which questions
        // Here are the ones I'm scoring:
        // 1.2  Wholes and Parts
        // 1.3  Effectively Respond to Uncertainty and Ambiguity
        // 1.4  Consider Issues Appropriately
        // 2.2  Define and Maintain Boundaries
        // 2.3  Differentiate and Quantify Elements
        // 3.1  Identify Relationships
        // 3.2  Characterize Relationships
        // 3.3  Identify Feedback Loops
        // 3.4  Characterize Feedback Loops
        // 4.2  Predict Future System Behavior
        // 4.3  Respond to Changes over Time
        // 4.4  Use Leverage Points to Produce Effects
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 1, 2);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 1, 3);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 1, 4);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 2, 2);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 2, 3);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 3, 1);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 3, 2);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 3, 3);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 3, 4);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 4, 2);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 4, 3);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 4, 4);
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    public static void writeExcelSheet7(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        // Now do the grading based on my rubric for what skills need which questions
        // Here are the ones I'm scoring:
        // 1.2  Wholes and Parts
        // 1.3  Effectively Respond to Uncertainty and Ambiguity
        // 1.4  Consider Issues Appropriately
        // 2.2  Define and Maintain Boundaries
        // 2.3  Differentiate and Quantify Elements
        // 3.1  Identify Relationships
        // 3.2  Characterize Relationships
        // 3.3  Identify Feedback Loops
        // 3.4  Characterize Feedback Loops
        // 4.2  Predict Future System Behavior
        // 4.3  Respond to Changes over Time
        // 4.4  Use Leverage Points to Produce Effects
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 1, 2);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 1, 3);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 1, 4);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 2, 2);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 2, 3);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 3, 1);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 3, 2);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 3, 3);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 3, 4);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 4, 2);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 4, 3);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 4, 4);
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    
    public static void writeExcelSheet8(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        // Now do the grading based on my rubric for what skills need which questions
        // Here are the ones I'm scoring:
        // 1.2  Wholes and Parts
        // 1.3  Effectively Respond to Uncertainty and Ambiguity
        // 1.4  Consider Issues Appropriately
        // 2.2  Define and Maintain Boundaries
        // 2.3  Differentiate and Quantify Elements
        // 3.1  Identify Relationships
        // 3.2  Characterize Relationships
        // 3.3  Identify Feedback Loops
        // 3.4  Characterize Feedback Loops
        // 4.2  Predict Future System Behavior
        // 4.3  Respond to Changes over Time
        // 4.4  Use Leverage Points to Produce Effects
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 1, 2, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 1, 3, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 1, 4, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 2, 2, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 2, 3, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 3, 1, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 3, 2, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 3, 3, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 3, 4, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 4, 2, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 4, 3, true);
        colNum++;
        colNum = addScoreForSkill(rowHeader, row, colNum, 4, 4, true);
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    public static void writeExcelSheet9(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        // Now do the grading based on my rubric for what skills need which questions
        // Here are the ones I'm scoring:
        // 1.2  Wholes and Parts
        // 1.3  Effectively Respond to Uncertainty and Ambiguity
        // 1.4  Consider Issues Appropriately
        // 2.2  Define and Maintain Boundaries
        // 2.3  Differentiate and Quantify Elements
        // 3.1  Identify Relationships
        // 3.2  Characterize Relationships
        // 3.3  Identify Feedback Loops
        // 3.4  Characterize Feedback Loops
        // 4.2  Predict Future System Behavior
        // 4.3  Respond to Changes over Time
        // 4.4  Use Leverage Points to Produce Effects
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 1, 2, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 1, 3, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 1, 4, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 2, 2, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 2, 3, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 3, 1, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 3, 2, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 3, 3, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 3, 4, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 4, 2, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 4, 3, true);
        colNum++;
        colNum = addScoreForSkill_Stage1(rowHeader, row, colNum, 4, 4, true);
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }
    
    public static void writeExcelSheet10(Sheet sheet, int rowNum) {
        // If you didn't even do stage 2, you should not be considered
        // in the overall score measures
        if (Player.getTopScoreForStage(2) < 0) {
            return;
        }

        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "#EX");
        addDataPoint(row, colNum, countNumNewExps());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg1");
        addDataPoint(row, colNum, Player.getTopScoreForStage(1));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Imp1");
        addDataPoint(row, colNum, Player.getScoreForStageRound(1, 2) - Player.getScoreForStageRound(1, 1));

        colNum++;
        writeRowHeader(rowHeader, colNum, "CQ1");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage1());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg2");
        addDataPoint(row, colNum, Player.getTopScoreForStage(2));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Imp2");
        addDataPoint(row, colNum, Player.getScoreForStageRound(2, 2) - Player.getScoreForStageRound(2, 1));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "CQ2");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage2());

        colNum++;
        writeRowHeader(rowHeader, colNum, "CQA");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage1() + getTotalCorrectAnswersForStage2());
        
        // Now do the grading based on my rubric for what skills need which questions
        // Here are the ones I'm scoring:
        // 1.2  Wholes and Parts
        // 1.3  Effectively Respond to Uncertainty and Ambiguity
        // 1.4  Consider Issues Appropriately
        // 2.2  Define and Maintain Boundaries
        // 2.3  Differentiate and Quantify Elements
        // 3.1  Identify Relationships
        // 3.2  Characterize Relationships
        // 3.3  Identify Feedback Loops
        // 3.4  Characterize Feedback Loops
        // 4.2  Predict Future System Behavior
        // 4.3  Respond to Changes over Time
        // 4.4  Use Leverage Points to Produce Effects
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 1, 2);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 1, 3);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 1, 4);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 2, 2);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 2, 3);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 3, 1);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 3, 2);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 3, 3);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 3, 4);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 4, 2);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 4, 3);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 4, 4);
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    public static void writeExcelSheet11(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "#EX");
        addDataPoint(row, colNum, countNumNewExps());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg1");
        addDataPoint(row, colNum, Player.getStrScoreForStageRound(1, 2));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Imp1");
        addDataPoint(row, colNum, Player.getScoreForStageRound(1, 2) - Player.getScoreForStageRound(1, 1));

        colNum++;
        writeRowHeader(rowHeader, colNum, "CQ1");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage1());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg2");
        addDataPoint(row, colNum, Player.getStrScoreForStageRound(2, 2));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Imp2");
        addDataPoint(row, colNum, Player.getScoreForStageRound(2, 2) - Player.getScoreForStageRound(2, 1));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "CQ2");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage2());

        colNum++;
        writeRowHeader(rowHeader, colNum, "CQA");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage1() + getTotalCorrectAnswersForStage2());
        
        // Now do the grading based on my rubric for what skills need which questions
        // Here are the ones I'm scoring:
        // 1.2  Wholes and Parts
        // 1.3  Effectively Respond to Uncertainty and Ambiguity
        // 1.4  Consider Issues Appropriately
        // 2.2  Define and Maintain Boundaries
        // 2.3  Differentiate and Quantify Elements
        // 3.1  Identify Relationships
        // 3.2  Characterize Relationships
        // 3.3  Identify Feedback Loops
        // 3.4  Characterize Feedback Loops
        // 4.2  Predict Future System Behavior
        // 4.3  Respond to Changes over Time
        // 4.4  Use Leverage Points to Produce Effects
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 1, 2);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 1, 3);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 1, 4);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 2, 2);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 2, 3);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 3, 1);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 3, 2);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 3, 3);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 3, 4);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 4, 2);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 4, 3);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 4, 4);
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    public static void writeExcelSheet12(Sheet sheet, int rowNum) {
        // If you didn't even do stage 2, you should not be considered
        // in the overall score measures
        if (Player.getTopScoreForStage(2) < 0) {
            return;
        }

        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "#EX");
        addDataPoint(row, colNum, countNumNewExps());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg1");
        addDataPoint(row, colNum, Player.getTopScoreForStage(1));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Imp1");
        addDataPoint(row, colNum, Player.getScoreForStageRound(1, 2) - Player.getScoreForStageRound(1, 1));

        colNum++;
        writeRowHeader(rowHeader, colNum, "CQ1");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage1());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg2");
        addDataPoint(row, colNum, Player.getTopScoreForStage(2));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Imp2");
        addDataPoint(row, colNum, Player.getScoreForStageRound(2, 2) - Player.getScoreForStageRound(2, 1));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "CQ2");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage2());

        colNum++;
        writeRowHeader(rowHeader, colNum, "CQA");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage1() + getTotalCorrectAnswersForStage2());
        
        // Now do the grading based on my rubric for what skills need which questions
        // Here are the ones I'm scoring:
        // 1.2  Wholes and Parts
        // 1.3  Effectively Respond to Uncertainty and Ambiguity
        // 1.4  Consider Issues Appropriately
        // 2.2  Define and Maintain Boundaries
        // 2.3  Differentiate and Quantify Elements
        // 3.1  Identify Relationships
        // 3.2  Characterize Relationships
        // 3.3  Identify Feedback Loops
        // 3.4  Characterize Feedback Loops
        // 4.2  Predict Future System Behavior
        // 4.3  Respond to Changes over Time
        // 4.4  Use Leverage Points to Produce Effects
        STUtils.ScoreType type = STUtils.ScoreType.DPOnly;
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 1, 2, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 1, 3, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 1, 4, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 2, 2, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 2, 3, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 3, 1, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 3, 2, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 3, 3, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 3, 4, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 4, 2, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 4, 3, type);
        colNum++;
        colNum = addScoreRatingForSkill(rowHeader, row, colNum, 4, 4, type);
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }

    public static void writeExcelSheet13(Sheet sheet, int rowNum) {
        Row rowHeader = null;
        if (rowNum == 1) {
            rowHeader = sheet.createRow(0);
        }
        Row row = sheet.createRow(rowNum);
        int colNum = 0;
        int uid = 0;

        // ID
        writeRowHeader(rowHeader, colNum, "ID");
        addDataPoint(row, colNum, Player.getId());

        // Name 
        colNum++;
        writeRowHeader(rowHeader, colNum, "Name");
        addDataPoint(row, colNum, Player.getName());

        // STXP 
        colNum++;
        writeRowHeader(rowHeader, colNum, "STXP");
        addDataPoint(row, colNum, Player.getSTExposure());
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "#EX");
        addDataPoint(row, colNum, countNumNewExps());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg1");
        addDataPoint(row, colNum, Player.getStrScoreForStageRound(1, 2));

        colNum++;
        writeRowHeader(rowHeader, colNum, "Imp1");
        addDataPoint(row, colNum, Player.getScoreForStageRound(1, 2) - Player.getScoreForStageRound(1, 1));

        colNum++;
        writeRowHeader(rowHeader, colNum, "CQ1");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage1());

        colNum++;
        writeRowHeader(rowHeader, colNum, "Stg2");
        addDataPoint(row, colNum, Player.getStrScoreForStageRound(2, 2));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "Imp2");
        addDataPoint(row, colNum, Player.getScoreForStageRound(2, 2) - Player.getScoreForStageRound(2, 1));
        
        colNum++;
        writeRowHeader(rowHeader, colNum, "CQ2");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage2());

        colNum++;
        writeRowHeader(rowHeader, colNum, "CQA");
        addDataPoint(row, colNum, getTotalCorrectAnswersForStage1() + getTotalCorrectAnswersForStage2());
        
        // Now do the grading based on my rubric for what skills need which questions
        // Here are the ones I'm scoring:
        // 1.2  Wholes and Parts
        // 1.3  Effectively Respond to Uncertainty and Ambiguity
        // 1.4  Consider Issues Appropriately
        // 2.2  Define and Maintain Boundaries
        // 2.3  Differentiate and Quantify Elements
        // 3.1  Identify Relationships
        // 3.2  Characterize Relationships
        // 3.3  Identify Feedback Loops
        // 3.4  Characterize Feedback Loops
        // 4.2  Predict Future System Behavior
        // 4.3  Respond to Changes over Time
        // 4.4  Use Leverage Points to Produce Effects
        STUtils.ScoreType type = STUtils.ScoreType.DPOnly;
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 1, 2, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 1, 3, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 1, 4, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 2, 2, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 2, 3, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 3, 1, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 3, 2, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 3, 3, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 3, 4, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 4, 2, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 4, 3, type);
        colNum++;
        colNum = addScoreRatingForSkill_Stage1(rowHeader, row, colNum, 4, 4, type);
        
        if (rowHeader != null) {
            rowHeader.setHeight((short)-1);
        }
        row.setHeight((short)-1);
    }
}