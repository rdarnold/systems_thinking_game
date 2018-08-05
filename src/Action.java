package gos;

import java.util.ArrayList;
import java.util.EnumSet;

// Basically, an Action is something the player has done. It's our way of tracking
// usage patterns.  Theoretically we should be able to play back a list of actions
// exactly as they are recorded and reproduce the player's experience exactly
// provided we also have the associated Turns, as there is randomness in each scenario.
public class Action {

    public static enum Type {
        Button(0),          // Any button press
        SubmitExpChange(1), // Change set for an experiment
        SubmitChange(2),    // Change set for actually changing the system
        SubmitVarRating(3), // Submit the ratings for the different variables
        TitledPane(4);      // Titled pane was hit, like help/info buttons in the help screen

        private final int value;
        private Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Action.Type actionType;
    public long timestamp; // Every acion has a timestamp so we know when it occurred.
    public String desc; // Description, like what button was clicked for a button type
    public String fromScreen; // What screen was this done on?

    public Action(Action.Type aType, String strDescription, String strScreen) {
        actionType = aType;
        desc = strDescription;
        fromScreen = strScreen;
        setTimeToNow(); // Restamp it later if needs be if we pre-created this.
    }

    public void setTimeToNow() {
        timestamp = System.currentTimeMillis();
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