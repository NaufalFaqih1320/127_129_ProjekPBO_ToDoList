/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mvc;

/**
 *
 * @author ASUS
 */

import controller.TaskController;
import model.Task;
import model.TugasTask;
import model.AcaraTask;

import java.awt.*;
import javax.swing.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AddTaskDialog extends JDialog {

    JTextField txtTitle;
    JTextArea txtDesc;
    JSpinner txtDeadline;
    JComboBox<String> cbType;
    
    private Task taskToEdit = null;   // null = mode Add, tidak null = mode Edit

    // Constructor untuk ADD Task
    public AddTaskDialog(JFrame parent, TaskController controller, Runnable refreshCallback) {
        this(parent, controller, refreshCallback, null);
    }

    // Constructor untuk EDIT Task
    public AddTaskDialog(JFrame parent, TaskController controller, Runnable refreshCallback, Task task) {
        super(parent, task == null ? "Add Task" : "Edit Task", true);

        this.taskToEdit = task;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtTitle = new JTextField();
        txtDesc = new JTextArea(5, 30);
        
        // Spinner Deadline
        SpinnerDateModel dateModel = new SpinnerDateModel();
        txtDeadline = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(txtDeadline, "dd MMM yyyy");
        txtDeadline.setEditor(dateEditor);

        cbType = new JComboBox<>(new String[]{"Tugas", "Acara"});

        JButton btnSave = new JButton(task == null ? "Save Task" : "Update Task");

        // Tambah komponen
        panel.add(new JLabel("Title"));
        panel.add(txtTitle);

        panel.add(new JLabel("Description"));
        panel.add(new JScrollPane(txtDesc));

        panel.add(new JLabel("Deadline"));
        panel.add(txtDeadline);

        panel.add(new JLabel("Category"));
        panel.add(cbType);

        add(panel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);

        // Pre-fill data jika mode Edit
        if (task != null) {
            txtTitle.setText(task.getTitle());
            txtDesc.setText(task.getDescription());
            cbType.setSelectedItem(task.getType());
            
            // Parse deadline untuk spinner
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                Date deadlineDate = sdf.parse(task.getDeadline());
                txtDeadline.setValue(deadlineDate);
            } catch (Exception e) {
                txtDeadline.setValue(new Date());
            }
        } else {
            txtDeadline.setValue(new Date()); // default hari ini
        }

        // Action Save / Update
        btnSave.addActionListener(e -> {
            String title = txtTitle.getText().trim();
            String desc = txtDesc.getText().trim();
            Date selectedDate = (Date) txtDeadline.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            String deadline = sdf.format(selectedDate);

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Task taskToSave;

            if (cbType.getSelectedItem().equals("Acara")) {
                taskToSave = new AcaraTask(0, title, desc, deadline, false);
            } else {
                taskToSave = new TugasTask(0, title, desc, deadline, false);
            }

            if (taskToEdit != null) {
                // Mode Edit
                taskToSave.setId(taskToEdit.getId());
                taskToSave.setCompleted(taskToEdit.isCompleted());
                controller.updateTask(taskToSave);
            } else {
                // Mode Add
                controller.addTask(taskToSave);
            }

            refreshCallback.run();
            dispose();
        });
    }
}