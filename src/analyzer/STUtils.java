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

// This is essentially like a static class in C#
public final class STUtils {

    private STUtils() { // private constructor
    }

    public static int calc(STSkills skill) {
        switch (skill) {
            case ExploreMultiplePerspectives:
                return calcExploreMultiplePerspectives();
            case ConsiderWholesAndParts:
                return calcConsiderWholesAndParts();
            case EffectivelyRespondToUncertaintyAndAmbiguity:
                return calcEffectivelyRespondToUncertaintyAndAmbiguity();
            case ConsiderIssuesAppropriately:
                return calcConsiderIssuesAppropriately();
            case UseMentalModelingAndAbstraction:
                return calcUseMentalModelingAndAbstraction();
    
            // Structure
            case RecognizeSystems:
                return calcRecognizeSystems();
            case MaintainBoundaries:
                return calcMaintainBoundaries();
            case DifferentiateAndQuantifyElements:
                return calcDifferentiateAndQuantifyElements();
    
            // Content
            case IdentifyRelationships:
                return calcIdentifyRelationships();
            case CharacterizeRelationships:
                return calcCharacterizeRelationships();
            case IdentifyFeedbackLoops:
                return calcIdentifyFeedbackLoops();
            case CharacterizeFeedbackLoops:
                return calcCharacterizeFeedbackLoops();
    
            // Behavior
            case DescribePastSystemBehavior:
                return calcDescribePastSystemBehavior();
            case PredictFutureSystemBehavior:
                return calcPredictFutureSystemBehavior();
            case RespondToChangesOverTime:
                return calcRespondToChangesOverTime();
            case UseLeveragePoints:
                return calcUseLeveragePoints();
        }
        return -1;
    }


    // Maybe I have to analyze the results myself at first, and then those results are used
    // as parametrs that can be used to design a new assessment policy.  Like I've "discovered"
    // the patterns of how good sytsems thinkers interact with my game and isolated those from
    // non-good ones, so those patterns can be used to assess in the future.

    /* The SCORE in general:
    50% of EVERY skill score is just the overall game performance.  Game performance affects all
    skills and all should be used during the game in theory, so the initial cut says how well
    you do in general in the game, how well you score, is fully half of your individual score in
    every skill.  The theory is that these skills are interrelated and dependent on each other so
    you must be exercising every skill to some degree if you're able to do well.  This is weird in that
    if you do shitty in the game, you still might be really good in some particular skill.  But this
    method isn't the best way to evaluate individual skills, it's a holistic method that gives a general
    idea of how good somoene is at Systems Thinking on the whole, and then attempts to sub-divide that
    into some esimate of sub-skills that isn't intended to be 100% accurate but is just intended to
    try to figure out which skills should be targeted.  Perhaps even the rating states this - it's not
    giving you a skill rating in each individual skill, but it's suggesting to you which ones you in particular
    are better or worse at.

    The remaining 50% is specific to each skill depending on the questions, patterns of play,
    ratings, and strategies.

    Honestly I'm just not convinced that a little sim like this can give you a comprehensive score 
    on your systems thinking ability.  It is good for complex problem solving though.  Well I guess we can
    see how it relates to the scores that everyone self-evaluated, and if it isn't accurate, I can determine
    where the issues are and what could be improved upon in a future iteration / what was lacking, or where
    people "went wrong" in the sim.

    BOTTOM LINE:
    - It's kind of hard to extrapolate specific skills from the holistic systemic behavior JUST by observing
    how someone plays the game (unless you ALREADY know what the good patterns are)
    - You really need to ask them some questions about the system and how they played in order to get insight
    into their thinking processes, you can't ONLY look at what they did and then sort of reverse-engineer
    how it was done.
    - THEREFORE, in this DISCOVERY phase we need to ask questions, we can't ONLY look at playing patterns
    UNTIL we know what good patterns look like BY ASKING those questions first
    */


    /*
    Some people didn't read the instructions fully - there was "too much information" to take in - but I
    would argue that many systems are like this; they have a lot of information that needs to be digested,
    and to not spend the time to take it in is to "not consider issues appropriately"
    a systems thinker would digest this information

    Some people mucked around and played with experiment a lot but didn't really read the instructions,
    maybe correlates to learning?  But did quite poorly in the game itself.  

    Publishing field with a lot of computer experience did very poorly on the first exercise.
    Very good grades and very smart person, but did very poorly on the exercise when had no systems thinking
    experience.  Also she did not try to get a high score in the final round of Chaos, rather she had other
    goals in mind.  She had a good direct understanding of how the system worked but some of the more indirect
    associations seemed to elude her.

    Actually many people did not even have the "grit" to complete it, which I would argue is shitty systems
    thinking

    Sara - "I didn't need to observe because the scenarios played out quite slowly. And because the visuals 
    were so basic, it was quite easy to see what was happening" (spent only 3 minutes observing)

    "Well, I can tell you now (spoiler alert) that the first half I was very careful and I was trying to figure 
    out what each of the options did. And in the second half (chaos) I threw in the towel and just went for it"

    */

