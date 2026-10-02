/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clienteturista;

/**
 *
 * @author PC
 */
import java.rmi.*;

public interface IBanco
        extends Remote {
    public Pago Debitar(String pasaporte, double montoUSD)  throws RemoteException;
}
