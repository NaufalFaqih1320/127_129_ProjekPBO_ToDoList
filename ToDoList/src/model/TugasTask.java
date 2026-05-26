/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */

// Class TugasTask merupakan subclass dari Task
// yang digunakan untuk merepresentasikan task jenis tugas
public class TugasTask extends Task {
    
    // Constructor untuk mengisi data task tugas
    public TugasTask(int id, String title, String description, String deadline, boolean completed) {
        // Memanggil constructor parent class Task
        // untuk menginisialisasi attribute task
        super(id, title, description, deadline, completed);
    }

    // Override method displayInfo dari class Task
    @Override
    public String displayInfo() {
        // Mengembalikan informasi task dalam bentuk String
        return "Tugas Task : " + title;
    }

    // Override method getType dari class Task
    @Override
    public String getType() {
        // Mengembalikan tipe task
        return "Tugas";
    }
}
