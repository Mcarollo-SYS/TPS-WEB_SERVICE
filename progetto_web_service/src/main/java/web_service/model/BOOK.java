package web_service.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item") // "item" per le liste, ma adattabile a "book" per richieste
public class BOOK {
    private int id;
    private String title;
    private int author_id; // Nota: corrisponde a "author_id" nel DB
    private int publication_year;

    public BOOK() {}

    public BOOK(int id, String title, int authorId, int publicationYear) {
        this.id = id;
        this.title = title;
        this.author_id = authorId;
        this.publication_year = publicationYear;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getAuthor_id() { return author_id; }
    public void setAuthor_id(int author_id) { this.author_id = author_id; }
    public int getPublication_year() { return publication_year; }
    public void setPublication_year(int publication_year) { this.publication_year = publication_year; }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', authorId=" + author_id + ", year=" + publication_year + "}";
    }
}