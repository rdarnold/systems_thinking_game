package gos;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "infoList")
public class InfoListWrapper {

    private List<Info> infoList;

    @XmlElement(name = "info")
    public List<Info> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<Info> list) {
        this.infoList = list;
    }
}