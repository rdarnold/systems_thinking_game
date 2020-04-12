package gos;

public class Score {
    private String strExTask = "";
    private String strExTaskExtended = "";
    private String strScore = "";

    private int exId = -1;
    private int exDisplayId = -1;
    private int taskId = -1;

    // These are basically transient
    private Exercise ex;
    private Task task;
    private Simulator sim;

    public String getScoreString() { return strScore; }
    public String getExTaskString() { return strExTask; }
    public String getExTaskExtendedString() { return strExTaskExtended; }
    public String toString() { return strScore; }

    // Get the exercise and task IDs that this score is for
    public int getExId() { return exId; }
    public int getExDisplayId() { return exDisplayId; }
    public int getTaskId() { return taskId; }

    public Score() { }
    public Score(Simulator s) { 
        get(s);
    }

    // From a specific system state, based on current exercise
    // and task, calculate a score
    public void get(Simulator s) {
        get(s, s.getNumberLiveShapes());
    }

    // If we don't want to use the shapes in the current sim, we can pass it a number
    // of shapes. This is useful if the state of the sim may have changed since our score
    // was generated
    public void get(Simulator s, int numShapes) {
        sim = s;
        ex = Player.getCurrentExercise();
        task = ex.getCurrentTask();
        if (task == null) {
            strScore = "There is no score for this task.";
            return;
        }

        exId = ex.getId();
        exDisplayId = ex.getDisplayId();
        taskId = task.getId();

        strExTask = "Stage " + exDisplayId + "." + (taskId + 1);
        strExTaskExtended = strExTask + ": " + ex.getName() + ", " + task.getName();

        //strExTask = "Stage: " + ex.getName() + ", Task: " + task.getName();
        strScore = "";
        //strScore = "Stage " + exId + ", Task " + taskId + ": No score formula found!";

        switch (exId) {
            case 0:
            case 1:
            case 5:
            case 6:
                // These are all surveys and questionairres and practice
                break;
            case 2:
                strScore = "There is no score for the tutorial.";
                break;
            case 3:
                createExerciseScore1(numShapes);
                break;
            case 4:
                createExerciseScore2();
                break;
            /*case 5:
                createExerciseScore3createExerciseScore3();
                break;*/
            default:
                break;
        }

        // We don't hold onto these, just have them around for
        // convenience.
        ex = null;
        task = null;
        sim = null;
    }

    private void createExerciseScore1(int numShapes) {
        switch (taskId) {
            case 0: 
            case 1:{
                // Over 5 turns, try to maximum the number of spinning shapes in the system.  
                // The more shapes you can make (or preserve), the better.
                if (numShapes >= 50) {
                    strScore = "Wow, nice job!\r\nYou finished with all " + numShapes + " shapes.";
                }
                else {
                    strScore = "You finished with " + numShapes + " shapes.";
                }
                break;
            }
            /*case 1: {
                // Over 3 turns, try to maximize the total area across all spinning shapes.  
                // The more area, the better.
                int num = 0;
                for (SysShape shape : sim.getShapes()) {
                    num += shape.getSize();
                }
                strScore = "You concluded the task with " + num + " units of area.";
                break;
            }*/
        }
    }

    // Maybe this should also only go up to 50
    private void createExerciseScore2() {
        switch (taskId) {
            case 0: 
            case 1: {
                // Your own success level.
                int num = Player.getSelectedShape().getSuccess();
                strScore = "You achieved level " + num + "!";
                break;
            }
        }
    }
}