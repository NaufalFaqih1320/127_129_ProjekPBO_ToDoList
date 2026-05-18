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

public class TaskController {

    TaskDAO dao;

    public TaskController() {
        dao = new TaskDAO();
    }

    public void addTask(Task task) {
        dao.insertTask(task);
    }

    public List<Task> getTasks() {
        return dao.getAllTasks();
    }

    public void updateTask(Task task) {
        dao.updateTask(task);
    }

    public void deleteTask(int id) {
        dao.deleteTask(id);
    }
}