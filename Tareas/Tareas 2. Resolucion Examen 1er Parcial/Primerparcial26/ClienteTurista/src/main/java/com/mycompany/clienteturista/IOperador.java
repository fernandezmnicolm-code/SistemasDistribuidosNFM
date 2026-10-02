/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteturista;

/**
 *
 * @author PC
 */
import java.rmi.*;
public interface IOperador extends Remote {
  public  Voucher ComprarTour(String pasaporte, String codigoTour, int personas) throws RemoteException;
}