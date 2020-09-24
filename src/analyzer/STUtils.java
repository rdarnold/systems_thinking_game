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

    // Pass it a string like "32 28 17" and it'll return the sum of correct, partial, and incorrect
    // answers for those questions (C P I) as in 4 1 12
    public static String getCorrectPartialIncorrectForAnswerSet(String strAnswerSet) {
        String[] questions = strAnswerSet.split(" ");
        Answer ans = null;
        
        int numCorrect = 0;
        int numPartial = 0;
        int numWrong = 0;
        for (int i = 0; i < questions.length; i++) {
            int uid = Utils.tryParseInt(questions[i]);
            ans = Player.getAnswerForQuestionUID(uid);
            numCorrect += ans.getTotalCorrect();
            numPartial += ans.getTotalPartial();
            numWrong += ans.getTotalIncorrect();
        }

        return "" + numCorrect + " " + numPartial + " " + numWrong;
    }

    // Simply sum up the question correctness for each skill
    // we return it as a string so we can do it all in one go then we parse it out later
    public static String getCorrectPartialIncorrectForSkill(int skillDomain, int skillNum) {
        String str = "0 0 0";
        switch (skillDomain) {
            case 1: {
                switch (skillNum) {
                    case 2: // 1.2 wholes and parts
                        // 32
                        return getCorrectPartialIncorrectForAnswerSet("32");
                    case 3: // 1.3  Effectively Respond to Uncertainty and Ambiguity
                        break;
                    case 4: // 1.4  Consider Issues Appropriately
                        break;
                }
                break;
            }
            case 2: {
                switch (skillNum) {
                    case 2: // 2.2  Define and Maintain Boundaries
                        // 27, 28, 31, 32
                        return getCorrectPartialIncorrectForAnswerSet("27 28 31 32");
                    case 3: // 2.3  Differentiate and Quantify Elements
                        // 17, 20, 22, 27, 28, 29, 30
                        return getCorrectPartialIncorrectForAnswerSet("17 20 22 27 28 29 30");
                }
                break;
            }
            case 3: {
                switch (skillNum) {
                    case 1: // 3.1  Identify Relationships
                        // 18, 21, 27, 28, 31, 32, 
                        return getCorrectPartialIncorrectForAnswerSet("18 21 27 28 31 32");
                    case 2: // 3.2  Characterize Relationships
                        // 18, 21
                        return getCorrectPartialIncorrectForAnswerSet("18 21");
                    case 3: // 3.3  Identify Feedback Loops
                        // 19, 22, 26, 27
                        return getCorrectPartialIncorrectForAnswerSet("19 22 26 27");
                    case 4: // 3.4  Characterize Feedback Loops
                        // 19, 22, 27
                        return getCorrectPartialIncorrectForAnswerSet("19 22 27");
                }
                break;
            }
            case 4: {
                switch (skillNum) {
                    case 2: // 4.2  Predict Future System Behavior
                        // 26, 27, 30
                        return getCorrectPartialIncorrectForAnswerSet("26 27 30");
                    case 3: // 4.3  Respond to Changes over Time
                        break;
                    case 4: // 4.4  Use Leverage Points to Produce Effects
                        break;
                }
                break;
            }
        }

        return str;
    }
    
    public static int getCorrectForSkill(int skillDomain, int skillNum) {
        String strSum = getCorrectPartialIncorrectForSkill(skillDomain, skillNum);
        String[] vals = strSum.split(" ");
        return Utils.tryParseInt(vals[0]);
    }

    public static int getPartialForSkill(int skillDomain, int skillNum) {
        String strSum = getCorrectPartialIncorrectForSkill(skillDomain, skillNum);
        String[] vals = strSum.split(" ");
        return Utils.tryParseInt(vals[1]);

    }
    public static int getIncorrectForSkill(int skillDomain, int skillNum) {
        String strSum = getCorrectPartialIncorrectForSkill(skillDomain, skillNum);
        String[] vals = strSum.split(" ");
        return Utils.tryParseInt(vals[2]);
    }


    // So we need to be able to load up the system at any point in any stage based on the player
    // data. 
    public static boolean loadSystemAtStageRoundTurn(int stageNum, int roundNum, int turnNum) {
        Action action = AssessmentData.findSubmitChangeActionForStageRoundTurn(stageNum, roundNum, turnNum);
        if (action == null) {
            return false;
        }

        // "false" is to make it AFTER not BEFORE; we want AFTER because we need to see
        // the changes that the player made, otherwise we won't see the changed variables
        SystemSnapshot snap = AssessmentData.createSnapshotFromSubmitAction(action, false);
        if (snap == null) {
            return false;
        }

        // Then we restore it to the sim
        snap.restore(Gos.sim);
        return true;
    }

    // So now, calc each "data point" and then I can put a chart together showing which
    // skill scores are affected by which data points.  These are ALL booleans and are
    // designed to be booleans, they're like "question answers" in that they can be either
    // correct or not.  
    public static int STAGE_FOUR_SHAPES = 3;
    public static int STAGE_CHAOS = 4;
    public static int MAX_TURNS_FOUR_SHAPES = 5;
    public static int MAX_TURNS_CHAOS = 10;
    public static int ROUND_1 = 0;
    public static int ROUND_2 = 1;

    // Did they turn up rain rate and change growth (while rain was turned up) to decrease spikes
    // and set up a red ball storm?  
    public static boolean dp1_rainAndGrowth(int stg, int rnd, int maxTurns) {
        // Check to see if growth was changed to no growth at any time, and if so, during that
        // time, was rain rate also turned up.
        int turn = 0;
        for (turn = 0; turn < maxTurns; turn++) {
            if (loadSystemAtStageRoundTurn(stg, rnd, turn) == true) {
                if (Data.currentValues.growthRules == Constants.GrowthRules.NoGrowth.getValue()) {
                    if (Data.currentValues.rainRate > 0.8) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // We only check the 2nd round of each stage, assuming the first is practice
    // This is generally true across the board, much scoring is based off of round 2
    public static boolean dp1_rainAndGrowth_stg1() {
        return dp1_rainAndGrowth(STAGE_FOUR_SHAPES, ROUND_2, MAX_TURNS_FOUR_SHAPES);
    }
    public static boolean dp1_rainAndGrowth_stg2() {
        return dp1_rainAndGrowth(STAGE_CHAOS, ROUND_2, MAX_TURNS_CHAOS);
    }

    // Did they “block” the screen at any point so that new spike formation was
    // generally prevented?  This could be through a shape line or through tweaking
    // the variables to reduce spike formation but generally speaking, you stopped
    // new spikes from forming effectively for the remainder of the stage.
    public static boolean dp2_stoppedSpikes(int stg, int rnd, int maxTurns) {
        // So basically just check if we have a very limited number of spikes,
        // if, after the second turn, we've ever been able to get spikes 
        // below let's say 10, which should
        // happen in the case of both creating a red ball storm and in the
        // case of blocking with a line
        int turn = 0;
        for (turn = 2; turn < maxTurns; turn++) {
            if (loadSystemAtStageRoundTurn(stg, rnd, turn) == true) {
                if (Gos.sim.spikes.size() <= 10) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean dp2_stoppedSpikes_stg1() {
        return dp2_stoppedSpikes(STAGE_FOUR_SHAPES, ROUND_2, MAX_TURNS_FOUR_SHAPES);
    }
    public static boolean dp2_stoppedSpikes_stg2() {
        return dp2_stoppedSpikes(STAGE_CHAOS, ROUND_2, MAX_TURNS_CHAOS);
    }

    // Did they successfully change the ratio of red balls to spikes such that red balls
    // are at least double the number of spikes (and at least 50 red balls exist)
    public static boolean dp3_ballSpikeRatio(int stg, int rnd, int maxTurns) {
        // First, we need at least 50 red balls.  Then, make sure there are 
        // less than half number of spikes as there are red balls
        // TODO check this later to make sure this is reasonable and isn't giving
        // people score when they shouldn't have it, but I think 50+ red balls
        // should make sense
        int turn = 0;
        for (turn = 0; turn < maxTurns; turn++) {
            if (loadSystemAtStageRoundTurn(stg, rnd, turn) == true) {
                int rb = Gos.sim.patches.size();
                if (rb >= 50) {
                    if (Gos.sim.spikes.size() < (rb/2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean dp3_ballSpikeRatio_stg1() {
        return dp3_ballSpikeRatio(STAGE_FOUR_SHAPES, ROUND_2, MAX_TURNS_FOUR_SHAPES);
    }
    public static boolean dp3_ballSpikeRatio_stg2() {
        return dp3_ballSpikeRatio(STAGE_CHAOS, ROUND_2, MAX_TURNS_CHAOS);
    }

    // Did they pull their shapes towards the "center of mass" of red balls within
    // the scenario?   Maybe I don't care about this right now; there are too many
    // red ball things
    /*public static boolean dp4_pullTowardsBalls(int stg, int rnd, int maxTurns) {
        // Calculate a local center of red balls and see if the gravity well is within
        // that general area.
        // Actually even easier - just look at their gravity well location and see
        // if it has a lot of red balls around it.  So, in the 25% of the game area
        // surrounding the gravity well, does it contain more than 50% of the red
        // balls and are there are least 20 red balls in the game.
        int turn = 0;
        for (turn = 0; turn < maxTurns; turn++) {
            if (loadSystemAtStageRoundTurn(stg, rnd, turn) == true) {
                int rb = Gos.sim.patches.size();
                if (rb >= 20) {
                    
                }
            }
        }
        return false;
    }
    public static boolean dp4_pullTowardsBalls_stg1() {
        return dp4_pullTowardsBalls(STAGE_FOUR_SHAPES, ROUND_2, MAX_TURNS_FOUR_SHAPES);
    }
    public static boolean dp4_pullTowardsBalls_stg2() {
        return dp4_pullTowardsBalls(STAGE_CHAOS, ROUND_2, MAX_TURNS_CHAOS);
    }*/

    // Did their score improve from round to round?
    public static boolean dp5_scoreImproved(int stgNum) {
        String strScore1 = Player.getStrScoreForStageRound(stgNum, 1);
        String strScore2 = Player.getStrScoreForStageRound(stgNum, 2);
        if (strScore1 == null || strScore1.length() <= 0 || strScore2 == null || strScore2.length() <= 0) {
            return false;
        }
        int score1 = Utils.tryParseInt(strScore1);
        int score2 = Utils.tryParseInt(strScore2);
        if (score1 < 0 || score2 < 0) {
            return false;
        }
        // Can't improve if they maxed score... so we'll say they get this point
        // if they maxed it on the first try
        if (score1 >= 50 || score2 > score1) {
            return true;
        }
        return false;
    }
    // In this case, stage 1 is 1 and stage 2 is 2, it's not zero-based
    public static boolean dp5_scoreImproved_stg1() {
        return dp5_scoreImproved(1);
    }
    public static boolean dp5_scoreImproved_stg2() {
        return dp5_scoreImproved(2);
    }


    // Did they increase the number of red balls as the game proceeded? 
    // Or did we capture this with the ball/spike ratio already?
    public static boolean dp6() {
        return false;
    }

    // Did they switch their shape to turn clockwise and keep it there after they changed it 
    // (did they end the stage with it clockwise)?  This only pertains to Chaos stage
    // since all shapes will eventually eat other ones in 4 shapes if they're not clockwise
    public static boolean dp7_clockwise() {
        // So look through until we find the turn where we flip it clockwise, then
        // make sure we don't flip it back.
        int turn = 0;
        boolean spinDir = false;
        for (turn = 0; turn < MAX_TURNS_CHAOS; turn++) {
            if (loadSystemAtStageRoundTurn(STAGE_CHAOS, ROUND_2, turn) == true) {
                SysShape shape = Player.getSelectedShape();
                spinDir = shape.getSpin();
            }
            else {
                break;
            }
        }
        return spinDir;
    }

    // Did the player increase spin speed of own shape to max while spinning clockwise
    // in Chaos stage?
    public static boolean dp7_spinSpeedMaxWhileClockwise() {
        // So look through until we find turns where it's clockwise, then see
        // if we max it, and make sure we don't flip it back or unmax it
        // (or at least, it's in the proper configuration at the turn's end)
        int turn = 0;
        boolean spinDir = false;
        boolean maxSpeed = false;
        for (turn = 0; turn < MAX_TURNS_CHAOS; turn++) {
            if (loadSystemAtStageRoundTurn(STAGE_CHAOS, ROUND_2, turn) == true) {
                SysShape shape = Player.getSelectedShape();
                spinDir = shape.getSpin();
                maxSpeed = false;
                if (spinDir == true && shape.getSpinSpeedPercent() > 90) {
                    maxSpeed = true;
                }
            }
            else {
                break;
            }
        }
        return (spinDir && maxSpeed);
    }

    // Did they switch paradigm to cooperative on the chaos stage?    
    public static boolean dp8_paradigmCoop() {
        int turn = 0;
        for (turn = 0; turn < MAX_TURNS_CHAOS; turn++) {
            if (loadSystemAtStageRoundTurn(STAGE_CHAOS, ROUND_2, turn) == true) {
                if (Data.currentValues.paradigm == Constants.Paradigms.Cooperative.getValue()) {
                    return true;
                }
            }
            else {
                break;
            }
        }
        return false;
    }

    // Did the player observe at least one “Play All” on Four Shapes (NOT the tutorial) 
    // before starting the main game?   
    public static boolean dp9_playAllFourShapes() {
        Action action = AssessmentData.findButtonActionForStageRoundTurn(3, 0, 0, "Play All");
        if (action != null) {
            return true;
        }
        return false;
    }

    // Did the player go back and observe (Play All) again to see the new stuff on the Chaos stage?    
    public static boolean dp10_playAllChaos() {
        Action action = AssessmentData.findButtonActionForStageRoundTurn(4, 0, 0, "Play All");
        if (action != null) {
            return true;
        }
        return false;
    }

    // Did the player conduct experiments at all in the beginning? 
    public static boolean dp11_experiments() {
        Action action = AssessmentData.findButtonActionForStageRoundTurn(3, 0, 0, "Create");
        if (action != null) {
            return true;
        }
        return false;
    } 

    // Did the player use the experiment slider to zero in on events?   
    // I don't think I have a mechanism for recording this
    /*public static boolean dp10_experimentSlider() {
        return false;
    } */

    // Did the player try any new experiments on the chaos stage before forging forward?  
    public static boolean dp12_newExpChaos() {
        Action action = AssessmentData.findButtonActionForStageRoundTurn(4, 0, 0, "Create");
        if (action != null) {
            return true;
        }
        return false;
    }  

    // Did the player try changing every single variable in experiments? (should be weighted the highest)  
    public static boolean dp13_changeAllExpVars() {
        // Let's just look through all experiments in the entire game and see if they did
        // at least one with each variable changed.
        return false;
    }
    // Did the player try changing at least 3 variables in experiments?
    public static boolean dp13_changeThreeExpVars() {
        // Let's just look through all experiments in the entire game and see if they did
        // at least one with each variable changed.
        return false;
    }
    // Did the player try changing at least 3 overall variables in experiments?
    public static boolean dp13_changeThreeOverallExpVars() {
        // Let's just look through all experiments in the entire game and see if they did
        // at least one with each variable changed.
        return false;
    }
    // Did the player try changing at least 3 specific variables in experiments?
    public static boolean dp13_changeThreeSpecificExpVars() {
        // Let's just look through all experiments in the entire game and see if they did
        // at least one with each variable changed.
        return false;
    }
    // Did the player try changing every single variable BEFORE jumping into the sim?
    public static boolean dp13_changeAllExpVarsFirst() {
        // Let's just look through all experiments in the entire game and see if they did
        // at least one with each variable changed.
        return false;
    }
    // Did the player try changing at least 3 variables BEFORE jumping into the sim?
    public static boolean dp13_changeThreeExpVarsFirst() {
        // Let's just look through all experiments in the entire game and see if they did
        // at least one with each variable changed.
        return false;
    }
    // Did the player try changing at least 3 overall variables BEFORE jumping into the sim?
    public static boolean dp13_changeThreeOverallExpVarsFirst() {
        // Let's just look through all experiments in the entire game and see if they did
        // at least one with each variable changed.
        return false;
    }
    // Did the player try changing at least 3 specific variables BEFORE jumping into the sim?
    public static boolean dp13_changeThreeSpecificExpVarsFirst() {
        // Let's just look through all experiments in the entire game and see if they did
        // at least one with each variable changed.
        return false;
    }
    
    // At turn 1, did they order the overall variable ratings (generally) correctly
    // in each stage?

    // So, your top 3 should be growth, paradigm, and gravity well location for Chaos- if any
    // one of these is in the top 3 (we'll say 4 just because of the difficulty of learning
    // the system is likely to produce something spurious) then you're good
    public static boolean dp14_orderGrowthVariable(int stg) {
        RatingData ratings = new RatingData(stg, 1, 0);
        if (ratings != null) {
            if (ratings.inTopX(4, RatingData.RatingType.Growth) == true) {
                return true;
            }
        }
        return false;
    }
    public static boolean dp14_orderGrowthVariable_stg1() {
        return dp14_orderGrowthVariable(3);
    }
    public static boolean dp14_orderGrowthVariable_stg2() {
        return dp14_orderGrowthVariable(4);
    }

    public static boolean dp14_orderParadigmVariable(int stg) {
        RatingData ratings = new RatingData(stg, 1, 0);
        if (ratings != null) {
            if (ratings.inTopX(4, RatingData.RatingType.Paradigm) == true) {
                return true;
            }
        }
        return false;
    }
    // I would not consider paradigm important on turn 1 of stage 1,
    // it doesn't become important until later
    /*public static boolean dp14_orderParadigmVariable_stg1() {
        return dp14_orderParadigmVariable(3);
    }*/
    public static boolean dp14_orderParadigmVariable_stg2() {
        return dp14_orderParadigmVariable(4);
    }

    public static boolean dp14_orderGravityWellLocationVariable(int stg) {
        RatingData ratings = new RatingData(stg, 1, 0);
        if (ratings != null) {
            if (ratings.inTopX(4, RatingData.RatingType.GravityWellLocation) == true) {
                return true;
            }
        }
        return false;
    }
    public static boolean dp14_orderGravityWellLocationVariable_stg1() {
        return dp14_orderGravityWellLocationVariable(3);
    }
    public static boolean dp14_orderGravityWellLocationVariable_stg2() {
        return dp14_orderGravityWellLocationVariable(4);
    }

    // At turn 1, was shape color and type rated below speed and dir?
    public static boolean dp15_rateSpinDirOverType(int stg) {
        // So, for individual vars, spin dir has to come first, then spin speed,
        // then color/type
        RatingData ratings = new RatingData(stg, 1, 0);
        int spinDirRating = ratings.getRatingFor(RatingData.RatingType.ShapeSpinDirection);
        int spinSpeedRating = ratings.getRatingFor(RatingData.RatingType.ShapeSpinSpeed);
        int typeRating = ratings.getRatingFor(RatingData.RatingType.ShapeType);
        int colorRating = ratings.getRatingFor(RatingData.RatingType.ShapeColor);
        if (spinDirRating <= typeRating || spinDirRating <= colorRating) {
            return false;
        }
        if (spinSpeedRating <= typeRating || spinSpeedRating <= colorRating) {
            return false;
        }
        return true;
    }
    public static boolean dp15_rateSpinDirOverType_stg1() {
        return dp15_rateSpinDirOverType(3);
    }
    public static boolean dp15_rateSpinDirOverType_stg2() {
        return dp15_rateSpinDirOverType(4);
    }

    // Was the order of specific vars actually correct?  This would take the other
    // dp15 and up it by one "level" of difficulty by saying that dir also had to be rated
    // greater than speed
    public static boolean dp15_orderSpecificVariables(int stg) {
        // So, for individual vars, spin dir has to come first, then spin speed,
        // then color/type
        RatingData ratings = new RatingData(stg, 1, 0);
        int spinDirRating = ratings.getRatingFor(RatingData.RatingType.ShapeSpinDirection);
        int spinSpeedRating = ratings.getRatingFor(RatingData.RatingType.ShapeSpinSpeed);
        int typeRating = ratings.getRatingFor(RatingData.RatingType.ShapeType);
        int colorRating = ratings.getRatingFor(RatingData.RatingType.ShapeColor);
        if (spinDirRating <= spinSpeedRating || spinDirRating <= typeRating || spinDirRating <= colorRating) {
            return false;
        }
        if (spinSpeedRating <= typeRating ||  spinSpeedRating <= colorRating) {
            return false;
        }
        return true;
    }
    public static boolean dp15_orderSpecificVariables_stg1() {
        return dp15_orderSpecificVariables(3);
    }
    public static boolean dp15_orderSpecificVariables_stg2() {
        return dp15_orderSpecificVariables(4);
    }

    // Rated overall variables more than specific variables?   
    public static boolean dp16_overallVarRating(int stg) {
        // Let's see what they have... if they have all the ratings,
        // then we'll use only the 2nd chaos stage.  If not, we'll
        // go back in time until we find one that they do have if they
        // didn't complete the whole thing.
        RatingData ratings = null;
        ratings = new RatingData(stg, 1, 0);
        if (ratings != null) {
            if (ratings.getWeightedSumOverallRatings() > ratings.getWeightedSumSpecificRatings()) {
                return true;
            }
            return false;
        }
        ratings = new RatingData(stg, 0, 0);
        if (ratings != null) {
            if (ratings.getWeightedSumOverallRatings() > ratings.getWeightedSumSpecificRatings()) {
                return true;
            }
            return false;
        }
        return false;
    }
    public static boolean dp16_overallVarRating_stg1() {
        return dp15_orderSpecificVariables(3);
    }
    public static boolean dp16_overallVarRating_stg2() {
        return dp15_orderSpecificVariables(4);
    }

    // Did the player change variable ratings on different turns on round 2
    // of either stage?  Player should have done this as the different variables
    // definitely change in importance
    public static boolean dp17_changedVarRatings(int stg, int maxTurns) {
        RatingData ratings = null;
        RatingData prevRatings = null;
        int turn = 0;
        for (turn = 0; turn < maxTurns; turn++) {
            ratings = new RatingData(stg, 1, turn);
            if (ratings != null && prevRatings != null) {
                if (ratings.ratingsEqual(prevRatings) == false) {
                    return true;
                }
            }
            prevRatings = ratings;
        }
        return false;
    }
    public static boolean dp17_changedVarRatings_stg1() {
        return dp17_changedVarRatings(3, MAX_TURNS_FOUR_SHAPES);
    }
    public static boolean dp17_changedVarRatings_stg2() {
        return dp17_changedVarRatings(4, MAX_TURNS_CHAOS);
    }
    
    // Did the player NOT change Shape Type ever in Chaos stage round 2?  
    public static boolean dp18_noChangeShapeType() {
        int turn = 0;
        boolean spinDir = false;
        if (loadSystemAtStageRoundTurn(STAGE_CHAOS, ROUND_2, 0) == false) {
            return false;
        }
        SysShape shape = Player.getSelectedShape();
        int startingType = shape.getNumCorners();

        for (turn = 1; turn < MAX_TURNS_CHAOS; turn++) {
            if (loadSystemAtStageRoundTurn(STAGE_CHAOS, ROUND_2, turn) == true) {
                shape = Player.getSelectedShape();
                if (startingType != shape.getNumCorners()) {
                    // Oops, they changed it...
                    return false;
                }
            }
            else {
                return false;
            }
        }
        return true;
    }

    /* SCORE TIERS:
    STAGE 1:
    0-5 is LOW score
    6-15 is SOME score (you made some progress)
    16-30 is SUCCESS score
    31-48 is HIGH SUCCESS score
    49+ is HIGH score (since the game records a 50 as a 49 due to race condition)

    STAGE 2:
    0-1 is LOW score
    2-9 is SOME score (you made some progress)
    10-20 is SUCCESS score
    21-29 is HIGH SUCCESS
    30+ is HIGH score
    */

    // Tiers are 0-4 just like maturity ratings on skills
    public static int calcFourShapesScoreTier() {
        // Get the score at the end of round 2 of each stage
        int score = Player.getScoreForStageRound(1, 2);
        if (score < 0) {
            return 0;
        }
        else if (score <= 5) {
            return 0;
        }
        else if (score <= 15) {
            return 1;
        }
        else if (score <= 30) {
            return 2;
        }
        else if (score <= 48) {
            return 3;
        }
        return 4;
    }
    public static int calcChaosScoreTier() {
        // Get the score at the end of round 2 of each stage
        int score = Player.getScoreForStageRound(2, 2);
        if (score < 0) {
            return 0;
        }
        else if (score <= 1) {
            return 0;
        }
        else if (score <= 9) {
            return 1;
        }
        else if (score <= 20) {
            return 2;
        }
        else if (score <= 29) {
            return 3;
        }
        return 4;
    }

    public static double getDataPointWeightForSkill(int domain, int num) {
        // Literally just reverse score weight
        return (1.0 - getScoreWeightForSkill(domain, num));
    }

    // How much is total game score weighted for each skill?
    // It's only different for Respond to Uncertainty, in which case
    // score is 60% of total.  In all others, it's 30% of total.
    public static double getScoreWeightForSkill(int domain, int num) {
        if (domain == 1 && num == 3) {
            return 0.6;
        }
        return 0.3;
    }

    // Four shapes is 1/3, Chaos is 2/3
    public static double getScoreWeightForStage(int stg) {
        if (stg == STAGE_FOUR_SHAPES) {
            return (1.0 / 3.0);
        }
        else if (stg == STAGE_CHAOS) {
            return (2.0 / 3.0);
        }
        else {
            Utils.log("ERROR - unknown stage " + stg + " passed to getScoreWeightForStage");
        }
        return 0;
    }

    // So, this is a percentile of total score (0-100) based on the various weights
    // and values
    public static double getWeightedOverallScoreForSkill(int domain, int num) {
        // For most skills, the max is 30%.  

        // It's 0-4, so a 4 is 100% for this stage
        double stg1_perc = calcFourShapesScoreTier() * 25;
        double stg2_perc = calcChaosScoreTier() * 25;

        // Now, weight the scores
        stg1_perc *= getScoreWeightForStage(STAGE_FOUR_SHAPES);
        stg2_perc *= getScoreWeightForStage(STAGE_CHAOS);

        // Combine the weighted score for total (out of 100%)
        double total_perc = stg1_perc + stg2_perc;

        // Now, multiply by weight for this skill (generally ending up at 0-30%)
        return (total_perc * getScoreWeightForSkill(domain, num));
    }

    public static double calcMaxScoreForQuestion(int uid) {
        Answer ans = Player.getAnswerForQuestionUID(uid);
        if (ans == null) {
            return 0;
        }
        return (ans.getMaxCorrect());
    }

    public static double calcScoreForQuestion(int uid) {
        Answer ans = Player.getAnswerForQuestionUID(uid);
        if (ans == null) {
            return 0;
        }
        double numCorrect = ans.getTotalCorrect();
        double numPartial = ans.getTotalPartial();
        double numWrong = ans.getTotalIncorrect();

        if (ans.getAnswerType() == Question.AnswerType.Choice) {
            // So, if it's a choice if one answer, we either get it correct
            // for a +1, get a partial for +0.5, or get it wrong for 0
            return (numCorrect + (0.5 * numPartial));
        }
        else if (ans.getAnswerType() == Question.AnswerType.Check) {
            // So, we take numCorrect, minus half for each partial,
            // because in theory a partial here should NOT be selected,
            // it's just not "as wrong" so we wouldn't want someone to select
            // the correct answer AND ALSO a partial
            // And then minus 1 for each wrong
            // But, if you ONLY have partials selected, we'll give partial
            // credit

            // NO everyone did shitty on this so we're not going to deduct
            // for wrong answers
            /*if (numCorrect == 0 && numPartial > 0 && numWrong == 0) {
                return (0.5 * numPartial);
            }
            return (numCorrect - numWrong - (numPartial/2));*/
            return (numCorrect + (0.5 * numPartial));
        }

        return 0;
    }

    // Pass it a string like "32 21 28 17"
    public static double calcMaxScoreForQuestions(String strQuestions) {
        double max = 0;
        String[] arrQs = strQuestions.split(" ");

        for (String str : arrQs) {
            int num = Utils.tryParseInt(str);
            max += calcMaxScoreForQuestion(num);
        }
    
        return max;
    }

    // Pass it a string like "32 21 28 17"
    public static double calcScoreForQuestions(String strQuestions) {
        double score = 0;
        String[] arrQs = strQuestions.split(" ");

        for (String str : arrQs) {
            int num = Utils.tryParseInt(str);
            score += calcScoreForQuestion(num);
        }
    
        return score;
    }

    // Returned as a percentile from 0-100% right now
    public static String calcScoreForSkill(int domain, int skillnum) {
        if (domain == 1 && skillnum == 2) {
            return calcConsiderWholesAndParts();
        }
        if (domain == 1 && skillnum == 3) {
            return calcEffectivelyRespondToUncertaintyAndAmbiguity();
        }
        if (domain == 1 && skillnum == 4) {
            return calcConsiderIssuesAppropriately();
        }

        if (domain == 2 && skillnum == 2) {
            return calcMaintainBoundaries();
        }
        if (domain == 2 && skillnum == 3) {
            return calcDifferentiateAndQuantifyElements();
        }

        if (domain == 3 && skillnum == 1) {
            return calcIdentifyRelationships();
        }
        if (domain == 3 && skillnum == 2) {
            return calcCharacterizeRelationships();
        }
        if (domain == 3 && skillnum == 3) {
            return calcIdentifyFeedbackLoops();
        }
        if (domain == 3 && skillnum == 4) {
            return calcCharacterizeFeedbackLoops();
        }

        if (domain == 4 && skillnum == 2) {
            return calcPredictFutureSystemBehavior();
        }
        if (domain == 4 && skillnum == 3) {
            return calcRespondToChangesOverTime();
        }
        if (domain == 4 && skillnum == 4) {
            return calcUseLeveragePoints();
        }
        return "";
    }

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

        int domain = 1;
        int skillNum = 2;
        String strQuestions = "32";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        max_dp_score += calcMaxScoreForQuestions(strQuestions);
        dp_score += calcScoreForQuestions(strQuestions);

        // Now add game dps
        // We have none for this skill

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String calcEffectivelyRespondToUncertaintyAndAmbiguity() {
        String strScore = "";
        int domain = 1;
        int skillNum = 3;
        //String strQuestions = "";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // No questions for this one
        //max_dp_score += calcMaxScoreForQuestions(strQuestions);
        //dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        max_dp_score++;
        dp_score += dp5_scoreImproved_stg1() ? 1 : 0;
        max_dp_score++;
        dp_score += dp5_scoreImproved_stg2() ? 1 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String calcConsiderIssuesAppropriately() {
        String strScore = "";
        int domain = 1;
        int skillNum = 4;
        //String strQuestions = "";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // No questions for this one
        //max_dp_score += calcMaxScoreForQuestions(strQuestions);
        //dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        max_dp_score++;
        dp_score += dp9_playAllFourShapes() ? 1 : 0;
        max_dp_score++;
        dp_score += dp10_playAllChaos() ? 1 : 0;
        max_dp_score++;
        dp_score += dp11_experiments() ? 1 : 0;
        max_dp_score++;
        dp_score += dp12_newExpChaos() ? 1 : 0;
        max_dp_score++;
        dp_score += dp13_changeAllExpVars() ? 1 : 0;
        max_dp_score++;
        dp_score += dp13_changeThreeExpVars() ? 1 : 0;
        max_dp_score++;
        dp_score += dp13_changeThreeOverallExpVars() ? 1 : 0;
        max_dp_score++;
        dp_score += dp13_changeThreeSpecificExpVars() ? 1 : 0;
        max_dp_score++;
        dp_score += dp13_changeAllExpVarsFirst() ? 1 : 0;
        max_dp_score++;
        dp_score += dp13_changeThreeExpVarsFirst() ? 1 : 0;
        max_dp_score++;
        dp_score += dp13_changeThreeOverallExpVarsFirst() ? 1 : 0;
        max_dp_score++;
        dp_score += dp13_changeThreeSpecificExpVarsFirst() ? 1 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }
 
    public static String calcUseMentalModelingAndAbstraction() {
        String strScore = "";
        // Describes the strategy after the exercise, and the rationale for variable choices
        return strScore;
    }

    ////////////////////////
    /// Content Domain ///
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
        int domain = 2;
        int skillNum = 2;
        String strQuestions = "31 32";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        max_dp_score += calcMaxScoreForQuestions(strQuestions);
        dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        // No game dps for this one

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String calcDifferentiateAndQuantifyElements() {
        String strScore = "";
        int domain = 2;
        int skillNum = 3;
        String strQuestions = "17 20 22 27 28 29 30";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        max_dp_score += calcMaxScoreForQuestions(strQuestions);
        dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        max_dp_score++;
        dp_score += dp18_noChangeShapeType() ? 1 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    
    ////////////////////////
    //// Structure Domain ////
    ////////////////////////
    public static String calcIdentifyRelationships() {
        String strScore = "";
        int domain = 3;
        int skillNum = 1;
        String strQuestions = "16 18 21 27 28 31 32";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        max_dp_score += calcMaxScoreForQuestions(strQuestions);
        dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        // More heavily weight stage 2
        max_dp_score++;
        dp_score += dp1_rainAndGrowth_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp1_rainAndGrowth_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp2_stoppedSpikes_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp2_stoppedSpikes_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp3_ballSpikeRatio_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp3_ballSpikeRatio_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp7_clockwise() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp8_paradigmCoop() ? 2 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String calcCharacterizeRelationships() {
        String strScore = "";
        int domain = 3;
        int skillNum = 2;
        String strQuestions = "18 21";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        max_dp_score += calcMaxScoreForQuestions(strQuestions);
        dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        // More heavily weight stage 2
        max_dp_score++;
        dp_score += dp1_rainAndGrowth_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp1_rainAndGrowth_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp2_stoppedSpikes_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp2_stoppedSpikes_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp3_ballSpikeRatio_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp3_ballSpikeRatio_stg2() ? 2 : 0;
        max_dp_score+=2;
        dp_score += dp7_spinSpeedMaxWhileClockwise() ? 2 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String calcIdentifyFeedbackLoops() {
        String strScore = "";
        int domain = 3;
        int skillNum = 3;
        String strQuestions = "16 19 22 26 27";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        max_dp_score += calcMaxScoreForQuestions(strQuestions);
        dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        // More heavily weight stage 2
        max_dp_score++;
        dp_score += dp1_rainAndGrowth_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp1_rainAndGrowth_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp3_ballSpikeRatio_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp3_ballSpikeRatio_stg2() ? 2 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String calcCharacterizeFeedbackLoops() {
        String strScore = "";
        int domain = 3;
        int skillNum = 4;
        String strQuestions = "19 22 27";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        max_dp_score += calcMaxScoreForQuestions(strQuestions);
        dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        // More heavily weight stage 2
        max_dp_score++;
        dp_score += dp1_rainAndGrowth_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp1_rainAndGrowth_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp3_ballSpikeRatio_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp3_ballSpikeRatio_stg2() ? 2 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
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
        int domain = 4;
        int skillNum = 2;
        String strQuestions = "26 27 30";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        max_dp_score += calcMaxScoreForQuestions(strQuestions);
        dp_score += calcScoreForQuestions(strQuestions);

        // Game data points
        // None for this one

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String calcRespondToChangesOverTime() {
        String strScore = "";
        int domain = 4;
        int skillNum = 3;
        //String strQuestions = "";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        // None for this one

        // Game data points
        max_dp_score++;
        dp_score += dp17_changedVarRatings_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp17_changedVarRatings_stg2() ? 2 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String calcUseLeveragePoints() {
        String strScore = "";
        int domain = 4;
        int skillNum = 4;
        //String strQuestions = "";

        // Start with the overall score
        double score = getWeightedOverallScoreForSkill(domain, skillNum);

        // Now factor in the answers and data points score
        double max_dp_score = 0;
        double dp_score = 0;
        
        // Questions
        // None for this one

        // Game data points
        max_dp_score++;
        dp_score += dp1_rainAndGrowth_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp1_rainAndGrowth_stg2() ? 2 : 0;
        max_dp_score+=2;
        dp_score += dp8_paradigmCoop() ? 2 : 0;
        max_dp_score++;
        dp_score += dp14_orderGrowthVariable_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp14_orderGrowthVariable_stg2() ? 2 : 0;
        max_dp_score+=2;
        dp_score += dp14_orderParadigmVariable_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp14_orderGravityWellLocationVariable_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp14_orderGravityWellLocationVariable_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp15_rateSpinDirOverType_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp15_rateSpinDirOverType_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp15_orderSpecificVariables_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp15_orderSpecificVariables_stg2() ? 2 : 0;
        max_dp_score++;
        dp_score += dp16_overallVarRating_stg1() ? 1 : 0;
        max_dp_score+=2;
        dp_score += dp16_overallVarRating_stg2() ? 2 : 0;

        double dp_perc = dp_score / max_dp_score;
        
        // Weight it
        dp_perc *= getDataPointWeightForSkill(domain, skillNum);

        // Add it in
        score += dp_perc;

        // We now have a 0-100% score for this skill.
        return returnStr(score, strScore);
    }

    public static String returnStr(double score, String strScore) {
        return "" + Utils.round(score, 0) + strScore;
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