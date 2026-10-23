/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sis258.eleccionesclientes;

/**
 *
 * @author magne
 */
import javax.swing.SwingUtilities;
import ui.LoginFrame;


public class EleccionesClientes {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new LoginFrame().setVisible(true)
        );
    }
}
