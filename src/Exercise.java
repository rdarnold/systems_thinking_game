package gos;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;

public class Exercise extends GameDataItem {

    // Each exercise has an associated list of questions.
    // These two fields are not supported by the XML.
    public ArrayList<Question> questionList = new ArrayList<Question>();
    public ArrayList<Task> taskList = new ArrayList<Task>();
    public int currentQuestionNumber = 0;
    public int currentTaskNumber = 0;

    //public ArrayList<Task> getTasks() { return taskList; }
    //public ArrayList<Question> getQuestions() { return questionList; }

    /////////////////////
    ///// XML STUFF /////
    /////////////////////

    // The rest of the XML stuff is taken care of in GameDataItem.
    private final StringProperty popupText = new SimpleStringProperty("");
    private final BooleanProperty usesLevels = new SimpleBooleanProperty(false);
    private final BooleanProperty noSelect = new SimpleBooleanProperty(false);
    private final BooleanProperty noDeath = new SimpleBooleanProperty(false);

    public String getPopupText() { return popupText.get(); }
    public void setPopupText(String theText) { popupText.set(theText); }
    public StringProperty popupTextProperty() { return popupText; }

    public boolean getUsesLevels() { return usesLevels.get(); }
    public void setUsesLevels(boolean val) { usesLevels.set(val); }
    public BooleanProperty usesLevelsProperty() { return usesLevels; }
    public boolean getNoSelect() { return noSelect.get(); }
    public void setNoSelect(boolean val) { noSelect.set(val); }
    public BooleanProperty noSelectProperty() { return noSelect; }
    public boolean getNoDeath() { return noDeath.get(); }
    public void setNoDeath(boolean val) { noDeath.set(val); }
    public BooleanProperty noDeathProperty() { return noDeath; }

    /////////////////////
    /// END XML STUFF ///
    /////////////////////

    public int getNumQuestions() {
        return questionList.size();
    }

    public void resetQuestions() {
        currentQuestionNumber = 0;
    }

    public Question nextQuestion() {
        currentQuestionNumber++;
        if (currentQuestionNumber >= questionList.size()) {
            currentQuestionNumber = questionList.size() - 1;
            return null;
        }
        return getCurrentQuestion();
    }

    public Question getCurrentQuestion() {
        if (questionList == null || questionList.size() == 0) {
            return null;
        }
        return questionList.get(currentQuestionNumber);
    }

    public void processQuestions(ArrayList<Question> allQuestions) {
        int id = 0;
        for (Question q : allQuestions) {
            if (q.getExercise() == getId()) {
                q.setId(id);
                id++;
                questionList.add(q);
            }
        }
    }

    public Task getNextTask() {
        if (taskList == null || taskList.size() == 0) {
            return null;
        }
        int num = currentTaskNumber + 1;
        if (num >= taskList.size()) {
            return null;
        }
        return taskList.get(num);
    }

    public Task previousTask() {
        currentTaskNumber--;
        if (currentTaskNumber < 0) {
            currentTaskNumber = 0;
            return null;
        }
        return getCurrentTask();
    }

    public Task nextTask() {
        currentTaskNumber++;
        if (currentTaskNumber >= taskList.size()) {
            currentTaskNumber = taskList.size() - 1;
            return null;
        }
        return getCurrentTask();
    }

    public Task getCurrentTask() {
        if (taskList == null || taskList.size() == 0) {
            return null;
        }
        return taskList.get(currentTaskNumber);
    }

    public void processTasks(ArrayList<Task> allTasks) {
        int id = 0;
        for (Task t : allTasks) {
            if (t.getExercise() == getId()) {
                t.setId(id);
                id++;
                taskList.add(t);
            }
        }
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean carriageReturn) {
        if (carriageReturn == true) {
            return "Exercise " + (getId() + 1) + "\r\n" + getName();
        } 
        else {
            return "Exercise " + (getId() + 1) + ": " + getName();
        }
    }

    // Do whatever we need to dowith the text like strip CRs and whitespace or turn
    // certain characters into CRs
    // This is the same functionality as processText on the GameDataItem
    public void processPopupText() {
        if (getPopupText() == "") {
            return;
        }
        popupText.set(getPopupText().trim());

        // Remove newlines.
        popupText.set(getPopupText().replaceAll("\\n", ""));
        popupText.set(getPopupText().replaceAll("\\r", ""));

        // Remove all double whitespaces.
        popupText.set(getPopupText().replaceAll("\\s{2,}", " "));

        // Add carriage returns where specified
        // First do all the cr spaces
        popupText.set(getPopupText().replaceAll("CR ", "\r\n"));
        popupText.set(getPopupText().replaceAll("CR", "\r\n"));
    }
}