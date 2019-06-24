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

        strExTask = "Scenario " + exDisplayId + "." + (taskId + 1);
        strExTaskExtended = strExTask + ": " + ex.getName() + ", " + task.getName();

        //strExTask = "Scenario: " + ex.getName() + ", Task: " + task.getName();
        strScore = "";
        //strScore = "Scenario " + exId + ", Task " + taskId + ": No score formula found!";

        switch (exId) {
            case 0:
            case 1:
            case 5:
            case 6:
                // These are all surveys and questionairres and practice
                break;
            case 2:
                strScore = "There is no score for the practice scenario.";
                break;
            case 3:
                createExerciseScore1();
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

    private void createExerciseScore1() {
        switch (taskId) {
            case 0: 
            case 1:{
                // Over 5 turns, try to maximum the number of spinning shapes in the system.  
                // The more shapes you can make (or preserve), the better.
                int num = sim.getNumberLiveShapes();
                if (num == 50) {
                    strScore = "Wow, nice job!\r\nYou finished with all " + num + " shapes.";
                }
                else {
                    strScore = "You finished with " + num + " shapes.";
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

    /*private void createExerciseScore2() {
        switch (taskId) {
            case 0: 
            case 1:{
                // Maximize space between shapes.
                if (sim.getNumberLiveShapes() <= 1) {
                    strScore = "You need at least two shapes for them to be away from each other.";
                    return;
                }

                // Ideally you only have two shapes and they're in the furthest corners at this point.
                // Or four at the four corners.
                int totalDistance = 0;
                int num = 0;
                for (SysShape shape : sim.getShapes()) {
                    if (shape.isDead() == true) 
                        continue;
                    for (SysShape otherShape : sim.getShapes()) {
                        if (otherShape.isDead() == true)
                            continue;
                        if (shape == otherShape || shape.equals(otherShape)) {
                            continue;
                        }
                        num++;
                        totalDistance += Utils.calcDistance(shape, otherShape);
                    }
                }
                int avg = totalDistance / num;
                strScore = "You finished with " + num + " average pixel distance between shapes.";
                break;
            }
            case 2:
            case 3: {
                // Over 20 turns, try to make as many triangles as you can.
                int num = 0;
                for (SysShape shape : sim.getShapes()) {
                    if (shape.isDead() == true) 
                        continue;
                    if (shape.getNumCorners() == 5) {
                        num++;
                    }
                }
                strScore = "You finished with " + num + " pentagons.";
                break;
            }
        }
    }*/

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