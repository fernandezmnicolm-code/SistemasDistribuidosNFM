/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteturista;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class Banco extends UnicastRemoteObject
        implements IBanco {

    public Banco() throws RemoteException {
        super();
    }

    @Override
    public Pago Debitar(String pasaporte, double montoUSD) throws RemoteException {

        // Llamar al servidor antifraue (UDP)
        String respuesta = ConsultarAntifraude(pasaporte, montoUSD);
      if (respuesta.equals("bajo"))
      {
          return new Pago(true,"AA3","no tiene riesto");
      }
          return new Pago(false,"","tiene riesgo");
        
    }

    public String ConsultarAntifraude(String pasaporte, double montoUSD) {
        int puerto = 6789;
       
        try {
            String ip = "localhost";
            DatagramSocket socketUDP = new DatagramSocket();
            String dato = "riesgo: " + pasaporte + "-" + montoUSD;
            System.out.print("Banco envia a Antifraude: " + dato);
          
            byte[] mensaje = dato.getBytes();
            InetAddress hostServidor = InetAddress.getByName(ip);
           

            // Construimos un datagrama para enviar el mensaje al servidor
            DatagramPacket peticion
                    = new DatagramPacket(mensaje, dato.length(), hostServidor,
                            puerto);

            // Enviamos el datagrama
            socketUDP.send(peticion);

            // Construimos el DatagramPacket que contendrá la respuesta
            byte[] bufer = new byte[1000];
            DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
            socketUDP.receive(respuesta);

            String cadena=new String(respuesta.getData(), 0, respuesta.getLength());
            // Enviamos la respuesta del servidor a la salida estandar
           

            // Cerramos el socket
            socketUDP.close();
            return cadena;
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        return "bajo";
    }

}