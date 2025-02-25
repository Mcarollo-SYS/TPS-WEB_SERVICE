package web_service.model;

import static jakarta.xml.bind.annotation.XmlAccessType.FIELD;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

// classe per rappresentare le risposte POST, PUT, DELETE
@XmlRootElement(name = "response")
@XmlAccessorType(FIELD)
public class Response {
    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "id")
    private int id;

    public Response() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "mes='" + message + "', id=" + id + "}";
    }
}