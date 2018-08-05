package gos;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "exercises")
public class ExerciseListWrapper {

    private List<Exercise> list;

    @XmlElement(name = "exercise")
    public List<Exercise> getExerciseList() {
        return list;
    }

    public void setExerciseList(List<Exercise> newList) {
        this.list = newList;
    }
}