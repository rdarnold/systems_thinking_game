package gos;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
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

// Basically a holder of static data that contains player-related stuff.
// Like what task we're on, what answers we have made, how many discovery
// points we have left.
public final class Player {
    private Player () { // private constructor
    }

    public static void init() {
        observation = new TurnSet();
        playedTurns = new TurnSet();
    }

    public static ArrayList<Action> actions = new ArrayList<Action>();
    public static ArrayList<Answer> answers = new ArrayList<Answer>();

    private static Exercise currentExercise;

    private static TurnSet observation;
    public static TurnSet getObservation() { return observation; }

    private static TurnSet playedTurns;
    public static TurnSet getPlayedTurns() { return playedTurns; }

    private static SysShape selectedShape = null;
    public static long startTime = 0;
    public static long endTime = 0;

    // Start out in prac mode, if you're in prac mode, certain buttons
    // and controls aren't available.
    private static boolean m_bInPracticeMode = true;
    public static boolean inPracticeMode() { return m_bInPracticeMode; }

    // Did the player skip sections?  Only for testing and debugging
    private static boolean m_bSkipped = false;
    public static boolean getSkipped() { return m_bSkipped; }
    public static void setSkipped() { m_bSkipped = true; }

    // Info if the player wants to provide it.
    private static int id = 0;
    private static int submittedId = 0; // If they tried to submit their own ID we record that too
    private static String name = "Anon";
    private static String email = "";
    private static int timesPlayed = 0;
    private static boolean contactConsent = false;
    private static boolean playedBefore = false;
    
    public static int getId() { return id; }
    public static int getSubmittedId() { return submittedId; }
    public static String getName() { return name; }
    public static String getEmail() { return email; }
    public static int getTimesPlayed() { return timesPlayed; }
    public static boolean getContactConsent() { return contactConsent; }
    public static boolean getPlayedBefore() { return playedBefore; }

    public static void setId(int num) { id = num; }
    public static void setSubmittedId(int num) { submittedId = num; }
    public static void setName(String str) { 
        if (str == null || str.equals("")) {
            name = "Anon";
        }
        else {
            name = str;
        }
    }
    public static void setEmail(String str) { 
        if (str == null || str.equals("")) {
            email = "";
        }
        else {
            email = str;
        }
    }
    public static void setTimesPlayed(int num) { timesPlayed = num; }
    public static void setContactConsent(boolean b) { contactConsent = b; }
    public static void setPlayedBefore(boolean b) { playedBefore = b; }

    public static String lastSavedData = "";

    private static final int defaultDiscoveryPoints = 5;
    private static final int defaultMaxTurns = 5;
    ////////////////////
    //// PROPERTIES ////
    ////////////////////
    // Discovery Points are a property so we can easily bind.
    private static final IntegerProperty discoveryPoints = 
        new SimpleIntegerProperty(defaultDiscoveryPoints);
    public static IntegerProperty discoveryPointsProperty() { return discoveryPoints; }
    public static int getDiscoveryPoints() { return discoveryPoints.get(); }
    public static void setDiscoveryPoints(int num) { discoveryPoints.set(num); }
    public static void resetDiscoveryPoints() { discoveryPoints.set(defaultDiscoveryPoints); }
    public static void addDiscoveryPoints() { addDiscoveryPoints(1); }
    public static void addDiscoveryPoints(int num) {  discoveryPoints.set(discoveryPoints.get() + num); }
    public static void subDiscoveryPoints() { subDiscoveryPoints(1); }
    public static void subDiscoveryPoints(int num) {
        discoveryPoints.set(discoveryPoints.get() - num);
        if (discoveryPoints.get() < 0) { discoveryPoints.set(0); }
    }

    /*private static final IntegerProperty maxTurns = 
        new SimpleIntegerProperty(defaultMaxTurns);
    public static IntegerProperty maxTurnsProperty() { return maxTurns; }
    public static int getMaxTurns() { return maxTurns.get(); }
    public static void setMaxTurns(int num) { maxTurns.set(num); }
    public static void resetMaxTurns() { maxTurns.set(defaultMaxTurns); }
    public static void addMaxTurns() { addMaxTurns(1); }
    public static void addMaxTurns(int num) {  maxTurns.set(maxTurns.get() + num); }
    public static void subMaxMaxTurns() { subMaxMaxTurns(1); }
    public static void subMaxMaxTurns(int num) {
        maxTurns.set(maxTurns.get() - num);
        if (maxTurns.get() < 0) {  maxTurns.set(0);  }
    }*/

