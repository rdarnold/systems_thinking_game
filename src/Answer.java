package gos;

import java.util.ArrayList;
import java.util.EnumSet;

// A record of an answer to a question.
public class Answer {

    public Question.AnswerType answerType;
    public int questionId = -1;
    public int exerciseId = -1;
    public long timestamp; // Every acion has a timestamp so we know when it occurred.
    public String strAnswerText;

    public Answer(Question question, String text) {
        setTimeToNow(); // Restamp it later if needs be if we pre-created this.
        answerType = question.getAnswerType();
        questionId = question.getId();
        exerciseId = question.getExercise(); // This is stored as an int on the question
        strAnswerText = text;
    }

    public void setTimeToNow() {
        timestamp = System.currentTimeMillis();
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
}