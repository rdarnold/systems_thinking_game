package gos;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

// Basically, an Action is something the player has done. It's our way of tracking
// usage patterns.  Theoretically we should be able to play back a list of actions
// exactly as they are recorded and reproduce the player's experience exactly
// provided we also have the associated Turns, as there is randomness in each scenario.
public class Action {

    public static enum Type {
        Button(0, "Button"),              // Any button press
        SubmitExpChange(1, "SubmitExpChange"),  // Change set for an experiment
        SubmitChange(2, "SubmitChange"),       // Change set for actually changing the system
        SubmitVarRating(3, "SubmitVarRating"),  // Submit the ratings for the different variables
        TitledPane(4, "TitledPane");            // Titled pane was hit, like help/info buttons in the help screen

        private final int value;
        private final String string;
        private Type(int value, String name) {
            this.value = value;
            string = name;
        }

        // If we want the int value of the enum
        public int getValue() {
            return value;
        }

        // the toString just returns the given name
        public String toString() {
            return string;
        }

        // If we want to get the enum value by passing an int
        private static Type[] values = null;
        public static Type fromInt(int i) {
            if (Type.values == null) {
                Type.values = Type.values();
            }
            return Type.values[i];
        }
    }

    // This tells us when the game started so we can calculate internally "when" this action
    // was taken within the sim
    /*public long timeZero = 0; 
    public void setTimeZero(long time) {
        timeZero = time;
    }*/

    public Action.Type actionType;
    public long timestamp; // Every acion has a timestamp so we know when it occurred in relation to start of game
    public String desc; // Description, like what button was clicked for a button type
    public String fromScreen; // What screen was this done on?

    // These support the tableView
    public String getDesc() {
        return desc;
    }

    public String getFromScreen() {
        return fromScreen;
    }

    public String getStrAction() {
        return actionType.toString();
    }

    public String getStrTime() {
        // So diff is in ms.  We want to print basically the
        // hours/seconds/minutes 00:00:00 since this person started
        // the game.
        int seconds = (int) (timestamp / 1000) % 60 ;
        int minutes = (int) ((timestamp / (1000*60)) % 60);
        int hours   = (int) ((timestamp / (1000*60*60)) % 24);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    

    public Action(Action.Type aType, String strDescription, String strScreen) {
        actionType = aType;
        desc = strDescription;
        fromScreen = strScreen;
        setTimeToNow(); // Restamp it later if needs be if we pre-created this.
    }

    // Actually the time is since the game started, not since "forever"
    public void setTimeToNow() {
        timestamp = System.currentTimeMillis() - Player.startTime;
    }

    // The loadFrom string should have all of the action info it in already, ordered
    // correctly
    public Action(List<String> fromLines) {
        fromString(fromLines);
    }

    public boolean fromString(List<String> fromLines) {
        boolean success = true;
        String line = null;

        // First line should be ACT (type)
        line = fromLines.get(0);
        line = line.substring(5, line.length());
        actionType = Type.fromInt(Utils.tryParseInt(line));

        // Now AT (time)
        line = fromLines.get(1);
        line = line.substring(4, line.length());
        timestamp = Utils.tryParseLong(line);

        // Now AS (screen)
        line = fromLines.get(2);
        line = line.substring(4, line.length());
        fromScreen = line;

        // Now AD (Desc)
        line = fromLines.get(3);
        line = line.substring(4, line.length());

        // Now we just keep going until the end to load the rest of desc
        desc = line;

        int i = 4;
        while (i < fromLines.size()) {
            desc += "\r\n";
            desc += fromLines.get(i);
            i++;
        }

        Utils.log(toString());

        return success;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ACT: " + actionType.getValue());
        sb.append("\r\n");
        sb.append("AT: " + timestamp);
        sb.append("\r\n");
        sb.append("AS: " + fromScreen);
        sb.append("\r\n");
        sb.append("AD: " + desc);
        sb.append("\r\n");
        return sb.toString();
    }
}