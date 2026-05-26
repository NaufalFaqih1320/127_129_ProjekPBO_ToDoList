/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */

// Abstract class Task digunakan sebagai parent
// untuk seluruh jenis task dalam aplikasi
public abstract class Task {

    // Attribute task
    protected int id;
    protected String title;
    protected String description;
    protected String deadline;
    protected boolean completed;
    
    // Constructor untuk mengisi data task
    public Task(int id, String title, String description, String deadline, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
    }
    
    // Getter ID
    public int getId() {
        return id;
    }

    // Setter ID
    public void setId(int id) {
        this.id = id;
    }
    
    // Getter title
    public String getTitle() {
        return title;
    }
    
    // Setter title
    public void setTitle(String title) {
        this.title = title;
    }
    
    // Getter description
    public String getDescription() {
        return description;
    }
    
    // Setter description
    public void setDescription(String description) {
        this.description = description;
    }
    
    // Getter deadline
    public String getDeadline() {
        return deadline;
    }
    
    // Setter deadline
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    
    // Getter status completed
    public boolean isCompleted() {
        return completed;
    }
    
    // Setter status completed
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    // Abstract method untuk menampilkan informasi task
    public abstract String displayInfo();
    
    // Abstract method untuk mendapatkan tipe task
    public abstract String getType();
}
