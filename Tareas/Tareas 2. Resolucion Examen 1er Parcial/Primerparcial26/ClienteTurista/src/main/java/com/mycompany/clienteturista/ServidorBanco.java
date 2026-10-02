/*¿
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteturista;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author PC
 */
public class ServidorBanco {
  public static void main(String[] a) throws Exception {
    Registry reg = LocateRegistry.createRegistry(1099);
    reg.rebind("Banco", new Banco());
  }   // el servidor queda a la escucha
}