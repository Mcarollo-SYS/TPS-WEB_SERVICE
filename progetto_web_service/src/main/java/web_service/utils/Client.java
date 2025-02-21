package web_service.utils;

import web_service.model.BOOK;
import web_service.model.BookWithAuthor;
import web_service.model.Response;
import web_service.model.ResponseWrapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    private String baseUrl;
    private HttpClient client;

    public Client(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }

    // GET: Lista dei libri
    public void getBooks() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=getBooks"))
                .GET().build();
        ResponseWrapper<BOOK> response = sendRequest(request, BOOK.class);
        System.out.println("Books: " + response.getItems());
    }

    // GET: Lista degli autori
    public void getAuthors() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=getAuthors"))
                .GET().build();
        ResponseWrapper<BOOK> response = sendRequest(request, BOOK.class); // Usiamo Book per semplicità
        System.out.println("Authors: " + response.getItems());
    }

    // GET: Libri con autori (JOIN)
    public void getBooksWithAuthors() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=getBooksWithAuthors"))
                .GET().build();
        ResponseWrapper<BookWithAuthor> response = sendRequest(request, BookWithAuthor.class);
        System.out.println("Books with Authors: " + response.getItems());
    }

    // POST: Crea un nuovo libro
    public void createBook(String title, int authorId, int publicationYear) throws Exception {
        BOOK book = new BOOK(0, title, authorId, publicationYear);
        String body = XmlUtils.marshal(book);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=createBook"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        Response response = sendRequest(request, Response.class);
        System.out.println("Create response: " + response);
    }

    // PUT: Aggiorna un libro
    public void updateBook(int id, String title) throws Exception {
        BOOK book = new BOOK(id, title, 0, 0);
        String body = XmlUtils.marshal(book);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=updateBook&id=" + id))
                .header("Content-Type", "application/xml")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        Response response = sendRequest(request, Response.class);
        System.out.println("Update response: " + response);
    }

    // DELETE: Elimina un libro
    public void deleteBook(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=deleteBook&id=" + id))
                .DELETE().build();
        Response response = sendRequest(request, Response.class);
        System.out.println("Delete response: " + response);
    }

    // Metodo centralizzato per inviare richieste e gestire risposte
    @SuppressWarnings("unchecked")
    private <T> T sendRequest(HttpRequest request, Class<?> itemClass) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        System.out.println("Request: " + request.method() + " " + request.uri());
        System.out.println("Status: " + status);

        if (status >= 200 && status < 300) {
            if (request.method().equals("GET") && !request.uri().toString().contains("getBooksWithAuthors")) {
                return (T) XmlUtils.unmarshal(ResponseWrapper.class, Response.body(), BOOK.class);
            } else if (request.uri().toString().contains("getBooksWithAuthors")) {
                return (T) XmlUtils.unmarshal(ResponseWrapper.class, Response.body(), BookWithAuthor.class);
            } else {
                return (T) XmlUtils.unmarshal(Response.class, response.body());
            }
        } else {
            System.out.println("Error: " + response.body());
            throw new Exception("Request failed with status: " + status);
        }
    }
}