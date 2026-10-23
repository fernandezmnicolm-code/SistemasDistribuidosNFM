/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author magne
 */
import com.fasterxml.jackson.databind.*;
import java.net.URI;
import java.net.http.*;

public class Api {
    static String URL = "http://127.0.0.1:8000/api";
    static ObjectMapper JSON = new ObjectMapper();
    static HttpClient HTTP = HttpClient.newHttpClient();
    static String token = "";

    public static JsonNode pedir(String metodo, String ruta, String cuerpo) throws Exception {
        
        HttpRequest.Builder b = HttpRequest.newBuilder()
            .uri(URI.create(URL + ruta))
            .header("Accept", "application/json");

        if (!token.isEmpty())
            b.header("Authorization", "Bearer " + token);

        if (cuerpo == null) {
            if (metodo.equals("GET")) b.GET();
            else b.DELETE();
        } else {
            b.header("Content-Type", "application/json");
            b.method(metodo,
                HttpRequest.BodyPublishers.ofString(cuerpo));
        }

        var r = HTTP.send(b.build(),
            HttpResponse.BodyHandlers.ofString());

        if (r.statusCode() < 200 || r.statusCode() >= 300)
            throw new Exception("HTTP " + r.statusCode()
                + ": " + r.body());

        return r.body().isBlank()
            ? JSON.createObjectNode()
            : JSON.readTree(r.body());
    }
}