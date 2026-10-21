/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.clienteturista;

/**
 *
 * @author user
 */
public class ClienteTurista {

    private Voucher ComprarTour;
    private String pasaporte; 
    private String codigoTour;
    private int persona;
    public ClienteTurista(Voucher ComprarTour, String pasaporte, String codigoTour, int persona){
    this.ComprarTour = ComprarTour;
    this.pasaporte = pasaporte;
    this.codigoTour = codigoTour;
    this.persona = persona;
    } 
    
    public Voucher getComprarTour( String pasaporte, String codigoTour, int personas){
        System.out.println("Voucher Recibido");
    return ComprarTour;
    }
            
            
            
            
            
            
            
}