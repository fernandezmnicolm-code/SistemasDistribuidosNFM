package servidor;

import modelo.Cuenta;
import modelo.RespuestaCuenta;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IJusticia extends Remote {
    RespuestaCuenta consultarCuentas(String ci, String nombres, String apellidos) throws RemoteException;
    boolean congelar(Cuenta cuenta, double monto) throws RemoteException;
}