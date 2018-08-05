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

public class Question extends GameDataItem {
    
    public static enum AnswerType {
        Input(0), MultilineInput(1), Choice(2), Check(3);

        private final int value;
        private AnswerType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // Conversely I can name them differently fro the ones in the actual
    // XML using the annotation @XmlElement(name="XYZ") where XYZ is the actual
    // name in the XML file.  If not specified it defaults to the name of the variable.
    private final IntegerProperty exercise = new SimpleIntegerProperty(0);
    //private final IntegerProperty id = new SimpleIntegerProperty(0);
    //private final StringProperty name = new SimpleStringProperty("");
    //private final StringProperty text = new SimpleStringProperty("");
    private ObjectProperty<AnswerType> answerType = new SimpleObjectProperty<>(AnswerType.Input);

    // So this works just fine if I dont have it wrapped in answerOptions
    //@XmlElement(name="answerOption")
    //private ArrayList<String> answerOptions = new ArrayList<String>();
    //public ArrayList<String> getAnswerOptions() { return answerOptions;  }

    // If I do have it wrapped, I need a new AnswerOptionsWrapper class.
    // Basically anytime you have a list of things that is down one level in
    // some kind of XML element, you need to wrap it in a class
    // and that class holds the actual list.
    // Start wrapping stuff
    @XmlElement(name="answerOptions")
    private AnswerOptionsWrapper answerOptionsWrapper; // Don't need a new since the class is static

    public static class AnswerOptionsWrapper {
        // We need the annotations because the variable name is different from the XML element name.
        @XmlElement(name="answerOption")
        public ArrayList<String> answerOptions = new ArrayList<String>();
    }
    public ArrayList<String> getAnswerOptions() {  return answerOptionsWrapper.answerOptions;  }
    //End wrapping stuff

    public int getExercise() {
        return exercise.get();
    }

    public void setExercise(int num) {
        exercise.set(num);
    }

    public IntegerProperty ExerciseProperty() {
        return exercise;
    }

    public AnswerType getAnswerType() {
        return answerType.get();
    }

    public void setAnswerType(AnswerType newType) {
        answerType.set(newType);
    }

    public ObjectProperty<AnswerType> answerTypeProperty() {
        return answerType;
    }
    
    /*public void stripCarriageReturns() {
        // Remove leading and trailing whitespace.
        text.set(getText().trim());

        // Remove newlines.
        text.set(getText().replaceAll("\\n", ""));
        text.set(getText().replaceAll("\\r", ""));

        // Remove all double whitespaces.
        text.set(getText().replaceAll("\\s{2,}", " "));
        //s = s.replaceAll("\\s{2,}", " ");
       // text = text.replaceAll("\\r", "");
    }*/
}

/*
<tasks>
  <task>
    <id>1</id>
    <text>Using the information provided, guess the amount of Matter Node 1 will have after 2 turns.</text>
    <answerType>Input</answerType>
  </task>
  <task>
    <id>2</id>
    <task>What will happen to Node 1's Matter in 2 turns if it is not able to draw any Energy?</task>
    <answerType>Choice</answerType>
    <answerOptions>
        <answerOption>Decrease</answerOption>
        <answerOption>Increase</answerOption>
        <answerOption>Stay the same</answerOption>
        <answerOption>Not enough information</answerOption>
    </answerOptions>
  </task>
  <task>
    <id>3</id>
    <text>Does this system remind you of any real life systems?</text>
    <answerType>Input</answerType>
  </task>
</tasks>*/