/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package bo.edu.usfx.sockets.chat;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Sala {
    private final String nombre;
    private final Set<ManejadorChat> miembros = new CopyOnWriteArraySet<>();
    private final LinkedList<String> historial = new LinkedList<>();
    private static final int MAX_HISTORIAL = 10;

    public Sala(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public void agregar(ManejadorChat m) { miembros.add(m); }
    public void quitar(ManejadorChat m) { miembros.remove(m); }
    public int cantidadUsuarios() { return miembros.size(); }
    public Set<ManejadorChat> getMiembros() { return miembros; }

    public void difundir(String mensaje, ManejadorChat excepto) {
        guardarHistorial(mensaje);
        for (ManejadorChat m : miembros) {
            if (m != excepto) m.enviar(mensaje);
        }
    }

    private synchronized void guardarHistorial(String mensaje) {
        historial.addLast(mensaje);
        if (historial.size() > MAX_HISTORIAL) historial.removeFirst();
    }

    public synchronized List<String> getHistorial() {
        return new ArrayList<>(historial);
    }
}