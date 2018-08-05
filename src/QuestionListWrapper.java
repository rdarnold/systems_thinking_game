package gos;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "questions")
public class QuestionListWrapper {

    private List<Question> list;

    @XmlElement(name = "question")
    public List<Question> getQuestionList() {
        return list;
    }

    public void setQuestionList(List<Question> newList) {
        this.list = newList;
    }
}