/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sis258.server.operacion;

import java.io.*;
import java.net.*;

public class ServerOperacion {
    public static void main(String[] args) {
        int port = 5002;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Se inicio el servidor con exito");
            while (true) {
                Socket client = server.accept();
                System.out.println("Cliente se conecto");
                atenderCliente(client);
            }
        } catch (IOException ex) {
            System.out.println("Error en el servidor: " + ex.getMessage());
        }
    }

    private static void atenderCliente(Socket client) {
        try (
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream toClient = new PrintStream(client.getOutputStream())
        ) {
            String recibido = fromClient.readLine();
            System.out.println("El cliente envio el mensaje: " + recibido);
            toClient.println(procesarSolicitud(recibido));
        } catch (IOException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        } finally {
            try { client.close(); } catch (IOException e) {}
        }
    }

    public static String procesarSolicitud(String cadena) {
        try {
            String[] partes = cadena.trim().split(" ");
            if (partes.length != 3) return "Error: formato invalido. Use: numero operador numero (ej: 5 + 3)";
            double n1 = Double.parseDouble(partes[0]);
            String op = partes[1];
            double n2 = Double.parseDouble(partes[2]);
            double resultado;
            switch (op) {
                case "+": resultado = n1 + n2; break;
                case "-": resultado = n1 - n2; break;
                case "*": resultado = n1 * n2; break;
                case "/":
                    if (n2 == 0) return "Error: division entre cero";
                    resultado = n1 / n2; break;
                default: return "Error: operador '" + op + "' no reconocido. Use + - * /";
            }
            return "Resultado: " + resultado;
        } catch (NumberFormatException e) {
            return "Error: numeros invalidos";
        }
    }
}