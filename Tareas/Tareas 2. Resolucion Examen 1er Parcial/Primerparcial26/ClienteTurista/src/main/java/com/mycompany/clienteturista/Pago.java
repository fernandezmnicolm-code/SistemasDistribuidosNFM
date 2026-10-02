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
public class Pago implements Serializable {
     boolean aprobado;
     String codigoAutorización;
     String motivo;
    
    public Pago(boolean aprobado, String codigoAutorización, String motivo){
    this.aprobado = aprobado;
    this.codigoAutorización=codigoAutorización;
    this.motivo=motivo;
    }
    
    public boolean getAprobado(){
    return aprobado;
    }
    
    public void setAprobado(boolean aprobado){
    this.aprobado=aprobado;
    }
    
    public String getCodigoAutorizacion(){
    return codigoAutorización;
    }
    
     public void  setCodigoAutorizacion(String codigoAutorización){
    this.codigoAutorización=codigoAutorización;
    }
     
    public String getMotivo(){
    return motivo;
    }
    
    public void setMotivo(String  motivo){
    this.motivo = motivo;
    }

    public boolean isAprobado() {
    return aprobado;
}
    

    
}
