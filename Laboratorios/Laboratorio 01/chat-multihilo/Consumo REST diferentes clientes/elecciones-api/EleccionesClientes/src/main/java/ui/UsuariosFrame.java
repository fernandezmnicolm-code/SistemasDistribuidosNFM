/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author magne
 */
import model.Usuario;
import service.UsuarioService;
import service.Api;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuariosFrame extends JFrame {
    DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Nombre", "Correo", "Rol"}, 0
    ) {
        public boolean isCellEditable(int f, int c) { return false; }
    };

    JTable tabla = new JTable(modelo);

    public UsuariosFrame() {
        setTitle("CRUD de usuarios");
        setSize(650, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel botones = new JPanel();
        agregarBoton(botones, "Registrar", () -> crear());
        agregarBoton(botones, "Editar", () -> editar());
        agregarBoton(botones, "Eliminar", () -> eliminar());
        agregarBoton(botones, "Recargar", () -> cargar());

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        cargar();
    }

    void agregarBoton(JPanel p, String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> accion.run());
        p.add(b);
    }

    void cargar() {
        try {
            modelo.setRowCount(0);
            for (Usuario u : UsuarioService.listar())
                modelo.addRow(new Object[]{
                    u.id, u.name, u.email, u.role
                });
        } catch (Exception ex) { error(ex); }
    }

    String pedir(String texto, String valor) {
        return (String) JOptionPane.showInputDialog(this,
            texto, "Datos de usuario",
            JOptionPane.PLAIN_MESSAGE, null, null, valor);
    }

    void crear() {
        String n = pedir("Nombre:", "");
        if (n == null) return;
        String e = pedir("Correo:", "");
        if (e == null) return;
        String p = pedir("Contraseña:", "");
        if (p == null) return;

        try {
            UsuarioService.guardar("POST", 0,
                n.trim(), e.trim(), p, "user");
            cargar();
        } catch (Exception ex) { error(ex); }
    }

    void editar() {
        int f = tabla.getSelectedRow();
        if (f < 0) { aviso(); return; }

        int id = (int) modelo.getValueAt(f, 0);
        String n = pedir("Nombre:", modelo.getValueAt(f, 1).toString());
        if (n == null) return;
        String e = pedir("Correo:", modelo.getValueAt(f, 2).toString());
        if (e == null) return;
        String r = pedir("Rol (user/admin):",
                         modelo.getValueAt(f, 3).toString());
        if (r == null) return;
        String p = pedir("Nueva contraseña (vacío para conservar):", "");
        if (p == null) return;

        if (!r.equals("user") && !r.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Rol inválido.");
            return;
        }

        try {
            UsuarioService.guardar("PUT", id,
                n.trim(), e.trim(), p, r);
            cargar();
        } catch (Exception ex) { error(ex); }
    }

    void eliminar() {
        int f = tabla.getSelectedRow();
        if (f < 0) { aviso(); return; }

        int id = (int) modelo.getValueAt(f, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar usuario?",
            "Confirmar", JOptionPane.YES_NO_OPTION)
            != JOptionPane.YES_OPTION) return;

        try {
            UsuarioService.eliminar(id);
            cargar();
        } catch (Exception ex) { error(ex); }
    }

    void aviso() {
        JOptionPane.showMessageDialog(this,
            "Selecciona un usuario de la tabla.");
    }

    void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
