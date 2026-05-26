/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.SwingUtilities;
import mvc.MainFrame;

// Class utama untuk menjalankan aplikasi ToDo List
public class Main {

    public static void main(String[] args) {
        // Menjalankan GUI menggunakan SwingUtilities
        SwingUtilities.invokeLater(() -> {
            // Menampilkan tampilan utama aplikasi
            new MainFrame().setVisible(true);
        });
    }
}
