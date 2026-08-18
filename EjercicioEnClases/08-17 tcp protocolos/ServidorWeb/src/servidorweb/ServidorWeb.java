package servidorweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorWeb {

    public static void main(String[] args) {
        int port = 80;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto " + port);

            while (true) {
                Socket client = server.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());

                // Multihilo: cada cliente en su propio hilo
                Thread hilo = new Thread(() -> atenderCliente(client));
                hilo.start();
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void atenderCliente(Socket client) {
        try (
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream toClient = new PrintStream(client.getOutputStream())
        ) {
            // Leer la peticion HTTP
            String linea = fromClient.readLine();
            System.out.println("Peticion: " + linea);

            // Leer y descartar el resto de headers
            String header;
            while ((header = fromClient.readLine()) != null && !header.isEmpty()) {
                System.out.println(header);
            }

            // Construir la respuesta HTML
            String cuerpo = "<html><head><title>Servidor Web Java</title></head>"
                          + "<body><h1>Hola desde mi Servidor Web!</h1>"
                          + "<p>Servidor funcionando correctamente.</p></body></html>";

            // Enviar respuesta HTTP correcta
            toClient.println("HTTP/1.1 200 OK");
            toClient.println("Content-Type: text/html; charset=UTF-8");
            toClient.println("Content-Length: " + cuerpo.getBytes().length);
            toClient.println("Connection: close");
            toClient.println(); // linea en blanco obligatoria entre headers y cuerpo
            toClient.println(cuerpo);
            toClient.flush();

            System.out.println("Respuesta enviada al cliente.");

        } catch (IOException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("Error cerrando socket: " + e.getMessage());
            }
        }
    }

}
