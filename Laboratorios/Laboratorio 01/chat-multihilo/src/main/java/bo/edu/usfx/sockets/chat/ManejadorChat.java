/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.edu.usfx.sockets.chat;

import java.io.*;
import java.net.*;

public class ManejadorChat implements Runnable {
    private final Socket cliente;
    private final int id;
    private PrintWriter salida;
    private String nick;
    private Sala salaActual;

    public ManejadorChat(Socket cliente, int id) {
        this.cliente = cliente;
        this.id = id;
        this.nick = "usuario" + id;
    }

    public synchronized void enviar(String mensaje) {
        if (salida != null) salida.println(mensaje);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)) {

            this.salida = out;
            ServidorChat.CONECTADOS_ACTUALES.incrementAndGet();

            salaActual = ServidorChat.SALAS.get("general");
            salaActual.agregar(this);
            salaActual.difundir("* " + nick + " se unio a la sala", this);

            enviar("Bienvenido. Tu apodo temporal es: " + nick + ". Usa /nick <apodo> para cambiarlo.");

            String linea;
            while ((linea = in.readLine()) != null) {
                if (linea.startsWith("/")) {
                    procesarComando(linea.trim());
                } else if (salaActual != null) {
                    salaActual.difundir(nick + "> " + linea, this);
                }
            }
        } catch (IOException e) {
            System.err.println("Error con el cliente " + id + ": " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    private void procesarComando(String linea) {
        String[] partes = linea.split(" ", 2);
        String comando = partes[0].toLowerCase();
        String resto = partes.length > 1 ? partes[1].trim() : "";

        switch (comando) {
            case "/nick": cambiarNick(resto); break;
            case "/salas": listarSalas(); break;
            case "/crear": crearSala(resto); break;
            case "/unirse": unirseSala(resto); break;
            case "/quien": listarUsuarios(); break;
            case "/privado": mensajePrivado(resto); break;
            case "/estado": mostrarEstado(); break;
            case "/historial": mostrarHistorial(); break;
            case "/salir": enviar("Hasta luego, " + nick); try { cliente.close(); } catch (IOException e) {} break;
            default: enviar("Comando desconocido: " + comando);
        }
    }

    private void cambiarNick(String nuevoNick) {
        if (nuevoNick.isEmpty()) { enviar("Uso: /nick <apodo>"); return; }
        synchronized (ServidorChat.NICKS_USADOS) {
            if (ServidorChat.NICKS_USADOS.contains(nuevoNick)) {
                enviar("El apodo '" + nuevoNick + "' ya esta en uso. Elige otro.");
                return;
            }
            ServidorChat.NICKS_USADOS.remove(nick);
            ServidorChat.NICKS_USADOS.add(nuevoNick);
        }
        String anterior = nick;
        nick = nuevoNick;
        enviar("Tu apodo ahora es: " + nick);
        if (salaActual != null) salaActual.difundir("* " + anterior + " ahora se llama " + nick, this);
    }

    private void listarSalas() {
        StringBuilder sb = new StringBuilder("Salas disponibles:");
        for (Sala s : ServidorChat.SALAS.values()) {
            sb.append("\n - ").append(s.getNombre()).append(" (").append(s.cantidadUsuarios()).append(" usuarios)");
        }
        enviar(sb.toString());
    }

    private void crearSala(String nombreSala) {
        if (nombreSala.isEmpty()) { enviar("Uso: /crear <nombre-sala>"); return; }
        Sala nueva = new Sala(nombreSala);
        Sala existente = ServidorChat.SALAS.putIfAbsent(nombreSala, nueva);
        if (existente != null) { enviar("La sala '" + nombreSala + "' ya existe."); return; }
        cambiarDeSala(nueva);
        enviar("Sala '" + nombreSala + "' creada. Ahora estas en ella.");
    }

    private void unirseSala(String nombreSala) {
        if (nombreSala.isEmpty()) { enviar("Uso: /unirse <nombre-sala>"); return; }
        Sala destino = ServidorChat.SALAS.get(nombreSala);
        if (destino == null) { enviar("La sala '" + nombreSala + "' no existe. Usa /salas."); return; }
        cambiarDeSala(destino);
        enviar("Te uniste a la sala '" + nombreSala + "'.");
    }

    private void cambiarDeSala(Sala destino) {
        if (salaActual != null) {
            salaActual.quitar(this);
            salaActual.difundir("* " + nick + " salio de la sala", this);
        }
        salaActual = destino;
        salaActual.agregar(this);
        salaActual.difundir("* " + nick + " se unio a la sala", this);
    }

    private void listarUsuarios() {
        if (salaActual == null) return;
        StringBuilder sb = new StringBuilder("Usuarios en '" + salaActual.getNombre() + "':");
        for (ManejadorChat m : salaActual.getMiembros()) sb.append("\n - ").append(m.nick);
        enviar(sb.toString());
    }

    private void mensajePrivado(String resto) {
        String[] partes = resto.split(" ", 2);
        if (partes.length < 2) { enviar("Uso: /privado <apodo> <mensaje>"); return; }
        ManejadorChat destino = buscarPorNick(partes[0]);
        if (destino == null) { enviar("Usuario '" + partes[0] + "' no encontrado."); return; }
        destino.enviar("[privado de " + nick + "] " + partes[1]);
        enviar("[privado a " + partes[0] + "] " + partes[1]);
    }

    private ManejadorChat buscarPorNick(String buscado) {
        for (Sala s : ServidorChat.SALAS.values())
            for (ManejadorChat m : s.getMiembros())
                if (m.nick.equals(buscado)) return m;
        return null;
    }

    private void mostrarEstado() {
        enviar("Conectados: " + ServidorChat.CONECTADOS_ACTUALES.get()
                + " | Total historico: " + ServidorChat.CONTADOR_HISTORICO.get()
                + " | Salas: " + ServidorChat.SALAS.size());
    }

    private void mostrarHistorial() {
        if (salaActual == null) return;
        for (String m : salaActual.getHistorial()) enviar(m);
    }

    private void desconectar() {
        ServidorChat.CONECTADOS_ACTUALES.decrementAndGet();
        synchronized (ServidorChat.NICKS_USADOS) { ServidorChat.NICKS_USADOS.remove(nick); }
        if (salaActual != null) {
            salaActual.quitar(this);
            salaActual.difundir("* " + nick + " se desconecto", this);
        }
        try { cliente.close(); } catch (IOException e) {}
        System.out.println("Cliente " + id + " (" + nick + ") desconectado");
    }
}