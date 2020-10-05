
package gos.analyzer;

import gos.*;
import gos.gui.*;

// Just a holder and calculator for ratings data so it's easier to manage
public class RatingData {

    String m_strRating = "";

    int rainRate = -1; // (Rate)
    int gravityWellLocation = -1; //  (Law)
    int gravityDirection = -1; // (Law)
    int growth = -1; // (Law)
    int paradigm = -1; // (Paradigm)
    int shapeSpinSpeed = -1; // numerical value
    int shapeSpinDirection = -1; // numerical value
    int shapeType = -1; // numerical value
    int shapeColor = -1; // numerical value
    
    public RatingData () { // private constructor
    }

    public RatingData(String ratingString) {
        init(ratingString);
    }

    // stage, round, and turn are 3, 0, 0, NOT 1.1, etc)
    public RatingData(int stageNum, int roundNum, int turnNum) {
        init(AssessmentData.printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum));
    }
    public void setRatings(String ratingString) {
        init(ratingString);
    }
    public void setRatings(int stageNum, int roundNum, int turnNum) {
        init(AssessmentData.printVarRatingsForStageRoundTurn(stageNum, roundNum, turnNum));
    }

    public void reset() {
        rainRate = -1;
        gravityWellLocation = -1;
        gravityDirection = -1;
        growth = -1;
        paradigm = -1;
        shapeSpinSpeed = -1;
        shapeSpinDirection = -1;
        shapeType = -1;
        shapeColor = -1;
    }

    // This method is just for getting the values from the initial
    // loaded string
    private int getRatingFromStrFor(String substr) {
        int index = 0;

        index = m_strRating.indexOf(substr);
        if (index == -1) {
            return -1;
        }
        index += substr.length();

        // We have the first number
        String strVal = "" + m_strRating.charAt(index);

        // If it's a negative sign, just return -1, that's the only neg we have
        if (strVal.equals("-") == true) {
            return -1;
        }

        // Now move forward as long as each character is a number
        index++;
        while (Character.isDigit(m_strRating.charAt(index)) == true) {
            strVal += m_strRating.charAt(index);
            index++;
        }

        int val = Utils.tryParseInt(strVal);
        return val;
    }

    private void init(String strRating) {
        reset();
        m_strRating = strRating;
        // These should be a set of rating keys that are used universally throughout the software 
        // but at this point I just want to get my PhD done
        /*
        Rain Rate: -1
        Gravity Well Location: 10
        Gravity Direction: -1
        Growth: -1
        Paradigm: -1
        Shape Spin Speed: -1
        Shape Spin Direction: -1
        Shape Type: -1
        Shape Color: -1
        */
        rainRate = getRatingFromStrFor(Constants.VariableType.RainRate.toString() + ": ");
        gravityWellLocation = getRatingFromStrFor(Constants.VariableType.GravityWellLocation.toString() + ": ");
        gravityDirection = getRatingFromStrFor(Constants.VariableType.GravityDirection.toString() + ": ");
        growth = getRatingFromStrFor(Constants.VariableType.Growth.toString() + ": ");
        paradigm = getRatingFromStrFor(Constants.VariableType.Paradigm.toString() + ": ");
        shapeSpinSpeed = getRatingFromStrFor(Constants.VariableType.ShapeSpinSpeed.toString() + ": ");
        shapeSpinDirection = getRatingFromStrFor(Constants.VariableType.ShapeSpinDirection.toString() + ": ");
        shapeType = getRatingFromStrFor(Constants.VariableType.ShapeType.toString() + ": ");
        shapeColor = getRatingFromStrFor(Constants.VariableType.ShapeColor.toString() + ": ");
    }

    // Since we have 5 overall and 4 specific, we return a weighted sum
    // otherwise the sum is kind of meaningless
    public int getWeightedSumOverallRatings() {
        int sum = 0;

        sum += (rainRate > 0 ? rainRate : 0);
        sum += (gravityWellLocation > 0 ? gravityWellLocation : 0);
        sum += (gravityDirection > 0 ? gravityDirection : 0);
        sum += (growth > 0 ? growth : 0);
        sum += (paradigm > 0 ? paradigm : 0);
        
        // This is easy, 0 is already 50%, it has 5 ratings
        // So just reutrn the sum
        return sum;
    }

    public int getWeightedSumSpecificRatings() {
        int sum = 0;

        sum += (shapeSpinSpeed > 0 ? shapeSpinSpeed : 0);
        sum += (shapeSpinDirection > 0 ? shapeSpinDirection : 0);
        sum += (shapeType > 0 ? shapeType : 0);
        sum += (shapeColor > 0 ? shapeColor : 0);
        
        // Just multiply by 1.25 to turn 4 ratings into weighted 5.
        sum *= 1.25;
        return sum;
    }

    // Are the ratings of this one the same as another one?
    public boolean ratingsEqual(RatingData otherData) {
        if (rainRate != otherData.rainRate) return false;
        if (gravityWellLocation != otherData.gravityWellLocation) return false;
        if (gravityDirection != otherData.gravityDirection) return false;
        if (growth != otherData.growth) return false;
        if (paradigm != otherData.paradigm) return false;
        if (shapeSpinSpeed != otherData.shapeSpinSpeed) return false;
        if (shapeSpinDirection != otherData.shapeSpinDirection) return false;
        if (shapeType != otherData.shapeType) return false;
        if (shapeColor != otherData.shapeColor) return false;
        return true;
    }

    // Is the passed in type in the top X ratings?
    public boolean inTopX(int x, Constants.VariableType type) {
        int rating = getRatingFor(type);

        // So, if you're in the top 2, it means only one
        // is better than or equal to you.  Top 3 means only 2 are
        // better than you.
        // So add up the number that are rated better than or equal
        // to you.  That, plus you, should be equal to or less than x.
        int better = 1; // Since you are better than or equal to yourself
        if (Constants.VariableType.RainRate != type && getRatingFor(Constants.VariableType.RainRate) >= rating) {
            better++;
        }
        if (Constants.VariableType.GravityWellLocation != type && getRatingFor(Constants.VariableType.GravityWellLocation) >= rating) {
            better++;
        }
        if (Constants.VariableType.GravityDirection != type && getRatingFor(Constants.VariableType.GravityDirection) >= rating) {
            better++;
        }
        if (Constants.VariableType.Growth != type && getRatingFor(Constants.VariableType.Growth) >= rating) {
            better++;
        }
        if (Constants.VariableType.Paradigm != type && getRatingFor(Constants.VariableType.Paradigm) >= rating) {
            better++;
        }
        if (Constants.VariableType.ShapeSpinSpeed != type && getRatingFor(Constants.VariableType.ShapeSpinSpeed) >= rating) {
            better++;
        }
        if (Constants.VariableType.ShapeSpinDirection != type && getRatingFor(Constants.VariableType.ShapeSpinDirection) >= rating) {
            better++;
        }
        if (Constants.VariableType.ShapeType != type && getRatingFor(Constants.VariableType.ShapeType) >= rating) {
            better++;
        }
        if (Constants.VariableType.ShapeColor != type && getRatingFor(Constants.VariableType.ShapeColor) >= rating) {
            better++;
        }

        return (better >= x);
    }
    
    public int getRatingFor(Constants.VariableType type) {
        switch (type) {
            case RainRate:
                return rainRate;
            case GravityWellLocation:
                return gravityWellLocation;
            case GravityDirection:
                return gravityDirection;
            case Growth:
                return growth;
            case Paradigm:
                return paradigm;
            case ShapeSpinSpeed:
                return shapeSpinSpeed;
            case ShapeSpinDirection:
                return shapeSpinDirection;
            case ShapeType:
                return shapeType;
            case ShapeColor:
                return shapeColor;
        }
        return -1;
    }
}