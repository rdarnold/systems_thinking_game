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

    public static int getOverallScoreForSkillStr(String str) {
        return 0;
    }

    public static int calc(STSkills skill) {
        switch (skill) {
            case ExploreMultiplePerspectives:
                return getOverallScoreForSkillStr(calcExploreMultiplePerspectives());
            case ConsiderWholesAndParts:
                return getOverallScoreForSkillStr(calcConsiderWholesAndParts());
            case EffectivelyRespondToUncertaintyAndAmbiguity:
                return getOverallScoreForSkillStr(calcEffectivelyRespondToUncertaintyAndAmbiguity());
            case ConsiderIssuesAppropriately:
                return getOverallScoreForSkillStr(calcConsiderIssuesAppropriately());
            case UseMentalModelingAndAbstraction:
                return getOverallScoreForSkillStr(calcUseMentalModelingAndAbstraction());
    
            // Structure
            case RecognizeSystems:
                return getOverallScoreForSkillStr(calcRecognizeSystems());
            case MaintainBoundaries:
                return getOverallScoreForSkillStr(calcMaintainBoundaries());
            case DifferentiateAndQuantifyElements:
                return getOverallScoreForSkillStr(calcDifferentiateAndQuantifyElements());
    
            // Content
            case IdentifyRelationships:
                return getOverallScoreForSkillStr(calcIdentifyRelationships());
            case CharacterizeRelationships:
                return getOverallScoreForSkillStr(calcCharacterizeRelationships());
            case IdentifyFeedbackLoops:
                return getOverallScoreForSkillStr(calcIdentifyFeedbackLoops());
            case CharacterizeFeedbackLoops:
                return getOverallScoreForSkillStr(calcCharacterizeFeedbackLoops());
    
            // Behavior
            case DescribePastSystemBehavior:
                return getOverallScoreForSkillStr(calcDescribePastSystemBehavior());
            case PredictFutureSystemBehavior:
                return getOverallScoreForSkillStr(calcPredictFutureSystemBehavior());
            case RespondToChangesOverTime:
                return getOverallScoreForSkillStr(calcRespondToChangesOverTime());
            case UseLeveragePoints:
                return getOverallScoreForSkillStr(calcUseLeveragePoints());
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

    Sara also said about teh self-assessment:
    Also, another note. Those last 16 questions are phrased in such a way, that I didn't know how to assess my own skills. I felt they lacked context.
    Because how would I know how to assess my own performance when learning a new system? Generally people don't think about that, 
    they don't question their thought process, they just do it. So even that data might not reflect a person's abilities accurately
    I think it definitely made me think about my approach towards a system
    But I felt, because of how the questions were phrased, that I actually marked myself down
    Because I wasn't aware of whether I would actually do what said options suggested, because I don't ever reflect on how I interact with a system
    (Is she thinking of systems as ALL things connected?)
    And I also think that, because it was so vague, it was difficult for me to pin point a definition. 🤔 And this is where my lack of systems thinking kicks in. 
    I just didn't know how to rate myself. How my practical skills equated to what option.

    People without an introduction to Systems Thinking tended to say that they thought it didn't assess their
    ability to think systemitcally, while those familiar with Systems Thinking who did better on the test said
    that it did. (percentages)  There could be a number of reasons for this.  For starting, people who did poorly
    out of self-pride may rail on the test itself saying it's invalid - a very human response.  Secondly they 
    may not necessarily undersatnd the meaning of thinking systemically, third and perhaps most likely, they
    didn't do well on the test so they may not have grasped the systemic intentions or nature of the test itself,
    thus thinking it didn't assess their skills to think systemically.  Finally, it's possible the test is just
    invalid and actually doesn't systemically assess skills.  However based on the feedback from the known systems
    thinking experts, the latter does not appear to be the case.  
    I could have given examples of systems and said when you're thinking about this particular thing, what do you do?
    But that would have extended the test to being too long for the identifed window of 1 hour.


    GOOD THOUGHTS:
    Self-assesment validation:  People doing the assessment had comments like:
    which is why I found your questions at the end a bit like... why am I doing this again?
    i.e. it captures the same skills as what was done during the sim
    */

    ////////////////////////
    //// Mindset Domain ////
    ////////////////////////
    public static String calcExploreMultiplePerspectives() {
        String strScore = "";
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

        return strScore;
    }

    public static String calcConsiderWholesAndParts() {

        // The string basically "tells the story" of the score and where it came from,
        // and how each variable affected it.  It's pre-pended with a single number
        // which is the actual score.
        String strScore = "";

        /*
        -	Holistic thinking - did they use a strategy that flowed from one turn to the next 
            (keeping the “big picture” in mind), or was each turn a "new thing"
        -	Does the player consider the entire stage as a system?  Or do they focus on specific turns?  
            They need to consider the entire stage holistically and determine their goals based on that.
        -	Using a coordinated approach that considers the effects of each choice at each time period.
        -	Adjusts both the Shape direction and speed, and the overall parameters (shows an appreciation 
            for wholes and parts but not necessarily recognition that whole is other than parts or the philosophical utilization of this idea)
        */
       
        /* 
        0: Overall strategy is not coordinated.  Temporal aspects not considered.  Does not make choices that 
        are intended to flow into each other from one turn to the next. 0/3 answers correct on 32.

        1: Shows desire to coordinate an overall strategy for a stage.  Strategy is not effective or is erratic.  
        Makes effective choices on some turns but ineffective choices on others. 1/3 answers correct on 32.

        2: Coordinates an overall strategy that considers the entire stage and the differing changes that 
        need to be made each turn.  Strategy achieves success but is not the ideal approach.  Makes choices 
        that influence each other from turn to turn but the choices are not the ideal.  1-2 / 3 answers correct on 32.

        3: Coordinates an overall strategy that considers the entire stage and the differing changes that 
        need to be made each turn.  Strategy achieves success but is not the ideal approach.  Makes effective 
        choices that influence each other from turn to turn and feed into each other effectively. 2-3 / 3 answers correct on 32.

        4: Coordinates an overall strategy that considers the entire stage and the differing changes that 
        need to be made each turn.  Strategy succeeds in achieving 50 shapes in under 5 turns.  3/3 answers correct on 32.
        */

        double score = 0;

        // Start with answers correct on question 32
        Answer ans = Player.getAnswerForQuestionUID(32);
        if (ans == null) {
            return "Not enough data";
        }
        int numCorrect = ans.getTotalCorrect();
        int numPartial = ans.getTotalPartial();
        int numWrong = ans.getTotalIncorrect();

        // Our total is the total correct - the total wrong
        // But minus half for each partial
        score = numCorrect - numWrong - (numPartial/2);

        strScore += "Score: " + score + ", for Q32: " + numCorrect + " correct, " + numWrong + "wrong\r\n";

        // Now check what we did with the strategy and other related variables,
        // count round 2 of both of the stages equally
        // Remember this doesn't need to be perfect, we're just proving that we can get measures like this

        return returnStr(score, strScore);
    }

    public static String calcEffectivelyRespondToUncertaintyAndAmbiguity() {
        String strScore = "";
        // When doing poorly, makes choices that turn the system back around to doing well.  Or, perhaps
        // when doing poorly, does experiments and then uses the results to make the system then do better.
        return strScore;
    }

    public static String calcConsiderIssuesAppropriately() {
        String strScore = "";
        // Time spent doing experiments and observations and number of them prior to jumping in
        // Does not get frustrated and just "give up" during play; continues to try hard
        return strScore;
    }
 
    public static String calcUseMentalModelingAndAbstraction() {
        String strScore = "";
        // Describes the strategy after the exercise, and the rationale for variable choices
        return strScore;
    }

    ////////////////////////
    /// Structure Domain ///
    ////////////////////////
    public static String calcRecognizeSystems() {
        String strScore = "";
        // It'll have to be based on the questions, like, what kind of problem is this?  and one of
        // the answers is "systemic"  but if you do poorly in general on the sim we can say perhaps
        // you didn't recognize that it was systemic.  So if you for example did a lot of the same
        // manipulation of just the gravity well, and never the overarching parameters, then you didn't
        // realize it was systemic and you didn't do anything to fix that.
        return strScore;
    }

    public static String calcMaintainBoundaries() {
        String strScore = "";
        // Does the player touch the correct things, and rate the variables correctly, do the 
        // choices actually affect the system and aren't peripheral to the system
        return strScore;
    }

    public static String calcDifferentiateAndQuantifyElements() {
        String strScore = "";
        // The questions about the system, and also the degree to which different parameters are tweaked
        // I need to put in specific questions about this.
        return strScore;
    }

    
    ////////////////////////
    //// Content Domain ////
    ////////////////////////
    public static String calcIdentifyRelationships() {
        String strScore = "";
        // Questions about the system, and in general how well the person did (or does this give an overall score to all areas?)
        // I need to put in specific questions about this.  What is related to what?
        return strScore;
    }

    public static String calcCharacterizeRelationships() {
        String strScore = "";
        // Questions about the system, and how the strengths of the variables are rated
        // I need to put in specific questions about this.  What is related to what, and HOW MUCH?
        return strScore;
    }

    public static String calcIdentifyFeedbackLoops() {
        String strScore = "";
        // Did the person successfully create the emergent behavior of the dots
        // I need to put in specific questions about this.  What is related to what?
        return strScore;
    }

    public static String calcCharacterizeFeedbackLoops() {
        String strScore = "";
        // Did the person successfully create the emergent behavior of the dots and use it to score well?
        // I need to put in specific questions about this.  What is related to what, and HOW MUCH?
        return strScore;
    }

    /////////////////////////
    //// Behavior Domain ////
    /////////////////////////
    public static String calcDescribePastSystemBehavior() {
        String strScore = "";
        // Questions about system behavior
        return strScore;
    }
    
    public static String calcPredictFutureSystemBehavior() {
        String strScore = "";
        // Questions about what would happen if xyz
        return strScore;
    }

    public static String calcRespondToChangesOverTime() {
        String strScore = "";
        // Does the subject change methods over time as the system state changes?  Or keeps trying the same thing?
        return strScore;
    }

    public static String calcUseLeveragePoints() {
        String strScore = "";
        // Uses paradigm, uses shape spin, Laws (gravity) effectively?
        return strScore;
    }

    public static String returnStr(double score, String strScore) {
        return "" + Utils.round(score, 2) + strScore;
    }

    public static String calcStrategy() {
        String str = "";

        // Figure out what someone's general strategy was, but provide information on how we came to that
        // conclusion.

        /* IDEAL:
        •	Turn 1:
            o	Turn Growth to “No Growth”
            o	Move Gravity Well to bottom hemisphere but not right in the corners
            o	Turn Rain Rate to max
        •	Turn 2:
            o	Turn Rain Rate to min
        •	Turn 3:
            o	Turn Growth to “Growth”
        •	Turn 4, 5:
            o	Leave Rain Rate at min
            o	Move Gravity Well to general area of red ball storm
        •	Within first 3 turns:
            o	Turn Paradigm to “Cooperative”
        */

        /* Some things to consider from Whole and Parts:
        Overall strategy is not coordinated.  Temporal aspects not considered.  Does not make choices that 
        are intended to flow into each other from one turn to the next.

        1: Shows desire to coordinate an overall strategy for a stage.  Strategy is not effective or is erratic.  
        Makes effective choices on some turns but ineffective choices on others. 

        2: Coordinates an overall strategy that considers the entire stage and the differing changes that 
        need to be made each turn.  Strategy achieves success but is not the ideal approach.  Makes choices 
        that influence each other from turn to turn but the choices are not the ideal.  

        3: Coordinates an overall strategy that considers the entire stage and the differing changes that 
        need to be made each turn.  Strategy achieves success but is not the ideal approach.  Makes effective 
        choices that influence each other from turn to turn and feed into each other effectively. 

        4: Coordinates an overall strategy that considers the entire stage and the differing changes that 
        need to be made each turn.  Strategy succeeds in achieving 50 shapes in under 5 turns.
        */

        // So, let's determine what we CAN figure out about the person's strategy, and assign some overall 
        // quantitative measures from here, and then we can use those to apply to the various skills as
        // is fitting

        return str;
    }
}