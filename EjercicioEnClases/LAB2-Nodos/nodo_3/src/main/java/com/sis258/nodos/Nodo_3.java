/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sis258.nodos;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

public class Nodo_3 {

    public static void main(String[] args) {
        String hostNodo1 = args.length > 0 ? args[0] : "192.168.125.224";
        int puertoNodo1 = 7001;
        int puertoPropio = 7003;

        try (DatagramSocket socketUDP = new DatagramSocket(puertoPropio)) {
            InetAddress direccionNodo1 = InetAddress.getByName(hostNodo1);
            System.out.println("Nodo 3 escuchando en el puerto " + puertoPropio);

            byte[] bufferRecibe = new byte[4096];

            while (true) {
                DatagramPacket peticion = new DatagramPacket(bufferRecibe, bufferRecibe.length);
                socketUDP.receive(peticion);

                String recibido = new String(peticion.getData(), 0, peticion.getLength());
                System.out.println("Recibido del Nodo 2: " + recibido);

                String resultado = procesar(recibido);

                byte[] mensaje = resultado.getBytes();
                DatagramPacket envio = new DatagramPacket(
                        mensaje, mensaje.length, direccionNodo1, puertoNodo1);

                socketUDP.send(envio);
                System.out.println("Resumen final enviado al Nodo 1");
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Recibe: texto|cantidadCaracteres|cantidadPalabras|parImpar
    // Devuelve el resumen final en texto plano
    public static String procesar(String cadena) {
        String[] partes = cadena.split("\\|");
        String texto = partes[0];
        String caracteres = partes[1];
        String palabras = partes[2];
        String parImpar = partes[3];

        String mayusculas = texto.toUpperCase();

        int vocales = 0;
        String textoMinuscula = texto.toLowerCase();
        for (int i = 0; i < textoMinuscula.length(); i++) {
            char c = textoMinuscula.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                vocales++;
            }
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("Texto original: ").append(texto).append("\n");
        resumen.append("Texto en mayusculas: ").append(mayusculas).append("\n");
        resumen.append("Cantidad de caracteres: ").append(caracteres).append("\n");
        resumen.append("Cantidad de palabras: ").append(palabras).append("\n");
        resumen.append("Cantidad de caracteres es: ").append(parImpar).append("\n");
        resumen.append("Cantidad de vocales: ").append(vocales);

        return resumen.toString();
    }
}