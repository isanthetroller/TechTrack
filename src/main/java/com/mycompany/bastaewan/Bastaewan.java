/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.bastaewan;

/**
 *
 * @author JDLM
 */
public class Bastaewan {

    public static void main(String[] args) {
        /* Launch login screen instead of dashboard */
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
