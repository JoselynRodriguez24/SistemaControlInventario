/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HP
 */
import com.formdev.flatlaf.FlatLightLaf;
import presentacion.MainFrame;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception ignored) {
                // Si falla, se usa el look and feel por defecto.
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}