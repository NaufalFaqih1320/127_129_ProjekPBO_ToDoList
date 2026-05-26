/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */

// Class AcaraTask merupakan turunan dari class Task
// yang digunakan untuk task jenis acara
public class AcaraTask extends Task {
    // Constructor untuk mengisi data task acara
    public AcaraTask(int id, String title, String description, String deadline, boolean completed) {
        
        // Memanggil constructor dari parent class Task
        super(id, title, description, deadline, completed);
    }
    
    // Override method displayInfo dari class Task
    @Override
    public String displayInfo() {
        // Mengembalikan informasi task acara
        return "Acara Task : " + title;
    }
    
    // Override method getType dari class Task
    @Override
    public String getType() {
        // Mengembalikan tipe task
        return "Acara";
    }
}
