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

// Class AddTaskDialog digunakan untuk menampilkan form tambah dan edit task.
// User dapat menginput data task melalui dialog ini.
public class AddTaskDialog extends JDialog {

    JTextField txtTitle; // Field input judul task

    JTextArea txtDesc; // Field input deskripsi task

    JSpinner txtDeadline; // Field input deadline task
 
    JComboBox<String> cbType; // untuk memilih kategori task
     
    private Task taskToEdit = null; // Menyimpan task yang akan diedit : null = mode Add, tidak null = mode Edit

    public AddTaskDialog(JFrame parent, TaskController controller, Runnable refreshCallback) {
        this(parent, controller, refreshCallback, null);
    } // Constructor untuk mode Add Task

    // Constructor untuk mode Edit Task
    public AddTaskDialog(JFrame parent, TaskController controller, Runnable refreshCallback, Task task) {

        super(parent, task == null ? "Add Task" : "Edit Task", true); // Menentukan title dialog berdasarkan mode

        this.taskToEdit = task;

        setSize(400, 400); // Mengatur ukuran dan layout dialog
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Panel utama form
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input judul
        txtTitle = new JTextField();

        // Input deskripsi
        txtDesc = new JTextArea(5, 30);
        
        // Spinner untuk memilih deadline
        SpinnerDateModel dateModel = new SpinnerDateModel();
        txtDeadline = new JSpinner(dateModel);

        // Format tampilan tanggal
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(txtDeadline, "dd MMM yyyy");
        txtDeadline.setEditor(dateEditor);

        // Pilihan kategori task
        cbType = new JComboBox<>(new String[]{"Tugas", "Acara"});

        // Tombol save/update
        JButton btnSave = new JButton(task == null ? "Save Task" : "Update Task");

        // Menambahkan komponen ke panel
        panel.add(new JLabel("Title"));
        panel.add(txtTitle);

        panel.add(new JLabel("Description"));
        panel.add(new JScrollPane(txtDesc));

        panel.add(new JLabel("Deadline"));
        panel.add(txtDeadline);

        panel.add(new JLabel("Category"));
        panel.add(cbType);

        // Menambahkan panel dan tombol ke dialog
        add(panel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);

        // Mengisi field jika mode Edit
        if (task != null) {

            // Mengisi title dan description
            txtTitle.setText(task.getTitle());
            txtDesc.setText(task.getDescription());

            // Mengisi kategori
            cbType.setSelectedItem(task.getType());
            
            // Mengubah deadline String menjadi Date
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                Date deadlineDate = sdf.parse(task.getDeadline());
                txtDeadline.setValue(deadlineDate);

            } catch (Exception e) {

                // Jika gagal parse, gunakan tanggal hari ini
                txtDeadline.setValue(new Date());
            }

        } else {

            // Default deadline = hari ini
            txtDeadline.setValue(new Date());
        }

        // Action ketika tombol save/update ditekan
        btnSave.addActionListener(e -> {

            // Mengambil input dari user
            String title = txtTitle.getText().trim();
            String desc = txtDesc.getText().trim();

            // Mengambil tanggal deadline
            Date selectedDate = (Date) txtDeadline.getValue();

            // Format tanggal
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            String deadline = sdf.format(selectedDate);

            // Validasi title tidak boleh kosong
            if (title.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Title cannot be empty!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // Membuat object task berdasarkan kategori
            Task taskToSave;

            if (cbType.getSelectedItem().equals("Acara")) {

                taskToSave = new AcaraTask(0, title, desc, deadline, false);

            } else {

                taskToSave = new TugasTask(0, title, desc, deadline, false);
            }

            // Jika mode Edit
            if (taskToEdit != null) {

                // Mengatur id dan status completed
                taskToSave.setId(taskToEdit.getId());
                taskToSave.setCompleted(taskToEdit.isCompleted());

                // Update task
                controller.updateTask(taskToSave);

            } else {

                // Menambahkan task baru
                controller.addTask(taskToSave);
            }

            // Refresh tampilan task
            refreshCallback.run();

            // Menutup dialog
            dispose();
        });
    }
}
