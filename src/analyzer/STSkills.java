package gos.analyzer;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.control.Button;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.io.*; 
import java.util.Enumeration;

import gos.gui.*;
import gos.*;

// Calculates how good a player is in the various systems thinking skills

// Phrase it as follows (rather than saying YOUR score, it's the score of your WORK 
//   which may not be reflective of your real ability but it's what you SHOWED here): 
// The set of actions taken during the simulation are assessed at the following levels:

// The systems thinking skill enum copy list:
/*
ExploreMultiplePerspectives
ConsiderWholesAndParts
EffectivelyRespondToUncertaintyAndAmbiguity
ConsiderIssuesAppropriately
UseMentalModelingAndAbstraction

// Structure
RecognizeSystems
MaintainBoundaries
DifferentiateAndQuantifyElements

// Content
IdentifyRelationships
CharacterizeRelationships
IdentifyFeedbackLoops
CharacterizeFeedbackLoops

// Behavior
DescribePastSystemBehavior
PredictFutureSystemBehavior
ResponseToChangesOverTime
UseLeveragePoints
*/

// The various systems thinking skills
public enum STSkills {

    // Mindset
    ExploreMultiplePerspectives(0, 1, 1, "Explore Multiple Perspectives"), 
    ConsiderWholesAndParts (1, 1, 2, "Consider Wholes and Parts "),
    EffectivelyRespondToUncertaintyAndAmbiguity (2, 1, 3, "Effectively Respond to Uncertainty and Ambiguity"),
    ConsiderIssuesAppropriately(3, 1, 4, "Consider Issues Appropriately"),
    UseMentalModelingAndAbstraction(4, 1, 5, "Use Mental Modeling and Abstraction"),

    // Structure
    RecognizeSystems(5, 2, 1, "Recognize Systems"),
    MaintainBoundaries (6, 2, 2, "Maintain Boundaries "),
    DifferentiateAndQuantifyElements(7, 2, 3, "Differentiate and Quantify Elements"),

    // Content
    IdentifyRelationships(8, 3, 1, "Identify Relationships"),
    CharacterizeRelationships(9, 3, 2, "Characterize Relationships"),
    IdentifyFeedbackLoops(10, 3, 3, "Identify Feedback Loops"),
    CharacterizeFeedbackLoops(11, 3, 4, "Characterize Feedback Loops"),

    // Behavior
    DescribePastSystemBehavior(12, 4, 1, "Describe Past System Behavior"),
    PredictFutureSystemBehavior(13, 4, 2, "Predict Future System Behavior"),
    RespondToChangesOverTime(14, 4, 3, "Response to Changes over Time"),
    UseLeveragePoints(15, 4, 4, "Use Leverage Points");

    private int _value;
    private int _domain;
    private int _num_in_domain;
    private String _label;

    STSkills(int value, int domain, int num_in_domain, String label) {
        this._value = value;
        this._domain = domain;
        this._num_in_domain = num_in_domain;
        this._label = label;
    }

    public int getValue() {
            return _value;
    }
    public int getDomain() {
        return _domain;
    }
    public int getNumInDomain() {
        return _num_in_domain;
    }

    public String toStringAsNum() {
        return "" + _domain + "." + _num_in_domain;
    }

    public String toString() {
            return _label;
    }
    public String getLabel() {
            return _label;
    }

    private static STSkills[] cachedValues = null;
    public static STSkills fromInt(int i) {
        if (STSkills.cachedValues == null) {
            STSkills.cachedValues = STSkills.values();
        }
        return STSkills.cachedValues[i];
    }
}