    // Unfortunately this has to be different than the turn number in the TurnSet.
    // Because when we start a new turn, we haven't added a turn obj to the turn set
    // yet.  And we won't until we actually hit submit from Change System, and that's
    // how it should be.  I could do some other stuff with adding a new turn as soon
    // as the old one is done and putting it in pause mode, etc., but this is honestly
    // just easier than that.
    private static final IntegerProperty currentTurnNumber = new SimpleIntegerProperty(0);
    public static IntegerProperty currentTurnNumberProperty() { return currentTurnNumber; }
    public static int getCurrentTurnNumber() { return currentTurnNumber.get(); }
    public static void setCurrentTurnNumber(int num) { currentTurnNumber.set(num); }
    public static void resetCurrentTurnNumber() { currentTurnNumber.set(0); }
    public static void addCurrentTurnNumber() { addCurrentTurnNumber(1); }
    public static void addCurrentTurnNumber(int num) {  currentTurnNumber.set(currentTurnNumber.get() + num); }
    public static void subCurrentTurnNumber() { subCurrentTurnNumber(1); }
    public static void subCurrentTurnNumber(int num) {
        currentTurnNumber.set(currentTurnNumber.get() - num);
        if (currentTurnNumber.get() < 0) {  currentTurnNumber.set(0);  }
    }

    // Since it's zero-indexed we add 1 to make it look right to the player, and
    // clamp it so that at the end of the turn, when it increments past the max turns,
    // it doesn't display that.
    public static int getCurrentTurnNumberDisplay() { 
        return (Utils.clamp((currentTurnNumber.get() + 1), 0, getMaxTurns())); 
    }

    /*public static int getCurrentTurnNumber() {
        return getPlayedTurns().getTurnNumber();
    }*/
    public static int getMaxTurns() {
        return getPlayedTurns().getMaxTurns();
    }
    public static void setMaxTurns(int num) {
        getPlayedTurns().setMaxTurns(num);
    }
    
    // These are set as properties so we can bind them.
    // The "real" exercise and task numbers are on the current exercise, but because it
    // changes, we can't bind them, so when it changes, we set these in there, and then
    // we can bind these.  It's one extra step of misdirection for a lot of headache saving
    // down the line.
    // They are called "Tracker" so that they are not confused with the "actual" exercise
    // and task numbers which are on the Exercise itself.  These just are updated when the
    // exercise is updated only for the purpose of "bind" updates on screens.
    private static final IntegerProperty currentExerciseNumberTracker = new SimpleIntegerProperty(0);
    public static int getCurrentExerciseNumberTracker() { return currentExerciseNumberTracker.get(); }
    public static void setCurrentExerciseNumberTracker(int num) { currentExerciseNumberTracker.set(num);  }
    public static IntegerProperty currentExerciseNumberTrackerProperty() { return currentExerciseNumberTracker; }
    
    private static final IntegerProperty currentExerciseDisplayIdTracker = new SimpleIntegerProperty(0);
    public static int getCurrentExerciseDisplayIdTracker() { return currentExerciseDisplayIdTracker.get(); }
    public static void setCurrentExerciseDisplayIdTracker(int num) { currentExerciseDisplayIdTracker.set(num);  }
    public static IntegerProperty currentExerciseDisplayIdTrackerProperty() { return currentExerciseDisplayIdTracker; }
        
    private static final StringProperty currentExerciseNameTracker = new SimpleStringProperty("");
    public static String getCurrentExerciseNameTracker() { return currentExerciseNameTracker.get(); }
    public static void setCurrentExerciseNameTracker(String str) { currentExerciseNameTracker.set(str);  }
    public static StringProperty currentExerciseNameTrackerProperty() { return currentExerciseNameTracker; }
    
    private static final StringProperty currentExerciseTextTracker = new SimpleStringProperty("");
    public static String getCurrentExerciseTextTracker() { return currentExerciseTextTracker.get(); }
    public static void setCurrentExerciseTextTracker(String str) { currentExerciseTextTracker.set(str);  }
    public static StringProperty currentExerciseTextTrackerProperty() { return currentExerciseTextTracker; }

    private static final IntegerProperty currentTaskNumberTracker = new SimpleIntegerProperty(0);
    public static int getCurrentTaskNumberTracker() { return currentTaskNumberTracker.get(); }
    public static void setCurrentTaskNumberTracker(int num) { currentTaskNumberTracker.set(num);  }
    public static IntegerProperty currentTaskNumberTrackerProperty() { return currentTaskNumberTracker; }
    
