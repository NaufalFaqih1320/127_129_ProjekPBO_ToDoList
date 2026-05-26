/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvc;

/**
 *
 * @author ASUS
 */
/*
 * TaskCard.java - Updated with Edit Feature
 */

import controller.TaskController;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import model.Task;
import model.TaskDAO;

// Class TaskCard digunakan untuk menampilkan informasi task seperti judul, deskripsi, deadline, status, edit, dan delete.
public class TaskCard extends JPanel {

    // Constructor TaskCard
    public TaskCard(Task task, Runnable refreshCallback) {

        // Mengatur layout card
        setLayout(new BorderLayout(12, 0));

        // Margin dalam card
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // Ukuran maksimum card
        setMaximumSize(new Dimension(760, 160));
        
        // Mengatur warna background berdasarkan status task
        Color bgColor = task.isCompleted()
                ? new Color(245, 245, 245)
                : new Color(255, 255, 255);

        setBackground(bgColor);

        // Border card
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220),
                                1,
                                true
                        ),
                        BorderFactory.createEmptyBorder(16, 20, 16, 20)
                )
        );

        // ==================== CHECKBOX ====================

        // Checkbox status task selesai/belum
        JCheckBox checkBox = new JCheckBox();

        checkBox.setSelected(task.isCompleted());
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 20));

        // ==================== CONTENT ====================

        // Panel isi task
        JPanel content = new JPanel();

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Judul task
        JLabel title = new JLabel(task.getTitle());
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Deskripsi task
        JLabel description = new JLabel(
                "<html><body style='width: 480px'>"
                + task.getDescription()
                + "</body></html>"
        );

        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(new Color(100, 100, 100));

        // Label deadline
        JLabel deadline = createDeadlineLabel(task);

        // Label kategori task
        JLabel type = new JLabel("• " + task.getType());

        type.setFont(new Font("SansSerif", Font.PLAIN, 13));
        type.setForeground(new Color(80, 80, 80));

        // Menambahkan komponen ke content panel
        content.add(title);
        content.add(Box.createVerticalStrut(5));
        content.add(description);
        content.add(Box.createVerticalStrut(8));
        content.add(deadline);
        content.add(Box.createVerticalStrut(4));
        content.add(type);

        // ==================== BUTTONS ====================

        // Panel tombol
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttons.setOpaque(false);
        
        // Tombol edit
        JButton btnEdit = new JButton("✏");

        // Tombol delete
        JButton btnDelete = new JButton("🗑");
        
        // Styling tombol
        styleButton(btnEdit, new Color(0, 123, 255));
        styleButton(btnDelete, new Color(220, 53, 69));

        // Menambahkan tombol ke panel
        buttons.add(btnEdit);
        buttons.add(btnDelete);

        // ==================== ASSEMBLE ====================

        // Menambahkan semua komponen ke card
        add(checkBox, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        add(buttons, BorderLayout.EAST);

        // ==================== EVENT LISTENER ====================

        // Event checkbox
        checkBox.addActionListener(e -> {

            // Mengubah status completed
            task.setCompleted(checkBox.isSelected());

            // Update database
            new TaskDAO().updateTask(task);

            // Refresh tampilan
            refreshCallback.run();
        });

        // Event tombol edit
        btnEdit.addActionListener(e -> {

            // Membuka dialog edit task
            new AddTaskDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    new TaskController(),
                    refreshCallback,
                    task
            ).setVisible(true);
        });

        // Event tombol delete
        btnDelete.addActionListener(e -> {

            // Konfirmasi hapus task
            if (JOptionPane.showConfirmDialog(
                    this,
                    "Hapus task ini?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION) {

                // Menghapus task dari database
                new TaskDAO().deleteTask(task.getId());

                // Refresh tampilan
                refreshCallback.run();
            }
        });
    }

    // Method untuk styling tombol
    private void styleButton(JButton btn, Color color) {

        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setForeground(color);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setBackground(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Method untuk membuat label deadline
    private JLabel createDeadlineLabel(Task task) {

        JLabel label = new JLabel();

        try {

            // Format tanggal
            DateTimeFormatter fmt =
                    DateTimeFormatter.ofPattern("dd MMM yyyy");

            // Tanggal hari ini
            LocalDate today = LocalDate.now();

            // Deadline task
            LocalDate dl =
                    LocalDate.parse(task.getDeadline(), fmt);

            // Jika deadline lewat
            if (dl.isBefore(today)) {

                label.setText("Overdue");
                label.setForeground(Color.RED);

            // Jika deadline hari ini
            } else if (dl.isEqual(today)) {

                label.setText("Today");
                label.setForeground(new Color(255, 140, 0));

            // Jika deadline masih normal
            } else {

                label.setText(task.getDeadline());
                label.setForeground(new Color(70, 70, 70));
            }

        } catch (Exception e) {

            // Jika format tanggal error
            label.setText(task.getDeadline());
        }

        // Font label deadline
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        return label;
    }
}
