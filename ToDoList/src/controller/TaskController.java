/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author ASUS
 */
import java.util.List;
import model.Task;
import model.TaskDAO;

// Class TaskController berfungsi sebagai penghubung antara tampilan (View) dan data (Model).
// Class ini untuk proses tambah, ambil, update, dan hapus data task.
public class TaskController {

    private final TaskDAO dao; // Objek DAO digunakan untuk mengakses database

    public TaskController() { // Constructor untuk membuat objek DAO
        dao = new TaskDAO();
    }

    public void addTask(Task task) { // Method untuk menambahkan task baru
        dao.insertTask(task);
    }

    public List<Task> getTasks() { // Method untuk mengambil seluruh data task
        return dao.getAllTasks();
    }

    public void updateTask(Task task) { // Method untuk mengupdate data task
        dao.updateTask(task);
    }

    public void deleteTask(int id) { // Method untuk menghapus task berdasarkan id
        dao.deleteTask(id);
    }
}