    private static final StringProperty currentTaskNameTracker = new SimpleStringProperty("");
    public static String getCurrentTaskNameTracker() { return currentTaskNameTracker.get(); }
    public static void setCurrentTaskNameTracker(String str) { currentTaskNameTracker.set(str);  }
    public static StringProperty currentTaskNameTrackerProperty() { return currentTaskNameTracker; }

    private static final StringProperty currentTaskTextTracker = new SimpleStringProperty("");
    public static String getCurrentTaskTextTracker() { return currentTaskTextTracker.get(); }
    public static void setCurrentTaskTextTracker(String str) { currentTaskTextTracker.set(str);  }
    public static StringProperty currentTaskTextTrackerProperty() { return currentTaskTextTracker; }
    ////////////////////////
    //// END PROPERTIES ////
    ////////////////////////

    public static void generateId() {
        id = Utils.number(1, 999999);
    }

    public static void resetForNewTask() {
        //resetMaxObservationTurns();
        //resetCurrentObservationTurnNumber();
        resetCurrentTurnNumber();
        resetDiscoveryPoints();
        observation.reset();
        playedTurns.reset();
    }

    public static void setSelectedShape(SysShape shape) {
        if (selectedShape != null) {
            selectedShape.setSelected(false);
        }
        selectedShape = shape;
        if (selectedShape != null) {
            selectedShape.setSelected(true);
        }
    }

    public static SysShape getSelectedShape() {
        return selectedShape;
    }

    public static String getDataFileName() {
        String thisMoment = DateTimeFormatter.ofPattern("yyyy-MM-dd_'T'HH-mm-ss_X")
                              .withZone(ZoneOffset.UTC)
                              .format(Instant.now());
        // Based on the date/time, ID, name, etc., generate us a filename
        // for the data file.
        String name = "STTData_ID" + Player.id + "_";
        name += thisMoment;
        name += ".txt";
        return name;
    }

