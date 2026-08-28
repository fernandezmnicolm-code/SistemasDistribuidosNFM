package cliente;
// NICOL FERNANDEZ MENACHO
import modelo.Cuenta;
import modelo.RespuestaCuenta;
import servidor.IJusticia;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JuezClient {
    public static void main(String[] args) {
        try {
            // SE CONECTA A TU IP REAL DE WIFI
            Registry reg = LocateRegistry.getRegistry("10.20.210.50", 1099);
            IJusticia justicia = (IJusticia) reg.lookup("ServidorJusticia");

            // Caso de prueba obligatorio
            String ci = "11021654";
            String nombres = "Juan Perez";
            String apellidos = "Segovia";

            System.out.println("Enviando consulta judicial a la Red...");
            RespuestaCuenta resultado = justicia.consultarCuentas(ci, nombres, apellidos);

            if (!resultado.isError()) {
                System.out.println("\n--- RESULTADO DE CUENTAS ENCONTRADAS ---");
                for (Cuenta c : resultado.getCuentas()) {
                    System.out.println("Banco: " + c.getBanco() + " | Nro Cuenta: " + c.getNrocuenta() + " | Saldo: " + c.getSaldo());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}