/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.sockets.chat;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorChat {
    // Estado COMPARTIDO por todos los hilos del servidor
    static final Map<String, Sala> SALAS = new ConcurrentHashMap<>();
    static final Set<String> NICKS_USADOS = ConcurrentHashMap.newKeySet();
    // AtomicInteger en vez de int: un int normal puede "perder" incrementos si dos
    // hilos leen el mismo valor antes de que ninguno alcance a escribir el nuevo
    static final AtomicInteger CONTADOR_HISTORICO = new AtomicInteger(0);
    static final AtomicInteger CONECTADOS_ACTUALES = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        int hilos = args.length > 1 ? Integer.parseInt(args[1]) : 4;

        SALAS.put("general", new Sala("general")); // sala por defecto

        ServerSocket servidor = new ServerSocket(puerto);
        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        System.out.println("Servidor de chat en el puerto " + puerto + " con " + hilos + " hilos");

        while (true) {
            Socket cliente = servidor.accept(); // solo acepta
            int idConexion = CONTADOR_HISTORICO.incrementAndGet();
            System.out.println("Conexion #" + idConexion + " desde "
                    + cliente.getInetAddress().getHostAddress());
            pool.execute(new ManejadorChat(cliente, idConexion)); // y delega
        }
    }
}