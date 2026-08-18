/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.server.operacion;

import java.io.*;
import java.net.*;

public class ClienrteOperacion {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        Socket socket = new Socket(host, 5002);

        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream toServer = new PrintStream(socket.getOutputStream());
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escriba la operacion (ej: 5 + 3) y presione Enter:");
        String linea = teclado.readLine();
        toServer.println(linea);

        System.out.println(fromServer.readLine());

        socket.close();
    }
}