package gos;

import java.util.EnumSet;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// This is essentially like a static class in C#
public final class Constants {
    private Constants () { // private constructor
    }

    public static void init() {
    }
    
    //one second in nanoseconds
    public static final long ONE_SECOND_IN_NANOSECONDS = 1000000000;

    public static final String RES_LOAD_PATH = "/res/";

    public static final String SAVED_DATA_FILE_NAME = "SystemThinkingGame_Data.txt";

    // If we want to change the date/time after which we download results, change this:
    public static final String DL_START_DATE_TIME = "2020-08-18" + "T12:00:00";

    public static final int BUTTON_WIDTH = 120;
    public static final int NODE_SIZE = 125;

    public static final int SIM_WIDTH = 590;
    public static final int SIM_HEIGHT = 525;
 
    public static final int SIM_CENTER_X = SIM_WIDTH/2;
    public static final int SIM_CENTER_Y = SIM_HEIGHT/2;

    public static final String VERSION_NUMBER_STRING = "1.04";

    // Some key strings for the system
    public static final String SHAPE_KEY_STRING = "@S";
    public static final String DROP_KEY_STRING = "@R";
    public static final String SPIKE_KEY_STRING = "@K";
    public static final String PATCH_KEY_STRING = "@E";
    public static final String WELL_KEY_STRING = "@G";

    public static enum ResourceType {
        Matter, Energy;
    }
    
    public static enum Dir {
        Top, Bottom, Left, Right;
    }

    public static enum GravityRules {
        Normal(0), Reverse(1), Off(2);

        private int _value;

        GravityRules(int Value) {
            this._value = Value;
        }

        public int getValue() {
                return _value;
        }

        private static GravityRules[] cachedValues = null;
        public static GravityRules fromInt(int i) {
            if (GravityRules.cachedValues == null) {
                GravityRules.cachedValues = GravityRules.values();
            }
            return GravityRules.cachedValues[i];
        }
    }

    public static enum GrowthRules {
        Normal(0, "Normal"), 
        NoGrowth(1, "No Growth");

        private int _value;
        private String _label;

        GrowthRules(int value, String label) {
            this._value = value;
            this._label = label;
        }

        public int getValue() {
                return _value;
        }
        public String toString() {
                return _label;
        }
        public String getLabel() {
                return _label;
        }

        private static GrowthRules[] cachedValues = null;
        public static GrowthRules fromInt(int i) {
            if (GrowthRules.cachedValues == null) {
                GrowthRules.cachedValues = GrowthRules.values();
            }
            return GrowthRules.cachedValues[i];
        }
    }

    public static enum ArmorRules {
        Normal(0, "Normal"), 
        NoMatch(1, "No Matching"), 
        NoArmor(2, "No Armor");

        private int _value;
        private String _label;

        ArmorRules(int value, String label) {
            this._value = value;
            this._label = label;
        }

        public int getValue() {
                return _value;
        }
        public String toString() {
                return _label;
        }
        public String getLabel() {
                return _label;
        }

        private static ArmorRules[] cachedValues = null;
        public static ArmorRules fromInt(int i) {
            if (ArmorRules.cachedValues == null) {
                ArmorRules.cachedValues = ArmorRules.values();
            }
            return ArmorRules.cachedValues[i];
        }
    }

    public static enum Paradigms {
        // Either they compete, they cooperate, or they ignore each other.
        Competitive(0), Cooperative(1), Independent(2);

        // In competitive mode, shapes steal and give size
        // In cooperative mode, shapes do not steal and give size,
        //    but when they receive size from rain and earth, they
        //    share size between their neighbors evenly
        // In independent mode, shapes do not steal, give, or share size.

        private int _value;

        Paradigms(int Value) {
            this._value = Value;
        }

        public int getValue() {
                return _value;
        }

        private static Paradigms[] cachedValues = null;
        public static Paradigms fromInt(int i) {
            if (Paradigms.cachedValues == null) {
                Paradigms.cachedValues = Paradigms.values();
            }
            return Paradigms.cachedValues[i];
        }
    }

    // Seven primary culture traits:
    // learned behaviors, transmission of information, symbolism, 
    // flexibility, integration, ethnocentrism and adaptation
    // Or how to describe the network of values and beliefs held by
    // something?  That's what this is.
    public enum CultureTraits {
        Friendly, Patient, Industrious, Flexible, Ethnocentric;
    }
}