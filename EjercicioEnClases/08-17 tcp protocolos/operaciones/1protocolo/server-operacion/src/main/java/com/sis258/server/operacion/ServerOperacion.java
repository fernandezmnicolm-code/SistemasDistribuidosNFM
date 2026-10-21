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
            toClient.println("Introduzca el primer numero");
            String recibido = fromClient.readLine();
            System.out.println("El cliente envio el mensaje: " + recibido);
            int numero1 = Integer.parseInt(recibido);

            toClient.println("Introduzca el segundo numero");
            String recibido2 = fromClient.readLine();
            int numero2 = Integer.parseInt(recibido2);

            toClient.println("1.suma 2.resta 3.multiplicacion 4.division. Introduzca la operacion");
            String recibido3 = fromClient.readLine();

            int resultado = 0;
            boolean valido = true;
            switch (recibido3) {
                case "1": resultado = numero1 + numero2; break;
                case "2": resultado = numero1 - numero2; break;
                case "3": resultado = numero1 * numero2; break;
                case "4":
                    if (numero2 == 0) {
                        toClient.println("Error: division entre cero");
                        valido = false;
                    } else {
                        resultado = numero1 / numero2;
                    }
                    break;
                default:
                    toClient.println("Opcion invalida");
                    valido = false;
            }

            if (valido) {
                toClient.println("Resultado: " + resultado);
            }
        } catch (IOException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        } finally {
            try { client.close(); } catch (IOException e) {}
        }
    }
}