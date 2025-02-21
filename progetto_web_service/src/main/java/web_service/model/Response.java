package web_service.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class Response {
    private String message;
    private int id;

    public Response() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return "Response{message='" + message + "', id=" + id + "}";
    }
}