/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.sockets.chat;


import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Manejador implements Runnable {
    // Colección ESTÁTICA: compartida por todos los hilos (Paso 9)
    private static final Set<Manejador> CLIENTES = new CopyOnWriteArraySet<>();

    private final Socket cliente;
    private final int id;
    private PrintWriter salida; // para poder acceder a él en difundir()

    public Manejador(Socket cliente, int id) {
        this.cliente = cliente;
        this.id = id;
    }

    @Override
    public void run() { // se ejecuta en OTRO hilo
        String hilo = Thread.currentThread().getName();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(cliente.getInputStream()));
             PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)) {

            this.salida = out;
            CLIENTES.add(this); // Agregamos este cliente a la lista global

            out.println("Bienvenido. Le atiende el hilo: " + hilo);

            String linea;
            while ((linea = in.readLine()) != null) {
                System.out.println("[" + hilo + "] cliente " + id + ": " + linea);
                // En lugar de ECO, ahora difundimos a todos (Paso 9)
                difundir("cliente-" + id + "> " + linea);
            }
        } catch (IOException e) {
            System.err.println("Error con el cliente " + id + ": " + e.getMessage());
        } finally {
            CLIENTES.remove(this); // Lo quitamos si se desconecta
            try {
                cliente.close();
            } catch (IOException e) {
                // ignorado
            }
            System.out.println("Cliente " + id + " desconectado");
        }
    }

    // Método para enviar el mensaje a todos los demás clientes
    private void difundir(String mensaje) {
        for (Manejador m : CLIENTES) {
            if (m != this && m.salida != null) {
                m.salida.println(mensaje);
            }
        }
    }
}