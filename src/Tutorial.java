package gos;

import java.util.ArrayList;
import java.util.EnumSet;

// Holder of static information for the Tutorial part of the game
public final class Tutorial {
    private Tutorial () { // private constructor
    }

    public static void setTextForTutorialTurn(int turnNum) {
        /*
        1: Move the Gravity Well to the bottom right corner
        2: Switch your shape's spin direction
        3: Turn rain off
        4: Change Paradigm to Cooperative
        5: Turn Growth to "no Growth"
        */

        String str = "";
        switch (turnNum) {
            case 0:
                str = "The black dot in the center with the faint blue border is the Gravity Well.  Please click it and drag it to the bottom " +
                    "right corner of the screen, then click the Submit button.";
                break;
            case 1:
                str =  "Switch your shape's Spin Direction to the opposite direction (clockwise or counter-clockwise) using the panel on the left side of the screen, then click the Submit button.";
                break;
            case 2:
                str =  "Turn the Rain variable as low as possible (to the left) using the slider on the top part of the screen, then click the Submit button.";
                break;
            case 3:
                str =  "Change the Paradigm to Cooperative using the panel on the top part of the screen, then click the Submit button.";
                break;
            case 4:
                str =  "Change the Growth to No Growth using the panel on the top part of the screen, then click the Submit button.";
                break;
            default:
                str =  "The tutorial has concluded.  Please click Change System or Submit to proceed with the game.";
                break;
        }

        Player.setCurrentTaskTextTracker(str);
    }

    public static String getTutorialCorrectForTurn(int turnNum) {
        String str = "TUTORIAL TURN " + (turnNum + 1) + ": ";
        // So record basically how correct the tutorial action was according to what we asked the player to do,
        // and embed it in the data so we can quickly see if someone followed the tutorial properly or
        // not without a ton of analysis
        switch (turnNum) {
            case 0:
                // Check position of the gravity well based off of where it starts; it should be lower and to the right
                int x1 = Data.startingValues.gravityWellCenterX;
                int y1 = Data.startingValues.gravityWellCenterY;
                GravityWell well = Gos.sim.getGravityWell();
                int x2 = (int)well.getCenterX();
                int y2 = (int)well.getCenterY();
                if (x1 == x2 && y1 == y2) {
                    str += "BAD, Gravity Well was not moved";
                }
                else if (x1 == x2 ) {
                    str += "BAD, Gravity Well was not moved on X axis";
                }
                else if (y1 == y2 ) {
                    str += "BAD, Gravity Well was not moved on Y axis";
                }
                else if (x2 > x1 && y2 > y1) {
                    str += "GOOD, Gravity Well was moved to bottom right corner";
                }
                else {
                    str += "BAD, Gravity Well was moved to wrong place";
                }

                // now just include the coordinates regardless
                str += " (X:" + x2 + ",Y:" + y2 +")";
                break;
            case 1:
                // Check shape spin direction
                break;
            case 2:
                // Check rain slider to make sure it's low
                break;
            case 3:
                // Check Paradigm for Cooperative
                break;
            case 4:
                // Check Growth for No Growth
                break;
            default:
                str += "NO CORRECTNESS FOUND";
                break;
        }
        return str;
    }
}