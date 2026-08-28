package servidor;

import modelo.Cuenta;
import modelo.RespuestaCuenta;
import java.rmi.RemoteException; // <--- ESTO ES LO QUE FALTA AGREGAR
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ServidorJusticia extends UnicastRemoteObject implements IJusticia {

    public ServidorJusticia() throws RemoteException {
        super();
    }

    @Override
    public RespuestaCuenta consultarCuentas(String ci, String nombres, String apellidos) throws RemoteException {
        ArrayList<Cuenta> listaCuentas = new ArrayList<>();

        // 1. Consulta al Banco Mercantil (TCP - Puerto 5000)
        try (Socket socketTcp = new Socket("localhost", 5000);
             PrintStream toServer = new PrintStream(socketTcp.getOutputStream());
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socketTcp.getInputStream()))) {
            
            toServer.println(ci);
            String respuestaMercantil = fromServer.readLine();
            
            if (respuestaMercantil != null && !respuestaMercantil.isEmpty()) {
                String[] datos = respuestaMercantil.split("-");
                listaCuentas.add(new Cuenta(Cuenta.BancoEnum.MERCANTIL, datos[0], ci, nombres, apellidos, Double.parseDouble(datos[1])));
            }
        } catch (Exception e) {
            System.out.println("Error Banco Mercantil: " + e.getMessage());
        }

        // 2. Consulta al Banco BCP (UDP - Puerto 6789)
        try (DatagramSocket socketUdp = new DatagramSocket()) {
            String trama = "CONSULTAR:" + ci;
            byte[] bTrama = trama.getBytes();
            DatagramPacket peticion = new DatagramPacket(bTrama, bTrama.length, InetAddress.getByName("localhost"), 6789);
            socketUdp.send(peticion);

            byte[] buffer = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            socketUdp.setSoTimeout(3000);
            socketUdp.receive(respuesta);

            String respuestaBcp = new String(respuesta.getData(), 0, respuesta.getLength());
            if (respuestaBcp != null && !respuestaBcp.isEmpty()) {
                String[] datos = respuestaBcp.split("-");
                listaCuentas.add(new Cuenta(Cuenta.BancoEnum.BCP, datos[0], ci, nombres, apellidos, Double.parseDouble(datos[1])));
            }
        } catch (Exception e) {
            System.out.println("Error Banco BCP: " + e.getMessage());
        }

        return new RespuestaCuenta(false, "Proceso exitoso", listaCuentas);
    }

    @Override
    public boolean congelar(Cuenta cuenta, double monto) throws RemoteException {
        return true;
    }

    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("ServidorJusticia", new ServidorJusticia());
            System.out.println("Servidor Justicia RMI iniciado con éxito en el puerto 1099...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}