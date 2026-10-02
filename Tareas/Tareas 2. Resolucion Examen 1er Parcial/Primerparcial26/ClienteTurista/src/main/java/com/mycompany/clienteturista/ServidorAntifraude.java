/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteturista;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author user
 */
public class ServidorAntifraude {

    
  public static void main (String args[]) { 
    int port=6789;  
    try {
      
      DatagramSocket socketUDP = new DatagramSocket(port);
      byte[] bufer = new byte[1000];

      while (true) {
        // Construimos el DatagramPacket para recibir peticiones
        DatagramPacket peticion =
          new DatagramPacket(bufer, bufer.length);

        // Leemos una petición del DatagramSocket
        socketUDP.receive(peticion);

        System.out.print("Datagrama recibido del host: " +
                           peticion.getAddress());
        System.out.println(" desde enl puerto remoto: " +
                           peticion.getPort());
        
        
        String cadena =new String (peticion.getData(),0,peticion.getLength());
        String response=procesar(cadena);
       
        byte[] mensaje = response.getBytes();
              

        DatagramPacket respuesta =
          new DatagramPacket(mensaje, mensaje.length,
                             peticion.getAddress(), peticion.getPort());

        // Enviamos la respuesta, que es un eco
        socketUDP.send(respuesta);
      }

    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }
  public  static String procesar(String cadena)
        {
            String[] comando=cadena.split(":");
            String consulta=comando[1];
            String[] comando2=consulta.split("-");
            double monto=Double.parseDouble(comando2[1]);
            if (monto>1000 )
            {
                return "alto";
            }
            else
            {
                return "bajo";
            }
        }

}