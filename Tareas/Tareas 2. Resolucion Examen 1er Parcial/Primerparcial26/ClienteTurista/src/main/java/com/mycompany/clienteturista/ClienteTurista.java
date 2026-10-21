/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.clienteturista;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author user
 */
public class ClienteTurista {

    public static void main(String[] a) throws Exception {

        Registry reg = LocateRegistry.getRegistry(1098);
        IOperador Operador = (IOperador) reg.lookup("Operadora");

        System.out.println(Operador.ComprarTour("123", "T1", 3));  // 12

    }
}