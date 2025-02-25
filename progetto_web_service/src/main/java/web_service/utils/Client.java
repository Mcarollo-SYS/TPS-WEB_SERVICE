package web_service.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import web_service.model.Brand;
import web_service.model.Car;
import web_service.model.CarWithDetails;
import web_service.model.Customer;
import web_service.model.Response;
import web_service.model.ResponseWrapper;

public class Client {
    private String baseUrl;
    private HttpClient client;

    public Client(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }

    // GET: Lista delle auto
    public void getCars() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=getCars"))
                .GET().build();
        ResponseWrapper<Car> response = sendGetRequest(request, Car.class);
        System.out.println(response.getItems());
    }

    // GET: Lista delle marche
    public void getBrands() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=getBrands"))
                .GET().build();
        ResponseWrapper<Brand> response = sendGetRequest(request, Brand.class);
        System.out.println(response.getItems());
    }

    // GET: Lista dei clienti
    public void getCustomers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=getCustomers"))
                .GET().build();
        ResponseWrapper<Customer> response = sendGetRequest(request, Customer.class);
        System.out.println(response.getItems());
    }

    // GET: Auto con dettagli (JOIN)
    public void getCarsWithDetails() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=getCarsWithDetails"))
                .GET().build();
        ResponseWrapper<CarWithDetails> response = sendGetRequest(request, CarWithDetails.class);
        System.out.println(response.getItems());
    }

    // POST: Crea una nuova auto
    public void createCar(String model, int brandId, int year, double price, String color) throws Exception {
        Car car = new Car(0, model, brandId, year, price, color);
        String body = XmlUtils.marshal(car);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=createCar"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        Response response = sendNonGetRequest(request);
        System.out.println(response);
    }

    // PUT: Aggiorna un'auto
    public void updateCar(int id, String model, double price, String color) throws Exception {
        Car car = new Car(id, model, 0, 0, price, color);
        String body = XmlUtils.marshal(car);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=updateCar&id=" + id))
                .header("Content-Type", "application/xml")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        Response response = sendNonGetRequest(request);
        System.out.println(response);
    }

    // DELETE: Elimina un'auto
    public void deleteCar(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?action=deleteCar&id=" + id))
                .DELETE().build();
        Response response = sendNonGetRequest(request);
        System.out.println(response);
    }

    // Metodo per richieste GET con ResponseWrapper<T>
    @SuppressWarnings("unchecked")
    private <T> ResponseWrapper<T> sendGetRequest(HttpRequest request, Class<T> itemClass) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        System.out.println("Status: " + status);

        if (status >= 200 && status < 300) {
            // Deserializza direttamente in ResponseWrapper (JAXB deve inferire T dal XML)
            return (ResponseWrapper<T>) XmlUtils.unmarshal(ResponseWrapper.class, response.body());
        } else {
            System.out.println(response.body());
            throw new Exception("Request failed: " + status);
        }
    }

    // Metodo per richieste non-GET (POST, PUT, DELETE) con Response
    private Response sendNonGetRequest(HttpRequest request) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        System.out.println("Status: " + status);

        if (status >= 200 && status < 300) {
            return XmlUtils.unmarshal(Response.class, response.body());
        } else {
            System.out.println(response.body());
            throw new Exception("Request failed: " + status);
        }
    }
}