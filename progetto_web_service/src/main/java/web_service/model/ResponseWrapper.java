package web_service.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

//classe dedicata alle Richieste GET 
@XmlRootElement(name = "response")
public class ResponseWrapper<T> {
    private List<T> Obj;

    @XmlElement(name = "item")
    public List<T> getItems() {
         return Obj;
    }
    public void setItems(List<T> items) {
         this.Obj = items; 
    }

    @Override
    public String toString() {
        return "item=" + Obj + "}";
    }
}