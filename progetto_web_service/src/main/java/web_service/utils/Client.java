package web_service.utils;

import java.net.http.HttpClient;

public class Client {
    	private String baseUrl;
	private HttpClient client;

    public Client(String baseUrl){
		this.baseUrl = baseUrl;
		this.client = HttpClient.newHttpClient();
    }

}
