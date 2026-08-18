/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.sockets.chat;

import java.io.*;
import java.net.*;

/**
 *
 * @author PC
 */
public class ServidorSecuencial {
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(5000);
        System.out.println("Servidor escuchando en el puerto 5000...");

        while (true) {
            System.out.println("accept() ... esperando un cliente");
            Socket cliente = servidor.accept(); // BLOQUEA
            System.out.println("Conectado: " + cliente.getInetAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(cliente.getInputStream()));
            PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);

            String linea;
            while ((linea = in.readLine()) != null) { // BLOQUEA
                System.out.println("Recibido: " + linea);
                out.println("ECO: " + linea);
            }
            cliente.close();
        }
    }
}


