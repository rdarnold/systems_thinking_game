package gos;

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
    public static long getStartTime() { return startTime; }

    public static long endTime = 0;
    public static long getEndTime() { return endTime; }

    public static long lastTimeLogged = 0;
    public static String nowLocal = "";
    public static String nowZulu = "";

    // Start out in prac mode, if you're in prac mode, certain buttons
    // and controls aren't available.
    private static boolean m_bInPracticeMode = true;
    public static boolean inPracticeMode() { return m_bInPracticeMode; }

    // Did the player skip sections?  Only for testing and debugging
    private static boolean m_bSkipped = false;
    public static boolean getSkipped() { return m_bSkipped; }
    public static void setSkipped() { m_bSkipped = true; }

    // Has the player played current exercise already?  If so, the player 
    // is doing a replay and does not need to change or update answers to 
    // questions in the same exercise.
    public static boolean m_bReplaying = false;
    public static boolean getReplaying() { return m_bReplaying; }
    public static void setReplaying(boolean rep) { m_bReplaying = rep; }
    public static int m_numRetries = 0; // How many times has the player retried the current stage?
    public static int getRetries() { return m_numRetries; }
    public static void addRetry() { m_numRetries++; }
    public static void resetRetries() { m_numRetries = 0; }
    public static int m_fourShapesRetries = 0;  // How many times has player retried this stage?
    public static int m_chaosRetries= 0;

    // Info if the player wants to provide it.
    private static int id = 0;
    private static int submittedId = 0; // If they tried to submit their own ID we record that too
    private static int saveNum = 1; // How many times have we saved the data?  Increment each time so it's easy to see
    private static String name = "Anon";
    private static String email = "";
    private static int timesPlayed = 0;
    private static boolean contactConsent = false;
    private static boolean playedBefore = false;
    private static String macAddress = "";
    private static String osName = "";
    private static String JVMMemory = "";
    private static String javaVersion = "";
    private static String tutorialData = "";
    private static String scoreText = ""; // Only used for the data analyzer right now just so I can see what the score is

     // How much Systems Thinking exposure years?  This is something I rate based on background and experience provided.
    private static int stExposure = 0;
    
    public static int getId() { return id; }
    public static int getSubmittedId() { return submittedId; }
    public static String getName() { return name; }
    public static String getEmail() { return email; }
    public static int getTimesPlayed() { return timesPlayed; }
    public static boolean getContactConsent() { return contactConsent; }
    public static boolean getPlayedBefore() { return playedBefore; }
    public static String getMacAddress() { return macAddress; }
    public static String getTutorialData() { return tutorialData; }
    public static void appendTutorialData(String str) {
        tutorialData += str + "\r\n";
    }
    public static String getScoreText() { return scoreText; }

    public static int getSTExposure() { return stExposure; }
    public static void setSTExposure(int num) { stExposure = num; }

    public static void incSaveNum() { saveNum++; }
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
        String name = "STT_" + Player.getName() + "_" + Player.saveNum + "_ID" + Player.id + "_";
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

    public static int getIntAnswerForUID(int uID) {
        return Utils.tryParseInt(getFirstCharForAnswerUID(uID));
    }

    public static String getFirstCharForAnswerUID(int uID) {
        Answer ans = getAnswerForQuestionUID(uID);
        if (ans == null || ans.getStrAnswer() == null || ans.getStrAnswer().length() < 1) {
            return "";
        }
        return ans.getStrAnswer().substring(0, 1);
    }

    public static String getStrAnswerForQuestionUID(int uID) {
        Answer ans = getAnswerForQuestionUID(uID);
        if (ans == null) {
            return "";
        }
        return Utils.chomp(ans.getStrAnswer());
    }

    public static int getCodeAnswerForQuestionUID(int uID) {
        Answer ans = getAnswerForQuestionUID(uID);
        if (ans == null) {
            return -1;
        }
        String strAns = ans.getStrAnswer();

        // Now check the code, it's whatever is before the colon
        String code = strAns.substring(0, 1);
        if (strAns.substring(1, 2).equals(":") == false) {
            code += strAns.substring(1, 2);
        }
        return Utils.tryParseInt(code);
    }

    public static Answer getAnswerForQuestionUID(int uID) {
        // Go back from the end, because we want to return the "MOST RECENT"
        // answer for this UID (for now)
        for (int index = answers.size()-1; index >= 0; index--) {
            Answer ans = answers.get(index);
            if (ans.getQuestionUid() == uID) {
                return ans;
            }
        }
        return null;
    }

    public static boolean hasAnsweredQuestionsForExercise(int exId) {
        // Check to see if we have any answers for the passed in exercise/stage Id,
        // if so, we can say yes, the player has indeed answered the questions for this stage;
        // as we wouldn't check this while doing the answers, if any one answer for that stage
        // exists, the player has answered them all.
        for (Answer ans : Player.answers) {
            if (ans.getExerciseId() == exId) {
                return true;
            }
        }
        return false;
    }

    // Used to repopulate an answer field if one was already given
    public static Answer getPriorAnswer(Question question) {
        // Go backwards to find the most recent answer to this question, if there is one
        for (int i = Player.answers.size() - 1; i >= 0; i--) {
            Answer ans = Player.answers.get(i);
            if (ans.getQuestionId() == question.getId() &&
                ans.getExerciseId() == question.getExercise()) {
                return ans;
            }
        }
        return null;
    }

    public static boolean isDuplicateAnswer(Question question, String answerText) {
        // Go backwards from the end; if we find the id and it isn't the same, it's not
        // a duplicate, even though it may duplicate an answer even earlier than that - 
        // but we want to record that the player had an answer, changed, then changed
        // back later.
        for (int i = Player.answers.size() - 1; i >= 0; i--) {
            Answer ans = Player.answers.get(i);
            if (ans.getQuestionId() == question.getId()) {
                if (answerText.equals(ans.getStrAnswer()) == true) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static void recordAnswer(Question question, String answerText) {
        // So, we're going to say, if the player is recording the exact same answer
        // as the last time s/he answered this question, we're just not going to record it,
        // because it's moot - it's the same answer.  So look through the answer list to
        // find the last answer for this same question, and compare the answer - if it's different,
        // record it anew.
        if (isDuplicateAnswer(question, answerText) == true) {
            return;
        }
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

    public static String getScoreData() {
        StringBuilder result = new StringBuilder();
        for (Score score : Data.scores) {
            result.append(score.getExTaskString());
            result.append(": ");
            result.append(score.getScoreString());
            result.append("\r\n");
        }
        return "\r\nSCORE:\r\n" + result.toString(); 
    }

    public static int getTopScoreForStage(int stage) {
        int score1 = getScoreForStageRound(stage, 1);
        int score2 = getScoreForStageRound(stage, 2);

        if (score1 > score2) {
            return score1;
        }
        return score2;
    }

    public static int getScoreForStageRound(int stage, int round) {
        String str = getStrScoreForStageRound(stage, round);
        if (str == null || str.equals("") == true || str.length() < 1) {
            return -1;
        }
        return Utils.tryParseInt(str);
    }

    // Based on player UI, not internal structure,
    // so stage 1.1 is stage 1, round 1
    public static String getStrScoreForStageRound(int stage, int round) {
        // Looks like this:
        /*
        Stage 0.1: There is no score for the tutorial.
        Stage 1.1: You finished with 6 shapes.
        Stage 1.2: You finished with 25 shapes.
        Stage 2.1: You achieved level 0!
        Stage 2.2: You achieved level 22!
        */

        // getScoreText, NOT getScoreData which only works during the game, this is for display
        // in the excel file / data analyzer.  getScoreText loads the text from the player's
        // data file.
        String strScore = getScoreText();
        String find = "Stage " + stage + "." + round;
        int index = strScore.indexOf(find);
        if (index == -1) {
            return "";
        }

        // See if they maxed
        String maxScore = find + ": Wow, nice job";
        if (strScore.indexOf(maxScore) != -1) {
            return "50";
        }

        if (stage == 1) {
            index += "Stage 1.1: You finished with ".length();
        }
        else if (stage == 2) {
            index += "Stage 2.1: You achieved level ".length();
        }

        // We have the first number
        String score = "" + strScore.charAt(index);

        // Now move forward as long as each character is a number
        index++;
        while (Character.isDigit(strScore.charAt(index)) == true) {
            score += strScore.charAt(index);
            index++;
        }
        
        return score;
    }

    public static String getScratchPadData() {
        return "\r\nScratchPad:\r\n" + Gos.scratchPadWindow.getText() + "\r\n";
    }

    public static void copyDataToClipboard() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        Player.lastSavedData = Player.getPlayerData();
        content.putString(Player.lastSavedData);
        //content.putHtml("<b>Some</b> text");
        clipboard.setContent(content);
    }

    public static String saveDataToFile() {
        return Utils.saveFileToMyDocs(Constants.SAVED_DATA_FILE_NAME, Player.getPlayerData());
    }

    public static String getPlayerData() { 
        StringBuilder result = new StringBuilder();
        // Build the string that represents the file we want to send
        // back home as our all-important research data.
        //String nowLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX")
          //                    .format(Instant.now());
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        nowLocal = ZonedDateTime.now().format(FORMATTER);

        nowZulu = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss X")
                              .withZone(ZoneOffset.UTC)
                              .format(Instant.now());

        lastTimeLogged = System.currentTimeMillis();
        macAddress = Utils.getMacAddress();
        osName = Utils.getOSName();
        JVMMemory = Utils.getJVMMemoryMB();
        javaVersion = Utils.getJavaVersion();

        // Start with the meta-data like the id, name, email, etc.
        // Get some timestamps on there, might even help us figure out where people
        // are taking the test from without being intrusive.
        appendLine(result, "MS: " + lastTimeLogged);
        appendLine(result, "LT: " + nowLocal);
        appendLine(result, "ZT: " + nowZulu);
        appendLine(result, "ID: " + id);
        appendLine(result, "SubID: " + submittedId);
        appendLine(result, "Consent: " + contactConsent);
        appendLine(result, "Name: " + name);
        appendLine(result, "Email: " + email);
        appendLine(result, "PB: " + playedBefore);
        appendLine(result, "Times: " + timesPlayed);
        appendLine(result, "MAC: " + macAddress); // Add in the MAC if we are allowed.
        appendLine(result, "OS: " + osName); // Put in the OS if we were able to
        appendLine(result, "Mem: " + JVMMemory); // Max RAM in MB
        appendLine(result, "JVer: " + javaVersion);
        appendLine(result, "STXP: " + stExposure);
        appendLine(result, "Start: " + startTime);
        appendLine(result, "End: " + endTime);
        appendLine(result, "Tutorial: \r\n" + tutorialData + "EndTutorial");

        result.append(getAnswerData());
        result.append(getActionData());
        result.append(getScratchPadData());
        result.append(getScoreData());

        return result.toString(); 
    }

    public static boolean loadPlayerData(List<String> lines) {
        int i = 0;
        /*DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        String nowLocal = ZonedDateTime.now().format(FORMATTER);

        String nowZulu = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss X")
                              .withZone(ZoneOffset.UTC)
                              .format(Instant.now());

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
        result.append(getScratchPadData());*/

        // Load general data
        Utils.log("--------------------");
        Utils.log("Loading general data");
        i = loadGeneralData(lines, i);

        // Load answer data
        Utils.log("Loading answer data");
        i = loadAnswerData(lines, i);

        // Load actions
        Utils.log("Loading action data");
        i = loadActionData(lines, i);

        // Now load the scratch pad
        Utils.log("Loading notepad data");
        i = loadScratchPadData(lines, i);

        // Load scores
        Utils.log("Loading score data");
        i = loadScoreData(lines, i);

        //Utils.log(Player.getPlayerData());
        Utils.log("Loaded player " + Player.getName() + ", ID: " + Player.getId());
        return true;
    }

    
    // We found an ANS: which is start of an answer so now load the answer
    // and return the index of the last line of the answer whatever that was
    private static int loadAnswerFromString(List<String> lines, int start) {
        int i = start;

        List<String> loadLines = new ArrayList<String>();
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;

        // Now we just keep going until we hit @ETX: (or something else that terminates)
        String line = lines.get(i);
        while (line != null) {
            if (i >= lines.size()) {
                break;
            }
            line = lines.get(i);

            // Only more actions and the scratchpad come after actions right now.  that's just the way the file is set up.
            // It would be nice to future-proof this file and format such that it doesn't depend on the order of the contents.
            // But right now that would involve sending more data and I'm trying to keep it lean.
            if (line.length() >= ("@ETX").length() && line.substring(0, ("@ETX").length()).equals("@ETX") == true) {
                break;
            }
            // This shouldn't happen because if we hit an ANS: that means we just went past an @ETX
            if (line.length() >= ("ANS:").length() && line.substring(0, ("ANS:").length()).equals("ANS:") == true) {
                break;
            }
            loadLines.add(line);
            i++;
        }

        Answer ans = new Answer(loadLines);
        if (ans != null) {
            // We just do this to accomodate my initial data set which had absolute instead of relative
            // timestamps
            //ans.timestamp -= 1561517543573L;
            Player.answers.add(ans);
        }
        loadLines.clear();
        return i;
    }
    
    // We found an ACT: which is start of an action so now load the action
    // and return the index of the last line of the action whatever that was
    private static int loadActionFromString(List<String> lines, int start) {
        int i = start;

        List<String> loadLines = new ArrayList<String>();
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;
        loadLines.add(lines.get(i));
        i++;

        // Now we just keep going until we hit another ACT: (or something else that terminates) as the rest are parts of AD
        String line = lines.get(i);
        while (line != null) {
            if (i >= lines.size()) {
                break;
            }
            line = lines.get(i);

            // Only more actions and the scratchpad come after actions right now.  that's just the way the file is set up.
            // It would be nice to future-proof this file and format such that it doesn't depend on the order of the contents.
            // But right now that would involve sending more data and I'm trying to keep it lean.
            if (line.length() >= ("ACT:").length() && line.substring(0, ("ACT:").length()).equals("ACT:") == true) {
                break;
            }
            if (line.length() >= ("ScratchPad:").length() && line.substring(0, ("ScratchPad:").length()).equals("ScratchPad:") == true) {
                break;
            }
            loadLines.add(line);
            i++;
        }

        Action act = new Action(loadLines);
        if (act != null) {
            // We just do this to accomodate my initial data set which had absolute instead of relative
            // timestamps
            //act.timestamp -= 1561517543573L;
            Player.actions.add(act);
        }
        loadLines.clear();
        return i;
    }

    // This should match the default data at the top
    private static void resetGeneralData() {
        id = 0;
        submittedId = 0; // If they tried to submit their own ID we record that too
        saveNum = 1; // How many times have we saved the data?  Increment each time so it's easy to see
        name = "Anon";
        email = "";
        timesPlayed = 0;
        contactConsent = false;
        playedBefore = false;
        macAddress = "";
        osName = "";
        JVMMemory = "";
        javaVersion = "";
        tutorialData = "";
        scoreText = ""; // Only used for the data analyzer right now just so I can see what the score is
    
        stExposure = 0;
    }

    private static int loadGeneralData(List<String> lines, int start_index) {
        String str;
        int i = start_index;

        resetGeneralData();
        
        // So right now it's just quick and dirty since we know the order
        str = lines.get(i);
        lastTimeLogged = Utils.tryParseLong(str.substring(("MS: ").length(), str.length()));
        //appendLine(result, "MS: " + lastTimeLogged);
        i++;  
        str = lines.get(i);
        nowLocal = str.substring(("LT: ").length(), str.length());
        //appendLine(result, "LT: " + nowLocal);
        i++;  
        str = lines.get(i);
        nowZulu = str.substring(("ZT: ").length(), str.length());
        //appendLine(result, "ZT: " + nowZulu);
        i++;  
        str = lines.get(i);
        Player.setId(Utils.tryParseInt(str.substring(("ID: ").length(), str.length())));
        //appendLine(result, "ID: " + id);
        i++;
        str = lines.get(i);
        submittedId = Utils.tryParseInt(str.substring(("SubID: ").length(), str.length()));
        //appendLine(result, "SubID: " + submittedId);
        i++;
        str = lines.get(i);
        contactConsent = Boolean.parseBoolean(str.substring(("Consent: ").length(), str.length()));
        //appendLine(result, "Consent: " + contactConsent);
        i++;
        str = lines.get(i);
        name = str.substring(("Name: ").length(), str.length());
        //appendLine(result, "Name: " + name);
        i++;
        str = lines.get(i);
        email = str.substring(("Email: ").length(), str.length());
        //appendLine(result, "Email: " + email);
        i++;
        str = lines.get(i);
        playedBefore = Boolean.parseBoolean(str.substring(("PB: ").length(), str.length()));
        //appendLine(result, "PB: " + playedBefore);
        i++;
        str = lines.get(i);
        timesPlayed = Utils.tryParseInt(str.substring(("Times: ").length(), str.length()));
        //appendLine(result, "Times: " + timesPlayed);
        i++;
        str = lines.get(i);
        macAddress = str.substring(("MAC: ").length(), str.length());
        //appendLine(result, "MAC: " + Utils.getMacAddress()); // Add in the MAC if we are allowed.
        // Now check OS; older results might not have it; we need a better way to do this
        i++;
        str = lines.get(i);
        if (str.length() < ("OS: ").length() || str.substring(0, ("OS: ").length()).equals("OS: ") == false) {
            // They didn't have the OS string so just move on
            i--;
        }
        else {
            osName = str.substring(("OS: ").length(), str.length());
        }

        // Do the same with max heap memory for the JVM
        i++;
        str = lines.get(i);
        if (str.length() < ("Mem: ").length() || str.substring(0, ("Mem: ").length()).equals("Mem: ") == false) {
            // They didn't have the string so just move on
            i--;
        }
        else {
            JVMMemory = str.substring(("Mem: ").length(), str.length());
        }
        
        // Do the same with Java Version
        i++;
        str = lines.get(i);
        if (str.length() < ("JVer: ").length() || str.substring(0, ("JVer: ").length()).equals("JVer: ") == false) {
            // They didn't have the string so just move on
            i--;
        }
        else {
            javaVersion = str.substring(("JVer: ").length(), str.length());
        }

        // Now check ST field; older results might not have it; we need a better way to do this
        i++;
        str = lines.get(i);
        if (str.length() < ("STXP: ").length() || str.substring(0, ("STXP: ").length()).equals("STXP: ") == false) {
            // They didn't have the string so just move on
            i--;
        }
        else {
            stExposure = Utils.tryParseInt(str.substring(("STXP: ").length(), str.length()));
        }

        i++;
        str = lines.get(i);
        startTime = Utils.tryParseLong(str.substring(("Start: ").length(), str.length()));
        //appendLine(result, "Start: " + startTime);
        i++;
        str = lines.get(i);
        endTime = Utils.tryParseLong(str.substring(("End: ").length(), str.length()));
        //appendLine(result, "End: " + endTime);
        i++;

        // Now parse tutorial data if we have any
        str = lines.get(i);
        if (str.substring(0, "Tutorial".length()).equals("Tutorial")) {
            i++;
            str = lines.get(i);
            while (str.substring(0, "EndTutorial".length()).equals("EndTutorial") == false) {
                // Now load in the tutorial data
                tutorialData += str + "\r\n";
                i++;
                str = lines.get(i);
            }
            i++;
        }
        
        return i;
    }

    private static int loadAnswerData(List<String> lines, int start_index) {
        Player.answers = new ArrayList<Answer>();
        int i = 0;
        for (i = start_index; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null || line.length() < 4) {
                continue;
            }

            if (line.substring(0, 4).equals("ANS:")) {
                i = loadAnswerFromString(lines, i);
                i--; // Because we're gonna add to it in a second
            }
            if (line.length() >= ("ACT:").length() && line.substring(0, ("ACT:").length()).equals("ACT:") == true) {
                return i;
            }
        }
        return i;
    }

    private static int loadActionData(List<String> lines, int start_index) {
        Player.actions = new ArrayList<Action>();
        int i = 0;
        for (i = start_index; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null || line.length() < 4) {
                continue;
            }

            if (line.substring(0, 4).equals("ACT:")) {
                i = loadActionFromString(lines, i);
                i--; // Because we're gonna add to it in a second
            }
            if (line.length() >= ("ScratchPad:").length() && line.substring(0, ("ScratchPad:").length()).equals("ScratchPad:") == true) {
                return i;
            }
        }
        return i;
    }

    private static int loadScratchPadData(List<String> lines, int start_index) {
        // Effectively reset it
        Gos.scratchPadWindow.setText("");

        int i = 0;
        for (i = start_index; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) {
                continue;
            }

            if (line.length() >= ("ScratchPad:").length() && line.substring(0, ("ScratchPad:").length()).equals("ScratchPad:")) {
                i = loadScratchPadFromString(lines, i);
                i--; // Because we're gonna add to it when the loop continues
            }
            if (line.length() >= ("SCORE:").length() && line.substring(0, ("SCORE:").length()).equals("SCORE:") == true) {
                return i;
            }
        }
        return i;
    }

    private static int loadScoreData(List<String> lines, int start_index) {
        // Reset the score data
        Player.scoreText = "";

        int i = 0;
        for (i = start_index; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) {
                continue;
            }

            if (line.length() >= ("SCORE:").length() && line.substring(0, ("SCORE:").length()).equals("SCORE:")) {
                i = loadScoreFromString(lines, i);
                i--; // Because we're gonna add to it when the loop continues
            }
        }
        return i;
    }

    private static int loadScoreFromString(List<String> lines, int start) {
        int i = start + 1;

        if (i >= lines.size()) {
            return i;
        }

        List<String> loadLines = new ArrayList<String>();

        // Now we just keep going until we hit the end
        try {
            String line = lines.get(i);
            while (line != null) {
                if (i >= lines.size()) {
                    break;
                }
                line = lines.get(i);
                loadLines.add(line);
                i++;
            }
        }
        catch (Exception e) {
            Utils.log("Exception in SCORE for player " + Player.getName() + ", ID: " + Player.getId());
            Utils.log(e.toString());
            return i;
        }

        // Now we have loadLines which is what we want to load
        int n = 0;
        String str = "";
        while (n < loadLines.size()) {
            str += loadLines.get(n) + "\r\n";
            n++;
        }
        Player.scoreText = str;
        loadLines.clear();
        return i;
    }

    private static int loadScratchPadFromString(List<String> lines, int start) {
        int i = start + 1;

        List<String> loadLines = new ArrayList<String>();

        // Now we just keep going until we hit the end or the scores
        String line = lines.get(i);
        while (line != null) {
            if (i >= lines.size()) {
                break;
            }
            line = lines.get(i);
            if (line.length() >= ("SCORE:").length() && line.substring(0, ("SCORE:").length()).equals("SCORE:")) {
                break;
            }
            loadLines.add(line);
            i++;
        }

        // Now we have loadLines which is what we want to load
        int n = 0;
        String strSP = "";
        while (n < loadLines.size()) {
            strSP += loadLines.get(n) + "\r\n";
            n++;
        }
        Gos.scratchPadWindow.setText(strSP);
        loadLines.clear();
        return i;
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
    public static void updateTaskTrackers() {
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

    
    public static Task previousTask() {
        if (currentExercise == null) {
            return null;
        }
        Task task = currentExercise.previousTask();
        updateTaskTrackers();
        return task;
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

    public static Question previousQuestion() {
        if (currentExercise == null) {
            return null;
        }
        return currentExercise.previousQuestion();
    }

    public static void resetQuestions() {
        if (currentExercise == null) {
            return;
        }
        currentExercise.resetQuestions();
    }
}