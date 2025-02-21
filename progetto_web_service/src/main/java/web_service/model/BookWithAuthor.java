package web_service.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
public class BookWithAuthor {
    private String title;
    private String name;

    public BookWithAuthor() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "BookWithAuthor{title='" + title + "', author='" + name + "'}";
    }
}