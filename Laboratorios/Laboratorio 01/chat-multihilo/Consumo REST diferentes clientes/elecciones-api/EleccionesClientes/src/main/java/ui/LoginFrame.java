/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author magne
 */
import service.AuthService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    JTextField email = new JTextField(18);
    JPasswordField password = new JPasswordField(18);

    public LoginFrame() {
        setTitle("Iniciar sesión");
        setSize(350, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JButton entrar = new JButton("Entrar");

        p.add(new JLabel("Correo:"));
        p.add(email);
        p.add(new JLabel("Contraseña:"));
        p.add(password);
        p.add(new JLabel());
        p.add(entrar);
        add(p);

        entrar.addActionListener(e -> {
            try {
                AuthService.login(email.getText().trim(),
                    new String(password.getPassword()));

                new UsuariosFrame().setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }
}