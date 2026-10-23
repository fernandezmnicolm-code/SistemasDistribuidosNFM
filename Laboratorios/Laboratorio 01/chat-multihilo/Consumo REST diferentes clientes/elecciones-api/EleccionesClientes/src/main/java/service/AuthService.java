/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author magne
 */
import com.fasterxml.jackson.databind.JsonNode;

public class AuthService {
    public static String login(String email, String password)throws Exception {

        var datos = Api.JSON.createObjectNode();
        datos.put("email", email);
        datos.put("password", password);

        JsonNode r = Api.pedir("POST", "/login", datos.toString());

        if (!r.path("usuario").path("role").asText().equals("admin"))
            throw new Exception("Se requiere una cuenta admin.");

        Api.token = r.path("token").asText();

        if (Api.token.isEmpty())
            throw new Exception("La API no devolvió el token.");

        return r.path("usuario").path("name").asText();
    }
}

