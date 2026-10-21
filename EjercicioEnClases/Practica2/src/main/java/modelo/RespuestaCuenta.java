package modelo;
// FERNANDEZ MENACHO NICOL MILENA
import java.io.Serializable;
import java.util.ArrayList;

public class RespuestaCuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean error;
    private String mensaje;
    private ArrayList<Cuenta> cuentas;

    public RespuestaCuenta(boolean error, String mensaje, ArrayList<Cuenta> cuentas) {
        this.error = error;
        this.mensaje = mensaje;
        this.cuentas = cuentas;
    }

    public boolean isError() { return error; }
    public String getMensaje() { return mensaje; }
    public ArrayList<Cuenta> getCuentas() { return cuentas; }
}