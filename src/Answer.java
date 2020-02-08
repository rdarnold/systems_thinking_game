package gos;

import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;

// A record of an answer to a question.
public class Answer {

    public Question.AnswerType answerType;
    public int questionId = -1;
    public int exerciseId = -1;
    public long timestamp; // Every acion has a timestamp so we know when it occurred.
    public String strAnswerText;
    public String strQuestionText;  // Only used in the analyzer program, not the actual game

    public Answer(Question question, String text) {
        setTimeToNow(); // Restamp it later if needs be if we pre-created this.
        answerType = question.getAnswerType();
        questionId = question.getId();
        exerciseId = question.getExercise(); // This is stored as an int on the question
        strAnswerText = text;
    }

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

    public String getStrQuestion() {
        return strQuestionText;
    }

    public String getStrAnswer() {
        return strAnswerText;
    }
    // END tableView support


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
        sb.append("EI: " + exerciseId);
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

    public boolean fromStringArray(List<String> fromLines) {
        boolean success = true;
        String line = null;

        // First line should be ANS (type)
        line = fromLines.get(0);
        line = line.substring(5, line.length());
        answerType = Question.AnswerType.fromInt(Utils.tryParseInt(line));

        // Now AT (time)
        line = fromLines.get(1);
        line = line.substring(4, line.length());
        timestamp = Utils.tryParseLong(line);

        // Now QI 
        line = fromLines.get(2);
        line = line.substring(4, line.length());
        questionId = Utils.tryParseInt(line);

        // Now EI
        line = fromLines.get(3);
        line = line.substring(4, line.length());
        exerciseId = Utils.tryParseInt(line);

        // Now @STX which is nothing

        // Now we just keep going until the end or we hit @ETX, we should be able to handle either one
        int i = 5;
        strAnswerText = "";
        while (i < fromLines.size()) {
            line = fromLines.get(i);
            if (line.length() >= ("@ETX").length() && line.equals("@ETX")) {
                break;
            }
            strAnswerText += line;
            strAnswerText += "\r\n";
            i++;
        }

        return success;
    }
}