    public static String getDataSizeString(String str) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float numBytes = str.length();
        float numMb = numBytes/1024f/1024f; 
        str = "Data size: " + (int)numBytes + " bytes (" + df.format(numMb) + " mb)";
        return str;
    }

    public static String getDataSizeString() {
        return getDataSizeString(getPlayerData());
    }

    public static void recordAnswer(Question question, String answerText) {
        Answer answer = new Answer(question, answerText);
        answers.add(answer);
    }
    
    public static void recordButtonAction(Button btn, String screenName) {
        recordAction(Action.Type.Button, btn.getText(), screenName);
    }

    public static void recordButtonAction(MouseEvent event, String screenName) {
        Object o = event.getSource();
        if (o instanceof Button) {
            recordButtonAction((Button)o, screenName);
        }
    }

    public static void recordButtonAction(String btnName, String screenName) {
        recordAction(Action.Type.Button, btnName, screenName);
    }

    public static void recordAction(Action.Type type, String descr, String from) {
        Action action = new Action(type, descr, from);
        actions.add(action);
        //Utils.log(action.toString());
        //String data = getPlayerData();
        //Utils.log(getPlayerData());
    }

    public static void appendLine(StringBuilder builder, String str) {
        if (str != null && str != "")
            builder.append(str);
        builder.append("\r\n"); // We dont want system-independent as I want to read this on Windows
    }

    public static String getAnswerData() {
        StringBuilder result = new StringBuilder();
        // Now add all the responses to the questions
        for (Answer answer : Player.answers) {
            result.append(answer.toString());
        }
        return result.toString(); 
    }

    public static String getActionData() {
        StringBuilder result = new StringBuilder();
        // Now go through all the actions and write them to the string.
        for (Action action : Player.actions) {
            result.append(action.toString());
        }
        return result.toString(); 
    }

    public static String getScratchPadData() {
        return "\r\nScratchPad:\r\n" + Gos.scratchPadWindow.getText();
    }

    public static void copyDataToClipboard() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        Player.lastSavedData = Player.getPlayerData();
        content.putString(Player.lastSavedData);
        //content.putHtml("<b>Some</b> text");
        clipboard.setContent(content);
    }

    public static String getPlayerData() { 
        StringBuilder result = new StringBuilder();
        // Build the string that represents the file we want to send
        // back home as our all-important research data.
        //String nowLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX")
          //                    .format(Instant.now());
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        String nowLocal = ZonedDateTime.now().format(FORMATTER);

        String nowZulu = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss X")
                              .withZone(ZoneOffset.UTC)
                              .format(Instant.now());

        // Start with the meta-data like the id, name, email, etc.
        // Get some timestamps on there, might even help us figure out where people
        // are taking the test from without being intrusive.
        appendLine(result, "MS: " + System.currentTimeMillis());
        appendLine(result, "LT: " + nowLocal);
        appendLine(result, "ZT: " + nowZulu);
        appendLine(result, "ID: " + id);
        appendLine(result, "SubID: " + submittedId);
        appendLine(result, "Consent: " + contactConsent);
        appendLine(result, "Name: " + name);
        appendLine(result, "Email: " + email);
        appendLine(result, "PB: " + playedBefore);
        appendLine(result, "Times: " + timesPlayed);
        appendLine(result, "MAC: " + Utils.getMacAddress()); // Add in the MAC if we are allowed.
        appendLine(result, "Start: " + startTime);
        appendLine(result, "End: " + endTime);

        result.append(getAnswerData());
        result.append(getActionData());
        result.append(getScratchPadData());

        return result.toString(); 
    }

    //public static void setCurrentExercise(Exercise e) { currentExercise = e; }
    public static Exercise getCurrentExercise() { return currentExercise; }
    public static Task getCurrentTask() {
        if (currentExercise == null) {
            return null;
        }
        return currentExercise.getCurrentTask();
    }
    public static Question getCurrentQuestion() {
        if (currentExercise == null) {
            return null;
        }
        return currentExercise.getCurrentQuestion();
    }

    public static boolean inDemographicSurvey() {
        if (currentExercise.getId() == 0) {
            return true;
        }
        return false;
    }

    public static boolean inCareerSurvey() {
        if (currentExercise.getId() == 1) {
            return true;
        }
        return false;
    }

    public static Exercise setCurrentExercise(Exercise exe) {
        currentExercise = exe;
        // Set the bound value on the Player structure, so that
        // all of our bindings update on the screens
        if (currentExercise != null) {
            // 2 is the practice exercise.  If we are past that, we
            // aren't in practice mode anymore.
            if (currentExercise.getId() > 2) {
                m_bInPracticeMode = false;
            }
            else {
                m_bInPracticeMode = true;
            }
            setCurrentExerciseNumberTracker(currentExercise.getId());
            setCurrentExerciseDisplayIdTracker(currentExercise.getDisplayId());
            setCurrentExerciseNameTracker(currentExercise.getName());
            setCurrentExerciseTextTracker(currentExercise.getText());
        }
        else {
            setCurrentExerciseNumberTracker(0);
            setCurrentExerciseDisplayIdTracker(0);
            setCurrentExerciseNameTracker("");
            setCurrentExerciseTextTracker("");
        }
        updateTaskTrackers();
        return currentExercise;
    }

    // Update the various trackers for task
    private static void updateTaskTrackers() {
        if (currentExercise != null) {
            // Set the bound value on the Player structure, so that
            // all of our bindings update on the screens.
            setCurrentTaskNumberTracker(currentExercise.currentTaskNumber);

            Task task = currentExercise.getCurrentTask();
            if (task != null) {
                setCurrentTaskNameTracker(task.getName());
                setCurrentTaskTextTracker(task.getText());
            }
            else {
                setCurrentTaskNameTracker("");
                setCurrentTaskTextTracker("");
            }
        }
        else {
            setCurrentTaskNumberTracker(0);
            setCurrentTaskNameTracker("");
            setCurrentTaskTextTracker("");
        }
    }

    public static Exercise goToExercise(int num) {
        Exercise exe = null;
        if (num >= Data.exerciseList.size()) {
            exe = Data.exerciseList.get(Data.exerciseList.size()-1);
        }
        else {
            exe = Data.exerciseList.get(num);
        }
        return setCurrentExercise(exe);
    }

    public static Exercise nextExercise() {
        Exercise exe = null;
        if (currentExercise != null) {
            int nextNum = currentExercise.getId() + 1;
            if (nextNum >= Data.exerciseList.size()) {
                exe = null;
            }
            else {
                exe = Data.exerciseList.get(nextNum);
            }
        }
        return setCurrentExercise(exe);
    }

    public static Task getNextTask() {
        if (currentExercise == null) {
            return null;
        }
        return currentExercise.getNextTask();
    }

    public static Task nextTask() {
        if (currentExercise == null) {
            return null;
        }
        Task task = currentExercise.nextTask();
        updateTaskTrackers();
        return task;
    }
    
    public static Question nextQuestion() {
        if (currentExercise == null) {
            return null;
        }
        return currentExercise.nextQuestion();
    }
}