package gos;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tasks")
public class TaskListWrapper {

    private List<Task> list;

    @XmlElement(name = "task")
    public List<Task> getTaskList() {
        return list;
    }

    public void setTaskList(List<Task> newList) {
        this.list = newList;
    }
}