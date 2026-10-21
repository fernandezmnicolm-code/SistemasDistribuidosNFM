/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.practica.practica2;

/**
 *
 * @author Sebastian
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

public class nodo2 {

    public static void main(String[] args) {
        String hostNodo3 = args.length > 0 ? args[0] : "192.168.125.150";
        int puertoNodo3 = 7003;
        int puertoPropio = 7002;

        try (DatagramSocket socketUDP = new DatagramSocket(puertoPropio)) {
            InetAddress direccionNodo3 = InetAddress.getByName(hostNodo3);
            System.out.println("Nodo 2 escuchando en el puerto " + puertoPropio);

            byte[] bufferRecibe = new byte[4096];

            while (true) {
                DatagramPacket peticion = new DatagramPacket(bufferRecibe, bufferRecibe.length);
                socketUDP.receive(peticion);

                String recibido = new String(peticion.getData(), 0, peticion.getLength());
                System.out.println("Recibido del Nodo 1: " + recibido);

                String resultado = procesar(recibido);

                byte[] mensaje = resultado.getBytes();
                DatagramPacket envio = new DatagramPacket(
                        mensaje, mensaje.length, direccionNodo3, puertoNodo3);

                socketUDP.send(envio);
                System.out.println("Enviado al Nodo 3: " + resultado);
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String procesar(String cadena) {
        String[] partes = cadena.split("\\|");
        String texto = partes[0];
        String caracteres = partes[1];

        String textoLimpio = texto.trim();
        int cantidadPalabras = textoLimpio.isEmpty() ? 0 : textoLimpio.split("\\s+").length;

        int cantidadCaracteres = Integer.parseInt(caracteres);
        String parImpar = (cantidadCaracteres % 2 == 0) ? "par" : "impar";

        return texto + "|" + caracteres + "|" + cantidadPalabras + "|" + parImpar;
    }
}