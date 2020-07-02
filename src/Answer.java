package gos;

import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;

// A record of an answer to a question.
public class Answer {

    public Question.AnswerType answerType;
    public int questionId = -1; // The ID within the exercise of the question
    public int questionUid = -1; // The unique ID of the question
    public int exerciseId = -1;
    public int retries = 0; // When this answer was given, how many retries had the player done on this stage?
    public long timestamp; // Every acion has a timestamp so we know when it occurred.
    public String strAnswerText;
    public String strQuestionText;  // Only used in the analyzer program, not the actual game

    public Answer(Question question, String text) {
        setTimeToNow(); // Restamp it later if needs be if we pre-created this.
        answerType = question.getAnswerType();
        questionId = question.getId();
        questionUid = question.getUid();
        exerciseId = question.getExercise(); // This is stored as an int on the question
        retries = Player.getRetries(); // How many retries when this answer was created?
        strAnswerText = text;
    }

    public int getQuestionId() { return questionId; }
    public int getQuestionUid() { return questionUid; }
    public int getExerciseId() { return exerciseId; }
    public int getRetries() { return retries; }
    public long getTimestamp() { return timestamp; }

    // START tableView support
    // These support the tableView
    public String getStrTime() {
        // So diff is in ms.  We want to print basically the
        // hours/seconds/minutes 00:00:00 since this person started
        // the game.
        int seconds = (int) (timestamp / 1000) % 60 ;
        int minutes = (int) ((timestamp / (1000*60)) % 60);
        int hours   = (int) ((timestamp / (1000*60*60)) % 24);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String getStrQuestion() { return strQuestionText; }
    public String getStrAnswer() { return strAnswerText; }
    // END tableView support

    // Answers in the answer string have the number as their first character,
    // and go line by line.
    // 1: blah blah
    // 4: indeed this is correct
    // 5: zoids (always a correct answer)
    // This arraylist contains those numbers
    public ArrayList<Integer> getAnswerNumbers() {
        if (strAnswerText == null || strAnswerText.equals("")) {
            return null;
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        String lines[] = strAnswerText.split("\\r?\\n");
        for (String line : lines) {
            // Go to the first colon
            int end = line.indexOf(":");
            if (end <= 0) {
                continue;
            }
            int num = Utils.tryParseInt(line.substring(0, end));
            list.add(num);
        }
        return list;
    }

    // How many correct answers did we provide?
    public int getTotalCorrect() {
        int total = 0;
        Question q = Data.getQuestionByUID(getQuestionUid());
        if (q == null) {
            return 0;
        }
        String str = q.getCorrect();
        if (str == null || str.equals("")) {
            return 0;
        }
        String[] correctVals = str.split(",");
        ArrayList<Integer> answerVals = getAnswerNumbers();

        // So now, compare the answers we provided with the correct answers
        for (String val : correctVals) {
            int correctAns = Utils.tryParseInt(val);
            for (Integer ans : answerVals) {
                if (correctAns == ans.intValue()) {
                    total++;
                }
            }
        }
        return total;
    }
    
    // How many partials did we get?
    public int getTotalPartial() {
        int total = 0;
        Question q = Data.getQuestionByUID(getQuestionUid());
        if (q == null) {
            return 0;
        }
        String str = q.getPartial();
        if (str == null || str.equals("")) {
            return 0;
        }
        String[] partialVals = str.split(",");
        ArrayList<Integer> answerVals = getAnswerNumbers();

        // So now, compare the answers we provided with the partial answers
        for (String val : partialVals) {
            int partialAns = Utils.tryParseInt(val);
            for (Integer ans : answerVals) {
                if (partialAns == ans.intValue()) {
                    total++;
                }
            }
        }
        return total;
    }

    // How many wrong answers did we provide?
    // i.e. an answer is neither correct nor partial
    public int getTotalIncorrect() {
        int total = 0;
        Question q = Data.getQuestionByUID(getQuestionUid());
        if (q == null) {
            return 0;
        }
        // You must have SOME correct values
        String strCor = q.getCorrect();
        if (strCor == null || strCor.equals("")) {
            return 0;
        }
        String[] correctVals = strCor.split(",");
        String[] partialVals = null;

        // You don't need partial values
        String strPart = q.getPartial();
        if (strPart != null && strPart.equals("") == false) {
            partialVals = strPart.split(",");
        }
        ArrayList<Integer> answerVals = getAnswerNumbers();

        // So now, compare the answers we provided with the correct and partials
        boolean found = false;
        for (Integer ans : answerVals) {
            // Is the provided answer on the "correct" list?
            for (String val : correctVals) {
                int correctAns = Utils.tryParseInt(val);
                if (correctAns == ans.intValue()) {
                    // It's a correct answer, so skip it
                    found = true;
                    break;
                }
            }
            if (partialVals != null) {
                for (String pval : partialVals) {
                    int partialAns = Utils.tryParseInt(pval);
                    if (partialAns == ans.intValue()) {
                        // It's a partial answer, so skip it
                        found = true;
                    }
                }  
            }
            if (found == false) {
                // It wasn't correct or partial, so it was incorrect
                total++;
            }
            found = false;
        }
        return total;
    }

    // What is the max number of possible correct answers for this question?
    public int getMaxCorrect() {
        Question q = Data.getQuestionByUID(getQuestionUid());
        String str = q.getCorrect();
        if (str == null || str.equals("")) {
            return 0;
        }
        String[] corValues = str.split(",");
        return corValues.length;
    }

    // Does the answer contain the numerical option passed in?  as in,
    // if we selected answer number 2, is that this one?  Only works
    // for Choice and Check
    public boolean answerOptionSelected(int num) {
        if (answerType != Question.AnswerType.Check && answerType != Question.AnswerType.Choice) {
            return false;
        }
        // The answer string looks like this in a Choice:
        // 2: Answer text

        // Or in a Check:
        // 1: Answer text
        // 3: Answer text
        // 6: Answer text

        // So try to split it into lines if we have them, and then check the first number of each line
        String[] lines = getStrAnswer().split("\r\n");
        for (String str : lines) {
            int end = str.indexOf(":");
            int anum = Utils.tryParseInt(str.substring(0, end));
            if (num == anum) {
                return true;
            }
        }
        return false;
    }

    public void setTimeToNow() {
        timestamp = System.currentTimeMillis() - Player.startTime;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ANS: " + answerType.getValue());
        sb.append("\r\n");
        sb.append("AT: " + timestamp);
        sb.append("\r\n");
        sb.append("QI: " + questionId);
        sb.append("\r\n");
        sb.append("UD: " + questionUid);
        sb.append("\r\n");
        sb.append("EI: " + exerciseId);
        sb.append("\r\n");
        sb.append("RT: " + retries);
        sb.append("\r\n");
        sb.append("@STX\r\n");
        sb.append(strAnswerText); // We could have a multi-line answer
        sb.append("@ETX\r\n");
        return sb.toString();
    }
    
    // The fromLines array should have all of the action info it in already, ordered
    // correctly
    public Answer(List<String> fromLines) {
        fromStringArray(fromLines);

        // And now set the question text from the ID
        setQuestionText();
    }
    
    private void setQuestionText() {
        Question question = Data.getQuestion(questionId, exerciseId);
        if (question != null) {
            strQuestionText = question.getText();
        }
    }

    // Returns the line number after the @STX line, so, the line 
    // that the answer text should start on
    private int getAnswerData(List<String> fromLines) {
        String line = null;
        int lineNum = 0;
        
        line = fromLines.get(lineNum);
        while (line != null && line.substring(0, 4).equals("@STX") == false) {
            if (line.substring(0, 4).equals("ANS:") == true) {
                line = line.substring(5, line.length());
                answerType = Question.AnswerType.fromInt(Utils.tryParseInt(line));
            }
            else if (line.substring(0, 3).equals("AT:") == true) {
                line = line.substring(4, line.length());
                timestamp = Utils.tryParseLong(line);
            } 
            else if (line.substring(0, 3).equals("QI:") == true) {
                line = line.substring(4, line.length());
                questionId = Utils.tryParseInt(line);
            }
            else if (line.substring(0, 3).equals("UD:") == true) {
                line = line.substring(4, line.length());
                questionUid = Utils.tryParseInt(line);
            }
            else if (line.substring(0, 3).equals("EI:") == true) {
                line = line.substring(4, line.length());
                exerciseId = Utils.tryParseInt(line);
            }
            else if (line.substring(0, 3).equals("RT:") == true) {
                line = line.substring(4, line.length());
                retries = Utils.tryParseInt(line);
            }
            // If I need anything else, I can just add another entry here and
            // it won't render "old data" unloadable
            lineNum++;
            line = fromLines.get(lineNum);
        }

        lineNum++;
        return lineNum;
    }

    public boolean fromStringArray(List<String> fromLines) {
        boolean success = true;
        String line = null;
        int lineNum = getAnswerData(fromLines);

        // First line should be ANS (type)
        /*line = fromLines.get(lineNum);
        line = line.substring(5, line.length());
        answerType = Question.AnswerType.fromInt(Utils.tryParseInt(line));
        lineNum++;

        // Now AT (time)
        line = fromLines.get(lineNum);
        if (line.substring(0, 3).equals("AT:") == true) {
            line = line.substring(4, line.length());
            timestamp = Utils.tryParseLong(line);
            lineNum++;
        }

        // Now QI 
        line = fromLines.get(lineNum);
        if (line.substring(0, 3).equals("QI:") == true) {
            line = line.substring(4, line.length());
            questionId = Utils.tryParseInt(line);
            lineNum++;
        }

        // Now EI
        line = fromLines.get(lineNum);
        if (line.substring(0, 3).equals("EI:") == true) {
            line = line.substring(4, line.length());
            exerciseId = Utils.tryParseInt(line);
            lineNum++;
        }

        // Now RT
        line = fromLines.get(lineNum);
        if (line.substring(0, 3).equals("RT:") == true) {
            line = line.substring(4, line.length());
            retries = Utils.tryParseInt(line);
            lineNum++;
        }

        // Now @STX which is nothing
        lineNum++;*/

        // Now we just keep going until the end or we hit @ETX, we should be able to handle either one
        strAnswerText = "";
        while (lineNum < fromLines.size()) {
            line = fromLines.get(lineNum);
            if (line.length() >= ("@ETX").length() && line.equals("@ETX")) {
                break;
            }
            strAnswerText += line;
            strAnswerText += "\r\n";
            lineNum++;
        }

        return success;
    }
}