    ////////////////////////
    //// Mindset Domain ////
    ////////////////////////
    public static int calcExploreMultiplePerspectives() {
        // Usual things you'd look at with this skill:
        // - Looking from the point of view of different stakeholders
        // - Considering the problem from the POV of different fields like environmental, political

        // Unfortunately this skill doesn't lend well to the environment presented
        // by my simulation but there are some ways we might be able to try to infer
        // this and match it against the self-evaluation:
        // - Has the player tried every different variable change (this is a stretch?)
        // - Honestly maybe this type of sim is just not good for evaluating this skill.
        // - The problem is, this system is bound by the fact that it's a self-contained sim.
        // can't really think of external perspectives when none exist.  What about meta data,
        // like what distractions did the people have, or ask what perspectives they considered
        // when playing the game like, how would it affect the researchers, the world at large,
        // is it too much time and would affect their family, personal like what stake did they
        // have in getting a good score. 

        // UPDATE 
        // Actually the idea is to examine a problem from a lot of different angles.  these angles
        // will depend on the system.  So they could be different people, or different domains, or
        // in the case of some Systems Thinking definitions, things like "Operational Thinking" 
        // and "Blah Blah Thinking" are potentially different perspectives; it just depends on the kind
        // of problem and I'm not proscribing what those are but only to look FOR them and be aware they
        // exist.

        return 0;
    }

    public static int calcConsiderWholesAndParts() {
        // Tweak both overarching and specific parameters
        return 0;
    }

    public static int calcEffectivelyRespondToUncertaintyAndAmbiguity() {
        // When doing poorly, makes choices that turn the system back around to doing well.  Or, perhaps
        // when doing poorly, does experiments and then uses the results to make the system then do better.
        return 0;
    }

    public static int calcConsiderIssuesAppropriately() {
        // Time spent doing experiments and observations and number of them prior to jumping in
        return 0;
    }

    public static int calcUseMentalModelingAndAbstraction() {
        // Describes the strategy after the exercise, and the rationale for variable choices
        return 0;
    }

    ////////////////////////
    /// Structure Domain ///
    ////////////////////////
    public static int calcRecognizeSystems() {
        // It'll have to be based on the questions, like, what kind of problem is this?  and one of
        // the answers is "systemic"  but if you do poorly in general on the sim we can say perhaps
        // you didn't recognize that it was systemic.  So if you for example did a lot of the same
        // manipulation of just the gravity well, and never the overarching parameters, then you didn't
        // realize it was systemic and you didn't do anything to fix that.
        return 0;
    }

    public static int calcMaintainBoundaries() {
        // Does the player touch the correct things, and rate the variables correctly, do the 
        // choices actually affect the system and aren't peripheral to the system
        return 0;
    }

    public static int calcDifferentiateAndQuantifyElements() {
        // The questions about the system, and also the degree to which different parameters are tweaked
        // I need to put in specific questions about this.
        return 0;
    }

    
    ////////////////////////
    //// Content Domain ////
    ////////////////////////
    public static int calcIdentifyRelationships() {
        // Questions about the system, and in general how well the person did (or does this give an overall score to all areas?)
        // I need to put in specific questions about this.  What is related to what?
        return 0;
    }

    public static int calcCharacterizeRelationships() {
        // Questions about the system, and how the strengths of the variables are rated
        // I need to put in specific questions about this.  What is related to what, and HOW MUCH?
        return 0;
    }

    public static int calcIdentifyFeedbackLoops() {
        // Did the person successfully create the emergent behavior of the dots
        // I need to put in specific questions about this.  What is related to what?
        return 0;
    }

    public static int calcCharacterizeFeedbackLoops() {
        // Did the person successfully create the emergent behavior of the dots and use it to score well?
        // I need to put in specific questions about this.  What is related to what, and HOW MUCH?
        return 0;
    }

    /////////////////////////
    //// Behavior Domain ////
    /////////////////////////
    public static int calcDescribePastSystemBehavior() {
        // Questions about system behavior
        return 0;
    }
    
    public static int calcPredictFutureSystemBehavior() {
        // Questions about what would happen if xyz
        return 0;
    }

    public static int calcRespondToChangesOverTime() {
        // Does the subject change methods over time as the system state changes?  Or keeps trying the same thing?
        return 0;
    }

    public static int calcUseLeveragePoints() {
        // Uses paradigm, uses shape spin, Laws (gravity) effectively?
        return 0;
    }
}