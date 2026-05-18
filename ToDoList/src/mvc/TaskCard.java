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

public class TaskCard extends JPanel {

    public TaskCard(Task task, Runnable refreshCallback) {

        setLayout(new BorderLayout(12, 0));
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        setMaximumSize(new Dimension(760, 160));
        
        Color bgColor = task.isCompleted() ? 
                new Color(245, 245, 245) : new Color(255, 255, 255);
        setBackground(bgColor);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        // Checkbox
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(task.isCompleted());
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 20));

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel title = new JLabel(task.getTitle());
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel description = new JLabel("<html><body style='width: 480px'>" + 
                task.getDescription() + "</body></html>");
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(new Color(100, 100, 100));

        JLabel deadline = createDeadlineLabel(task);
        JLabel type = new JLabel("• " + task.getType());
        type.setFont(new Font("SansSerif", Font.PLAIN, 13));
        type.setForeground(new Color(80, 80, 80));

        content.add(title);
        content.add(Box.createVerticalStrut(5));
        content.add(description);
        content.add(Box.createVerticalStrut(8));
        content.add(deadline);
        content.add(Box.createVerticalStrut(4));
        content.add(type);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        
        JButton btnEdit = new JButton("✏");
        JButton btnDelete = new JButton("🗑");
        
        styleButton(btnEdit, new Color(0, 123, 255));
        styleButton(btnDelete, new Color(220, 53, 69));

        buttons.add(btnEdit);
        buttons.add(btnDelete);

        // Assemble
        add(checkBox, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        add(buttons, BorderLayout.EAST);

        // Listeners
        checkBox.addActionListener(e -> {
            task.setCompleted(checkBox.isSelected());
            new TaskDAO().updateTask(task);
            refreshCallback.run();
        });

        btnEdit.addActionListener(e -> {
            new AddTaskDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                            new TaskController(), refreshCallback, task)
                    .setVisible(true);
        });

        btnDelete.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Hapus task ini?", "Konfirmasi", 
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new TaskDAO().deleteTask(task.getId());
                refreshCallback.run();
            }
        });
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setForeground(color);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setBackground(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JLabel createDeadlineLabel(Task task) {
        JLabel label = new JLabel();
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
            LocalDate today = LocalDate.now();
            LocalDate dl = LocalDate.parse(task.getDeadline(), fmt);

            if (dl.isBefore(today)) {
                label.setText("Overdue");
                label.setForeground(Color.RED);
            } else if (dl.isEqual(today)) {
                label.setText("Today");
                label.setForeground(new Color(255, 140, 0));
            } else {
                label.setText(task.getDeadline());
                label.setForeground(new Color(70, 70, 70));
            }
        } catch (Exception e) {
            label.setText(task.getDeadline());
        }
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }
}