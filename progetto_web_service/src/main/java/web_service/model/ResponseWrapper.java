package web_service.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class ResponseWrapper<T> {
    private List<T> items;

    @XmlElement(name = "item")
    public List<T> getItems() {
         return items;
    }
    public void setItems(List<T> items) {
         this.items = items; 
    }

    @Override
    public String toString() {
        return "ResponseWrapper{items=" + items + "}";
    }
}