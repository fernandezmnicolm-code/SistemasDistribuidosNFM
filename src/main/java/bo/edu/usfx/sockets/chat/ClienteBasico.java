/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.sockets.chat;

import java.io.*;
import java.net.*;

public class ClienteBasico {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        Socket socket = new Socket(host, 5000); // saludo de 3 vias
        System.out.println("Conectado. Puerto local: " + socket.getLocalPort());

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        BufferedReader teclado = new BufferedReader(
                new InputStreamReader(System.in));

        String texto;
        while ((texto = teclado.readLine()) != null) {
            out.println(texto);
            System.out.println("Servidor: " + in.readLine());
        }
        socket.close();
    }
}
