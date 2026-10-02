/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteturista;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author user
 */
public class Operador extends UnicastRemoteObject implements IOperador{

    
    public Operador() throws RemoteException{
      super();
    }
    @Override
    public Voucher ComprarTour(String pasaporte, String codigoTour, int personas) {

        // llamar a migracion
        String migracion = consultarMgiracion(pasaporte);
        System.out.println("Migracion respondio: " + migracion);
        double precio = 0.0;
        String[] respuesta = migracion.split(":");
        if(respuesta.length > 1 && respuesta[0].equalsIgnoreCase("valido")){
        if(respuesta[1].equalsIgnoreCase("Bolivia")){
        precio = personas * 180 * 0.5;
        }else{
            precio = personas*180;
        }
        System.out.println("Precio calculado: " + precio);
        
        ///
        try {
                Registry reg = LocateRegistry.getRegistry(1099);

                IBanco banco = (IBanco) reg.lookup("Banco");
                if (banco.Debitar(pasaporte, precio).isAprobado()) {
                    return new Voucher(true, "C-001", "");
                } else {
                    return new Voucher(false, "", "No aprobad el credito");
                }
            } catch (RemoteException ex) {
                System.getLogger(Operador.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (NotBoundException ex) {
                System.getLogger(Operador.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        return new Voucher(false, "", "Passapote invalido");
    }

    public String consultarMgiracion(String pasaporte) {
        try {
            int port = 5002;
            Socket client = new Socket("localhost", port);
            PrintStream toServer = new PrintStream(client.getOutputStream());
            BufferedReader fromServer = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            toServer.println("pasaporte:" + pasaporte);
            String result = fromServer.readLine();
            client.close();
            return result;

        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
        return "";
    }
    
}
