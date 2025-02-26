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
    private final String baseUrl;
    private final HttpClient client;

    // Costruttore della classe client 
    public Client(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }

    // GET  Lista delle auto
    public ResponseWrapper<Car> getCars() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=getCars")).GET().build();
        return GetRequest(request, Car.class);
    }

    // GET Lista delle marche
    public ResponseWrapper<Brand> getBrands() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=getBrands")).GET().build();
        return GetRequest(request, Brand.class);
    }

    // GET Lista dei clienti
    public ResponseWrapper<Customer> getCustomers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=getCustomers")).GET().build();
        return GetRequest(request, Customer.class);
    }

    // GET: Auto con dettagli 
    public ResponseWrapper<CarWithDetails> getCarsWithDetails() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=getCarsWithDetails")).GET().build();
        return GetRequest(request, CarWithDetails.class);
    }

    // POST Crea una nuova auto
    public Response createCar(String model, int brandId, int year, double price, String color) throws Exception {
        Car car = new Car(0, model, brandId, year, price, color);
        String body = XmlUtils.marshal(car);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=createCar")).header("Content-Type", "application/xml").POST(HttpRequest.BodyPublishers.ofString(body)).build();
        return GetRequest(request);
    }

    // PUT Aggiorna auto
    public Response updateCar(int id, String model, double price, String color) throws Exception {
        Car car = new Car(id, model, 0, 0, price, color);
        String body = XmlUtils.marshal(car);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=updateCar&id=" + id)).header("Content-Type", "application/xml").PUT(HttpRequest.BodyPublishers.ofString(body)).build();
        return GetRequest(request);
    }

    // DELETE Elimina auto
    public Response deleteCar(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=deleteCar&id=" + id)).DELETE().build();
        return GetRequest(request);
    }

    // Metodo per richieste GET con ResponseWrapper<T>
    private <T> ResponseWrapper<T> GetRequest(HttpRequest request, Class<T> itemClass) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        System.out.println("Status: " + status);

        // Stampa l'XML 
        String xmlResponse = response.body();
        System.out.println("Raw XML Response:\n" + formatXml(xmlResponse));

        if (status >= 200 && status < 300) {
            // Deserializza con respwrapper
            ResponseWrapper<T> wrapper = XmlUtils.unmarshal(ResponseWrapper.class, xmlResponse);
            if (wrapper.getItems() == null || wrapper.getItems().isEmpty()) {
                System.out.println("Nessun elemento nel XML");
            }
            return wrapper;
        } else {
            System.out.println("Errore: " + xmlResponse);
            throw new Exception("Richiesta fallita stato: " + status);
        }
    }

    // Metodo per richieste POST, PUT, DELETE con Response
    private Response GetRequest(HttpRequest request) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        System.out.println("Status: " + status);

        // stampa xml 
        String xmlResponse = response.body();
        System.out.println("Raw XML Response:\n" + formatXml(xmlResponse));

        //se il status code e tra 200 e 300 allora deserializza in response
        if (status >= 200 && status < 300) {
            return XmlUtils.unmarshal(Response.class, xmlResponse);
        } else {
            System.out.println("Errore: " + xmlResponse);
            // in caso stampo l'errore della richiesta 
            throw new Exception("Richiesta fallita: " + status);
        }
    }

    //ogni volta che trova un >< va a capo 
    private String formatXml(String xml) {
        return xml.replace("><", ">\n<"); 
    }
}