package gos;

import java.util.ArrayList;
import java.util.EnumSet;

// Basically a holder of static data that should be accessible by everyone.
public final class Data {
    private Data () { // private constructor
    }

    public static ArrayList<Score> scores;
    public static ArrayList<Info> infoList;
    public static ArrayList<Task> taskList;
    public static ArrayList<Question> questionList;
    public static ArrayList<Exercise> exerciseList;
    public static Values startingValues;
    public static Values currentValues;

    private static XmlProcessor xmlProcessor = new XmlProcessor();

    public static int framesPerSecond = 60;
    public static int getFramesPerTurn() {
        return currentValues.turnSeconds * framesPerSecond;
    }

    public static void load() {
        scores = new ArrayList<Score>();
        infoList = new ArrayList<Info>();
        taskList = new ArrayList<Task>();
        questionList = new ArrayList<Question>();
        exerciseList = new ArrayList<Exercise>();
        startingValues = new Values();
        currentValues = new Values();

        xmlProcessor.loadInfoListXML(Constants.RES_LOAD_PATH + "info.xml", infoList);
        xmlProcessor.loadExerciseListXML(Constants.RES_LOAD_PATH + "exercises.xml", exerciseList);
        xmlProcessor.loadTaskListXML(Constants.RES_LOAD_PATH + "tasks.xml", taskList);
        xmlProcessor.loadQuestionListXML(Constants.RES_LOAD_PATH + "questions.xml", questionList);
        
        startingValues = xmlProcessor.loadValues(Constants.RES_LOAD_PATH + "starting_values.xml");
        currentValues = xmlProcessor.loadValues(Constants.RES_LOAD_PATH + "current_values.xml");

        if (infoList.size() <= 0) {
            throw new AssertionError("Info list failed to load.");
        }
        if (exerciseList.size() <= 0) {
            throw new AssertionError("Exercises failed to load.");
        }
        if (taskList.size() <= 0) {
            throw new AssertionError("Tasks failed to load.");
        }
        if (questionList.size() <= 0) {
            throw new AssertionError("Questions failed to load.");
        }

        processTasks();
        processQuestions();

        Player.setCurrentExercise(exerciseList.get(0));
    }

    // This is not actually used yet, but if we were hooked up to a separate program
    // we could save out the values to disk and then they could be read by the other
    // program, like a separate GUI.  So it allows the simulator to run separately from
    // the GUI.
    public static void loadCurrentValues() {
        currentValues = xmlProcessor.loadValues(Constants.RES_LOAD_PATH + "current_values.xml");
    }

    public static void saveCurrentValues() {
        xmlProcessor.saveValues(Constants.RES_LOAD_PATH + "current_values.xml", currentValues);
    }

    private static void processTasks() {
        // Do anything we need to do here like set IDs.
        /*int id = 0;
        for (Task task : taskList) {
            task.setId(id);
            id++;
        }*/

        // Set all the questions onto the exercises.
        for (Exercise e : exerciseList) {
            e.processTasks(taskList);
        }
    }

    private static void processQuestions() {
        // Do anything we need to do here like set IDs.
        /*int id = 0;
        for (Question item : questionList) {
            item.setId(id);
            id++;
        }*/

        // Set all the questions onto the exercises.
        for (Exercise e : exerciseList) {
            e.processQuestions(questionList);
        }
    }

    public static Question getQuestion(int id, int exNum) {
        for (Question q : questionList) {
            if (q.getId() == id && exNum == q.getExercise()) {
                return q;
            }
        }
        return null;
    }

    public static Info getInfoByName(String strName) {
        for (Info item : infoList) {
            if (item.getName().equals(strName)) {
                return item;
            }
        }
        return null;
    }
}