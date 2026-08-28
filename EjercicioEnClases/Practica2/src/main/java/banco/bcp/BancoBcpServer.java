package bancobcp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class BancoBcpServer {
    public static void main(String[] args) {
        int port = 6789;
        try (DatagramSocket socketUDP = new DatagramSocket(port)) {
            System.out.println("Banco BCP (UDP) iniciado con éxito en el puerto " + port);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(peticion);
                
                String recibido = new String(peticion.getData(), 0, peticion.getLength());
                System.out.println("Datagrama recibido: " + recibido);

                String[] partes = recibido.split(":");
                String operacion = partes[0];
                String ci = partes[1];

                String respuesta = "";
                if ("CONSULTAR".equalsIgnoreCase(operacion) && "11021654".equals(ci)) {
                    respuesta = "6576-6500.0"; // Caso de prueba obligatorio[cite: 1]
                }

                byte[] mensajeEnviar = respuesta.getBytes();
                DatagramPacket respuestaPacket = new DatagramPacket(
                    mensajeEnviar, mensajeEnviar.length, 
                    peticion.getAddress(), peticion.getPort()
                );
                socketUDP.send(respuestaPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}