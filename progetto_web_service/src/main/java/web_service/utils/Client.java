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

    // Costruttore
    public Client(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }

    // GET: Lista delle auto
    public ResponseWrapper<Car> getCars() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=getCars")).GET().build();
        return GetRequest(request, Car.class);
    }

    // GET: Lista delle marche
    public ResponseWrapper<Brand> getBrands() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=getBrands")).GET().build();
        return GetRequest(request, Brand.class);
    }

    // GET: Lista dei clienti
    public ResponseWrapper<Customer> getCustomers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=getCustomers")).GET().build();
        return GetRequest(request, Customer.class);
    }

    // GET: Auto con dettagli (JOIN)
    public ResponseWrapper<CarWithDetails> getCarsWithDetails() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=getCarsWithDetails")).GET().build();
        return GetRequest(request, CarWithDetails.class);
    }

    // POST: Crea una nuova auto
    public Response createCar(String model, int brandId, int year, double price, String color) throws Exception {
        Car car = new Car(0, model, brandId, year, price, color);
        String body = XmlUtils.marshal(car);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=createCar")).header("Content-Type", "application/xml").POST(HttpRequest.BodyPublishers.ofString(body)).build();
        return NonGetRequest(request);
    }

    // PUT: Aggiorna un'auto
    public Response updateCar(int id, String model, double price, String color) throws Exception {
        Car car = new Car(id, model, 0, 0, price, color);
        String body = XmlUtils.marshal(car);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=updateCar&id=" + id)).header("Content-Type", "application/xml").PUT(HttpRequest.BodyPublishers.ofString(body)).build();
        return NonGetRequest(request);
    }

    // DELETE: Elimina un'auto
    public Response deleteCar(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "?action=deleteCar&id=" + id)).DELETE().build();
        return NonGetRequest(request);
    }

    // Metodo per richieste GET con ResponseWrapper<T>
    @SuppressWarnings("unchecked")
    private <T> ResponseWrapper<T> GetRequest(HttpRequest request, Class<T> itemClass) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        System.out.println("Status: " + status);

        if (status >= 200 && status < 300) {
            // Deserializza direttamente in ResponseWrapper
            ResponseWrapper<T> wrapper = XmlUtils.unmarshal(ResponseWrapper.class, response.body());
            if (wrapper.getItems() == null || wrapper.getItems().isEmpty()) {
                System.out.println("attenzione Nessun elementonell'XML!!!!");
            }
            return wrapper;
        } else {
            System.out.println("Errore: " + response.body());
            throw new Exception("Richiesta fallita con stato: " + status);
        }
    }

    // Metodo per richieste non-GET (POST, PUT, DELETE) con Response
    private Response NonGetRequest(HttpRequest request) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int stat = response.statusCode();
        System.out.println("Status: " + stat);

        if (stat >= 200 && stat < 300) {
            return XmlUtils.unmarshal(Response.class, response.body());
        } else {
            System.out.println("Errore: " + response.body());
            throw new Exception("Richiesta fallita: " + stat);
        }
    }
}