/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mvc;

import controller.TaskController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import model.Task;

public class MainFrame extends JFrame {

    private TaskController controller;
    private JPanel cardPanel;
    private JPanel taskContainer;
    private CalendarPanel calendarPanel;

    public MainFrame() {
        controller = new TaskController();

        setTitle("To-Do Reminder App");
        setSize(900, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==================== HEADER ====================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 250, 250));
        header.setBorder(BorderFactory.createEmptyBorder(25, 30, 15, 30));

        JLabel title = new JLabel("To-Do Reminder");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(new Color(40, 40, 40));

        JButton btnAdd = new JButton("  + Tambah Task  ");
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnAdd.setBackground(new Color(70, 130, 180));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        header.add(title, BorderLayout.WEST);
        header.add(btnAdd, BorderLayout.EAST);

        // ==================== MENU NAVIGATION ====================
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));

        JButton btnTasks = createMenuButton("Tasks", true);
        JButton btnCalendar = createMenuButton("Kalender", false);

        menuPanel.add(btnTasks);
        menuPanel.add(btnCalendar);

        // ==================== NORTH PANEL (Header + Menu) ====================
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(header, BorderLayout.NORTH);
        northPanel.add(menuPanel, BorderLayout.CENTER);

        // ==================== CARD PANEL ====================
        cardPanel = new JPanel(new CardLayout());
        
        taskContainer = new JPanel();
        taskContainer.setLayout(new BoxLayout(taskContainer, BoxLayout.Y_AXIS));
        taskContainer.setBackground(Color.WHITE);

        JScrollPane taskScroll = new JScrollPane(taskContainer);
        taskScroll.setBorder(null);

        calendarPanel = new CalendarPanel(controller);

        cardPanel.add(taskScroll, "Tasks");
        cardPanel.add(calendarPanel, "Kalender");

        // Tambahkan ke Frame
        add(northPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        loadTasks();

        // Action Listeners
        btnAdd.addActionListener(e -> {
            AddTaskDialog dialog = new AddTaskDialog(this, controller, this::loadTasks);
            dialog.setVisible(true);
        });

        btnTasks.addActionListener(e -> switchToTasks(btnTasks, btnCalendar));
        btnCalendar.addActionListener(e -> switchToCalendar(btnTasks, btnCalendar));
    }

    private JButton createMenuButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isActive) {
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(Color.BLACK);
        }
        return btn;
    }

    private void switchToTasks(JButton btnTasks, JButton btnCalendar) {
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Tasks");
        btnTasks.setBackground(new Color(70, 130, 180));
        btnTasks.setForeground(Color.WHITE);
        btnCalendar.setBackground(new Color(240, 240, 240));
        btnCalendar.setForeground(Color.BLACK);
    }

    private void switchToCalendar(JButton btnTasks, JButton btnCalendar) {
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Kalender");
        btnCalendar.setBackground(new Color(70, 130, 180));
        btnCalendar.setForeground(Color.WHITE);
        btnTasks.setBackground(new Color(240, 240, 240));
        btnTasks.setForeground(Color.BLACK);
        calendarPanel.refreshCalendar();
    }

    public void loadTasks() {
        taskContainer.removeAll();
        List<Task> tasks = controller.getTasks();

        if (tasks.isEmpty()) {
            JLabel empty = new JLabel("Belum ada task");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 26));
            empty.setForeground(new Color(180, 180, 180));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskContainer.add(Box.createVerticalGlue());
            taskContainer.add(empty);
            taskContainer.add(Box.createVerticalGlue());
        } else {
            // My Tasks
            JLabel myTasksLabel = new JLabel("My Tasks");
            myTasksLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            myTasksLabel.setBorder(BorderFactory.createEmptyBorder(15, 25, 8, 0));
            taskContainer.add(myTasksLabel);
            taskContainer.add(Box.createVerticalStrut(8));

            for (Task task : tasks) {
                if (!task.isCompleted()) {
                    taskContainer.add(new TaskCard(task, this::loadTasks));
                    taskContainer.add(Box.createVerticalStrut(12));
                }
            }

            taskContainer.add(Box.createVerticalStrut(30));

            // Completed
            JLabel completedLabel = new JLabel("Completed");
            completedLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            completedLabel.setBorder(BorderFactory.createEmptyBorder(10, 25, 8, 0));
            taskContainer.add(completedLabel);
            taskContainer.add(Box.createVerticalStrut(8));

            for (Task task : tasks) {
                if (task.isCompleted()) {
                    taskContainer.add(new TaskCard(task, this::loadTasks));
                    taskContainer.add(Box.createVerticalStrut(12));
                }
            }
        }

        taskContainer.revalidate();
        taskContainer.repaint();
    }
}