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

public class Task extends GameDataItem {
    
    // Conversely I can name them differently fro the ones in the actual
    // XML using the annotation @XmlElement(name="XYZ") where XYZ is the actual
    // name in the XML file.  If not specified it defaults to the name of the variable.
    private final IntegerProperty exercise = new SimpleIntegerProperty(0);
    private final IntegerProperty turns = new SimpleIntegerProperty(5);

    public int getExercise() {
        return exercise.get();
    }

    public void setExercise(int num) {
        exercise.set(num);
    }

    public int getTurns() {
        return turns.get();
    }

    public void setTurns(int num) {
        turns.set(num);
    }

    // NOT PART OF XML
    public String toStringTitle() {
        String str = "Task " + (getId() + 1);

        if (getName() != null && getName() != "") {
            str += ", "+ getName();
        }
        else {
            str += " ("+ getTurns() + " turns)";
        }
        /*if (getName() != null && getName() != "") {
            str += ": "+ getName() + " (" + getTurns() + " turns)";
        }
        else {
            str += " ("+ getTurns() + " turns)";
        }*/
        return str;
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean carriageReturn) {
        // Could use stringBuilder but this doesnt get updated very often so whatever.
        String str = toStringTitle();

        if (carriageReturn == true) {
            str += "\r\n"; 
        } 
        else {
            str += ": ";
        }
        str += getText();
        return str;
    }
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