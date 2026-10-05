/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.jgroups.segundoejerciciop;

import java.io.Serializable;

public class MensajeroAforo implements Serializable {

    public enum Tipo {
        SOLICITUD,
        ACEPTADO,
        RECHAZADO,
        SALIDA
    }

    private Tipo tipo;
    private String puerta;
    private int personas;

    public MensajeroAforo(Tipo tipo, String puerta, int personas) {
        this.tipo = tipo;
        this.puerta = puerta;
        this.personas = personas;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getPuerta() {
        return puerta;
    }

    public int getPersonas() {
        return personas;
    }
}