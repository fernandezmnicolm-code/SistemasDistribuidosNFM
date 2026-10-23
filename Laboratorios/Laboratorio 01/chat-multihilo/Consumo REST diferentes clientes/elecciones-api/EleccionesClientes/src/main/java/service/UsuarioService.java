/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author magne
 */
import model.Usuario;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

public class UsuarioService {

    public static List<Usuario> listar() throws Exception {
        List<Usuario> lista = new ArrayList<>();

        for (JsonNode u : Api.pedir("GET", "/usuarios", null)) {
            lista.add(new Usuario(
                u.path("id").asInt(),
                u.path("name").asText(),
                u.path("email").asText(),
                u.path("role").asText()
            ));
        }
        return lista;
    }

    public static void guardar(String metodo, int id,
            String nombre, String email, String password,
            String rol) throws Exception {

        var d = Api.JSON.createObjectNode();
        d.put("name", nombre);
        d.put("email", email);

        if (password != null && !password.isBlank())
            d.put("password", password);

        if (metodo.equals("PUT"))
            d.put("role", rol);

        String ruta = "/usuarios" +
            (metodo.equals("PUT") ? "/" + id : "");

        Api.pedir(metodo, ruta, d.toString());
    }

    public static void eliminar(int id) throws Exception {
        Api.pedir("DELETE", "/usuarios/" + id, null);
    }
}

