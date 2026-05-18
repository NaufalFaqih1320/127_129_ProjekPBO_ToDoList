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

public class TaskDAO {

    Connection conn;

    public TaskDAO() {
        conn = DBConnection.connect();
    }

    public void insertTask(Task task) {
        String sql = "INSERT INTO tasks(title, description, deadline, completed, type) VALUES(?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getDeadline());
            ps.setBoolean(4, task.isCompleted());
            ps.setString(5, task.getType());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY STR_TO_DATE(deadline, '%d %b %Y') ASC";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String type = rs.getString("type");
                Task task;

                if ("Tugas".equals(type)) {
                    task = new TugasTask(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("deadline"),
                            rs.getBoolean("completed")
                    );
                } else {
                    task = new AcaraTask(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("deadline"),
                            rs.getBoolean("completed")
                    );
                }
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ==================== PERBAIKAN DISINI ====================
    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET title=?, description=?, deadline=?, completed=?, type=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getDeadline());
            ps.setBoolean(4, task.isCompleted());
            ps.setString(5, task.getType());      // ← Tambahan ini
            ps.setInt(6, task.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}