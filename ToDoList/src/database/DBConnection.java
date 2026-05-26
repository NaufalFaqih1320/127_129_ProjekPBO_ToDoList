/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author ASUS
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // URL database MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/todolist_db";
    
    // Username database
    private static final String USER = "root";
    
    // Password database
    private static final String PASSWORD = "";

    // Method untuk mendapatkan koneksi database
    public static Connection connect() {
        try {
            // Membuat koneksi ke database menggunakan JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
