/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteturista;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author user
 */
public class ServidorOperadora {
  public static void main(String[] a) throws Exception {
    Registry reg = LocateRegistry.createRegistry(1098);
    reg.rebind("Operadora", new Operador());
    System.out.println(
                "Servidor Operadora listo en puerto 1098..."
        );
  }   // el servidor queda a la escucha
}