/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sis258.nodos;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Scanner;

public class Nodo1 {

    public static void main(String[] args) {
        String hostNodo2 = args.length > 0 ? args[0] : "192.168.125.34";
        int puertoNodo2 = 7002;
        int puertoPropio = 7001;

        try (DatagramSocket socketUDP = new DatagramSocket(puertoPropio)) {
            InetAddress direccionNodo2 = InetAddress.getByName(hostNodo2);

            Scanner teclado = new Scanner(System.in);
            System.out.print("Ingrese una palabra o frase: ");
            System.out.flush();
            String texto = teclado.nextLine();

            int cantidadCaracteres = texto.length();

            // Protocolo: texto|cantidadCaracteres
            String mensajeTexto = texto + "|" + cantidadCaracteres;
            byte[] mensaje = mensajeTexto.getBytes();

            DatagramPacket peticion = new DatagramPacket(
                    mensaje, mensaje.length, direccionNodo2, puertoNodo2);

            socketUDP.send(peticion);
            System.out.println("Enviado al Nodo 2: \"" + texto + "\" (" + cantidadCaracteres + " caracteres)");
            System.out.println("Esperando resultado final del Nodo 3...");

            byte[] bufferRecibe = new byte[4096];
            DatagramPacket respuesta = new DatagramPacket(bufferRecibe, bufferRecibe.length);

            socketUDP.receive(respuesta);

            String resultadoFinal = new String(respuesta.getData(), 0, respuesta.getLength());

            System.out.println("\n===== RESULTADO FINAL =====");
            System.out.println(resultadoFinal);
            System.out.println("============================");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
