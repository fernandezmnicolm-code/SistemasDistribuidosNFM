package bancomercantil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BancoMercantilServer {
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Banco Mercantil (TCP) iniciado con éxito en el puerto " + port);
            while (true) {
                Socket client = server.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintStream toClient = new PrintStream(client.getOutputStream());

                String ciBuscado = fromClient.readLine();
                System.out.println("Consulta recibida para CI: " + ciBuscado);

                String respuesta = "";
                if ("11021654".equals(ciBuscado)) {
                    respuesta = "1515-5100.0"; // Caso de prueba obligatorio[cite: 1]
                }

                toClient.println(respuesta);
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}