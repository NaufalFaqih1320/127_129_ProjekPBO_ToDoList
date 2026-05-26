/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */

import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Class TaskDAO digunakan untuk operasi CRUD database
public class TaskDAO {
    
    // Membuat variabel koneksi database
    Connection conn;
    
    // Constructor TaskDAO
    // Digunakan untuk menghubungkan aplikasi ke database
    public TaskDAO() {
        
        // Memanggil method connect() dari DBConnection
        conn = DBConnection.connect();
    }
    
    // Method untuk menambahkan task ke database
    public void insertTask(Task task) {
        // Query SQL insert data
        String sql = "INSERT INTO tasks(title, description, deadline, completed, type) VALUES(?,?,?,?,?)";

        // Try-with-resources digunakan agar PreparedStatement otomatis ditutup
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Mengisi parameter query berdasarkan data task
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getDeadline());
            ps.setBoolean(4, task.isCompleted());
            ps.setString(5, task.getType());
            
            // Menjalankan query insert
            ps.executeUpdate();
        } catch (Exception e) {
            
            // Menampilkan error jika terjadi masalah
            e.printStackTrace();
        }
    }
    
    // Method untuk mengambil seluruh data task
    public List<Task> getAllTasks() {
        
        // Membuat list untuk menyimpan data task
        List<Task> tasks = new ArrayList<>();
        
        // Query SQL SELECT untuk mengambil seluruh data task
        // Data diurutkan berdasarkan deadline terdekat
        String sql = "SELECT * FROM tasks ORDER BY STR_TO_DATE(deadline, '%d %b %Y') ASC";
        
        // Membuat Statement dan menjalankan query
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            // Looping membaca seluruh data hasil query
            while (rs.next()) {
                
                // Mengambil tipe task dari database
                String type = rs.getString("type");
                // Membuat object task
                Task task;
                
                // Jika tipe task adalah Tugas
                if ("Tugas".equals(type)) {
                    // Membuat object TugasTask
                    task = new TugasTask(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("deadline"),
                            rs.getBoolean("completed")
                    );
                } else {
                    // Membuat object AcaraTask
                    task = new AcaraTask(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("deadline"),
                            rs.getBoolean("completed")
                    );
                }
                // Menambahkan object task ke dalam list
                tasks.add(task);
            }
        } catch (Exception e) {
            // Menampilkan error jika terjadi masalah
            e.printStackTrace();
        }
        // Mengembalikan list task
        return tasks;
    }
    
    // Method untuk mengupdate data task di database
    public void updateTask(Task task) {
        // Query SQL UPDATE
        String sql = "UPDATE tasks SET title=?, description=?, deadline=?, completed=?, type=? WHERE id=?";
        
        // Menyiapkan query
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getDeadline());
            ps.setBoolean(4, task.isCompleted());
            ps.setString(5, task.getType());      
            ps.setInt(6, task.getId());
            
            // Menjalankan query update
            ps.executeUpdate();
        } catch (Exception e) {
            // Menampilkan error jika update gagal
            e.printStackTrace();
        }
    }
    
    // Method untuk menghapus task berdasarkan id
    public void deleteTask(int id) {
        // Query SQL DELETE
        String sql = "DELETE FROM tasks WHERE id=?";
        
        // Menyiapkan query
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Mengisi parameter id task
            ps.setInt(1, id);
            // Menjalankan query delete
            ps.executeUpdate();
        } catch (Exception e) {
            // Menampilkan error jika delete gagal
            e.printStackTrace();
        }
    }
}
