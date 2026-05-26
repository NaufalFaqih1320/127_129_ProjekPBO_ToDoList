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

// Class MainFrame merupakan tampilan utama aplikasi.
// Class ini mengatur tampilan header, menu,
// daftar task, dan kalender.
public class MainFrame extends JFrame {

    private static final Color PRIMARY_COLOR =
        new Color(70, 130, 180);
    
    // Controller untuk mengelola data task
    private final TaskController controller;

    // Panel utama dengan CardLayout
    private final JPanel cardPanel;

    // Container untuk menampilkan daftar task
    private final JPanel taskContainer;

    // Panel kalender
    private final CalendarPanel calendarPanel;

    // Constructor MainFrame
    public MainFrame() {

        // Membuat object controller
        controller = new TaskController();

        // Mengatur properti frame
        setTitle("To-Do Reminder App");
        setSize(900, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==================== HEADER ====================

        // Panel header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 250, 250));
        header.setBorder(BorderFactory.createEmptyBorder(25, 30, 15, 30));

        // Judul aplikasi
        JLabel title = new JLabel("To-Do Reminder");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(new Color(40, 40, 40));

        // Tombol tambah task
        JButton btnAdd = new JButton("  + Tambah Task  ");
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnAdd.setBackground(PRIMARY_COLOR);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Menambahkan komponen ke header
        header.add(title, BorderLayout.WEST);
        header.add(btnAdd, BorderLayout.EAST);

        // ==================== MENU NAVIGATION ====================

        // Panel menu navigasi
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));

        // Tombol menu
        JButton btnTasks = createMenuButton("Tasks", true);
        JButton btnCalendar = createMenuButton("Kalender", false);

        // Menambahkan tombol ke panel menu
        menuPanel.add(btnTasks);
        menuPanel.add(btnCalendar);

        // ==================== NORTH PANEL ====================

        // Panel gabungan header dan menu
        JPanel northPanel = new JPanel(new BorderLayout());

        northPanel.add(header, BorderLayout.NORTH);
        northPanel.add(menuPanel, BorderLayout.CENTER);

        // ==================== CARD PANEL ====================

        // Panel utama dengan CardLayout
        cardPanel = new JPanel(new CardLayout());
        
        // Panel container task
        taskContainer = new JPanel();

        // Mengatur layout task menjadi vertikal
        taskContainer.setLayout(new BoxLayout(taskContainer, BoxLayout.Y_AXIS));
        taskContainer.setBackground(Color.WHITE);

        // Scroll pane untuk daftar task
        JScrollPane taskScroll = new JScrollPane(taskContainer);
        taskScroll.setBorder(null);

        // Membuat panel kalender
        calendarPanel = new CalendarPanel(controller);

        // Menambahkan halaman ke card panel
        cardPanel.add(taskScroll, "Tasks");
        cardPanel.add(calendarPanel, "Kalender");

        // Menambahkan panel ke frame
        add(northPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        // Memuat task pertama kali
        loadTasks();

        // ==================== ACTION LISTENER ====================

        // Event tombol tambah task
        btnAdd.addActionListener(e -> {

            // Membuka dialog tambah task
            AddTaskDialog dialog = new AddTaskDialog(
                    this,
                    controller,
                    this::loadTasks
            );

            dialog.setVisible(true);
        });

        // Event pindah ke halaman task
        btnTasks.addActionListener(e -> switchToTasks(btnTasks, btnCalendar));

        // Event pindah ke halaman kalender
        btnCalendar.addActionListener(e -> switchToCalendar(btnTasks, btnCalendar));
    }

    // Method untuk membuat tombol menu
    private JButton createMenuButton(String text, boolean isActive) {

        JButton btn = new JButton(text);

        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Jika menu aktif
        if (isActive) {

            btn.setBackground(PRIMARY_COLOR);
            btn.setForeground(Color.WHITE);

        } else {

            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(Color.BLACK);
        }

        return btn;
    }

    // Method untuk menampilkan halaman task
    private void switchToTasks(JButton btnTasks, JButton btnCalendar) {

        // Menampilkan card Tasks
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Tasks");

        // Mengubah warna tombol aktif
        btnTasks.setBackground(PRIMARY_COLOR);
        btnTasks.setForeground(Color.WHITE);

        // Mengubah warna tombol nonaktif
        btnCalendar.setBackground(new Color(240, 240, 240));
        btnCalendar.setForeground(Color.BLACK);
    }

    // Method untuk menampilkan halaman kalender
    private void switchToCalendar(JButton btnTasks, JButton btnCalendar) {

        // Menampilkan card Kalender
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Kalender");

        // Mengubah warna tombol aktif
        btnCalendar.setBackground(PRIMARY_COLOR);
        btnCalendar.setForeground(Color.WHITE);

        // Mengubah warna tombol nonaktif
        btnTasks.setBackground(new Color(240, 240, 240));
        btnTasks.setForeground(Color.BLACK);

        // Refresh kalender
        calendarPanel.refreshCalendar();
    }

    // Method untuk memuat semua task
    public void loadTasks() {

        // Menghapus isi task sebelumnya
        taskContainer.removeAll();

        // Mengambil semua task
        List<Task> tasks = controller.getTasks();

        // Jika tidak ada task
        if (tasks.isEmpty()) {

            JLabel empty = new JLabel("Belum ada task");

            empty.setFont(new Font("SansSerif", Font.PLAIN, 26));
            empty.setForeground(new Color(180, 180, 180));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);

            taskContainer.add(Box.createVerticalGlue());
            taskContainer.add(empty);
            taskContainer.add(Box.createVerticalGlue());

        } else {

            // ==================== TASK BELUM SELESAI ====================

            JLabel myTasksLabel = new JLabel("My Tasks");

            myTasksLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            myTasksLabel.setBorder(
                    BorderFactory.createEmptyBorder(15, 25, 8, 0)
            );

            taskContainer.add(myTasksLabel);
            taskContainer.add(Box.createVerticalStrut(8));

            // Menampilkan task yang belum selesai
            for (Task task : tasks) {

                if (!task.isCompleted()) {

                    taskContainer.add(new TaskCard(task, this::loadTasks));
                    taskContainer.add(Box.createVerticalStrut(12));
                }
            }

            taskContainer.add(Box.createVerticalStrut(30));

            // ==================== TASK SELESAI ====================

            JLabel completedLabel = new JLabel("Completed");

            completedLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            completedLabel.setBorder(
                    BorderFactory.createEmptyBorder(10, 25, 8, 0)
            );

            taskContainer.add(completedLabel);
            taskContainer.add(Box.createVerticalStrut(8));

            // Menampilkan task yang selesai
            for (Task task : tasks) {

                if (task.isCompleted()) {

                    taskContainer.add(new TaskCard(task, this::loadTasks));
                    taskContainer.add(Box.createVerticalStrut(12));
                }
            }
        }

        // Refresh tampilan task
        taskContainer.revalidate();
        taskContainer.repaint();
    }
}
