package modelo;
// FERNANDEZ MENACHO NICOL MILENA
import java.io.Serializable;

public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum BancoEnum { MERCANTIL, BCP }
    
    private BancoEnum banco;
    private String nrocuenta;
    private String ci;
    private String nombres;
    private String apellidos;
    private double saldo;

    public Cuenta(BancoEnum banco, String nrocuenta, String ci, String nombres, String apellidos, double saldo) {
        this.banco = banco;
        this.nrocuenta = nrocuenta;
        this.ci = ci;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.saldo = saldo;
    }

    public BancoEnum getBanco() { return banco; }
    public String getNrocuenta() { return nrocuenta; }
    public String getCi() { return ci; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public double getSaldo() { return saldo; }
}