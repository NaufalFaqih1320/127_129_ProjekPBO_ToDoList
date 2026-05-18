/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */

public class TugasTask extends Task {

    public TugasTask(int id, String title, String description, String deadline, boolean completed) {
        super(id, title, description, deadline, completed);
    }

    @Override
    public String displayInfo() {
        return "Tugas Task : " + title;
    }

    @Override
    public String getType() {
        return "Tugas";
    }
}