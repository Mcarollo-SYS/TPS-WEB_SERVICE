package web_service.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

//classe per Richieste GET 
@XmlRootElement(name = "response")
public class ResponseWrapper<T> {
    private List<T> Obj;
    
    //dichiarazioen tag per il contenitore della risposta 
    @XmlElement(name = "item")

        //vari getter e setter 
    public List<T> getItems() {
         return Obj;
    } 
    public void setItems(List<T> items) {
         this.Obj = items; 
    }

    //metodo tostring 
    @Override
    public String toString() {
        return "item=" + Obj + "}";
    }
}