/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteturista;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class Voucher implements Serializable{
    boolean confirmado;
    String CodigoCompra;
    String motivo;
    double montoUSD;
    
    
    public Voucher(boolean confirmado, String CodigoCompra, String motivo){
     this.confirmado = confirmado;
     this.CodigoCompra = CodigoCompra;
     this.motivo = motivo;
     this.montoUSD = montoUSD;
    }
    
    public boolean getConfirmado(){
    return confirmado;
    }
    
    public void setConfirmado(boolean Confirmado){
    this.confirmado = Confirmado;
    }
    
    public String getcodigoCompra(){
    return CodigoCompra;
    }
    
    public void setcodigoCompra(String CodigoCompra){
    this.CodigoCompra= CodigoCompra;
    }
    
    public String motivo(){
    return motivo;
    }
    
    public void motivo(String motivo){
    this.motivo= motivo;
    }
    
    public double getMontoUSD(){
    return montoUSD;
    }
    public void serMontoUSD(double montoUSD){
    this.montoUSD = montoUSD;
    }
    
     @Override
    public String toString() {
        String codigoCompra = null;
        return "Voucher{" + "confirmado=" + confirmado + ", codigoCompra=" + codigoCompra + ", motivo=" + motivo + ", montoUSD=" + montoUSD + '}';
    }
    
}
