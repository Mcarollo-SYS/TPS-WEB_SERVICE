package web_service;

import web_service.utils.Client;

public class App {
    public static void main(String[] args) {
        Client client = new Client("http://localhost/api.php");

        try {
            client.getBooks();
            client.getAuthors();
            client.getBooksWithAuthors();
            client.createBook("New Book", 1, 2023);
            client.updateBook(1, "Updated Book");
            client.deleteBook(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}