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
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

// Class CalendarPanel digunakan untuk menampilkan kalender beserta task berdasarkan tanggal deadline.
public class CalendarPanel extends JPanel {

    // Controller untuk mengambil data task
    private final TaskController controller;

    // Label untuk menampilkan nama bulan
    private JLabel monthLabel;

    // Panel grid kalender
    private JPanel gridPanel;

    // Menyimpan bulan yang sedang ditampilkan
    private YearMonth currentMonth;

    // Menyimpan task berdasarkan tanggal
    private Map<LocalDate, List<Task>> tasksByDate;

    // Constructor CalendarPanel
    public CalendarPanel(TaskController controller) {

        // Inisialisasi controller
        this.controller = controller;

        // Default bulan saat ini
        this.currentMonth = YearMonth.now();

        // Mengatur layout panel
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Memanggil komponen dan refresh kalender
        initComponents();
        refreshCalendar();
    }

    // Method untuk membuat komponen kalender
    private void initComponents() {

        // Header kalender
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 250, 250));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Tombol navigasi bulan
        JButton btnPrev = new JButton("◀");
        JButton btnNext = new JButton("▶");

        // Label nama bulan
        monthLabel = new JLabel();
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        // Panel navigasi
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);

        navPanel.add(btnPrev);
        navPanel.add(monthLabel);
        navPanel.add(btnNext);

        header.add(navPanel, BorderLayout.CENTER);

        // Grid kalender
        gridPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Menampilkan nama hari
        String[] days = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};

        for (String day : days) {

            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dayLabel.setForeground(new Color(100, 100, 100));

            gridPanel.add(dayLabel);
        }

        // Menambahkan header dan grid ke panel
        add(header, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        // Action tombol bulan sebelumnya
        btnPrev.addActionListener(e -> {

            currentMonth = currentMonth.minusMonths(1);
            refreshCalendar();
        });

        // Action tombol bulan berikutnya
        btnNext.addActionListener(e -> {

            currentMonth = currentMonth.plusMonths(1);
            refreshCalendar();
        });
    }

    // Method untuk memperbarui tampilan kalender
    public void refreshCalendar() {

        // Mengelompokkan task berdasarkan tanggal
        tasksByDate = groupTasksByDate();

        // Menghapus isi grid sebelumnya
        gridPanel.removeAll();

        // Menambahkan nama hari kembali
        String[] days = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};

        for (String day : days) {

            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
            lbl.setForeground(new Color(100, 100, 100));

            gridPanel.add(lbl);
        }

        // Menampilkan nama bulan dan tahun
        monthLabel.setText(
                currentMonth.getMonth().getDisplayName(
                        java.time.format.TextStyle.FULL,
                        Locale.forLanguageTag("id"))
                + " " + currentMonth.getYear()
        );

        // Mengambil tanggal pertama bulan
        LocalDate firstOfMonth = currentMonth.atDay(1);

        // Menentukan posisi awal hari
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7;

        // Menambahkan kotak kosong sebelum tanggal 1
        for (int i = 0; i < startDay; i++) {

            gridPanel.add(new JLabel(""));
        }

        // Mengambil jumlah hari dalam bulan
        int daysInMonth = currentMonth.lengthOfMonth();

        // Mengambil tanggal hari ini
        LocalDate today = LocalDate.now();

        // Menampilkan semua tanggal
        for (int day = 1; day <= daysInMonth; day++) {

            LocalDate date = currentMonth.atDay(day);

            // Membuat panel tanggal
            JPanel dayPanel = createDayPanel(date, today);

            gridPanel.add(dayPanel);
        }

        // Refresh tampilan panel
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // Method untuk membuat panel setiap tanggal
    private JPanel createDayPanel(LocalDate date, LocalDate today) {

        JPanel panel = new JPanel(new BorderLayout());

        // Mengatur ukuran panel tanggal
        panel.setPreferredSize(new Dimension(80, 80));

        // Border panel
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Mengecek apakah tanggal hari ini
        boolean isToday = date.equals(today);

        // Mengambil task pada tanggal tertentu
        List<Task> tasks = tasksByDate.getOrDefault(date, new ArrayList<>());

        // Label tanggal
        JLabel dateLabel = new JLabel(
                String.valueOf(date.getDayOfMonth()),
                SwingConstants.CENTER
        );

        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Jika tanggal hari ini
        if (isToday) {

            panel.setBackground(new Color(230, 243, 255));
            dateLabel.setForeground(new Color(0, 102, 204));

        } else if (!date.getMonth().equals(currentMonth.getMonth())) {

            // Jika tanggal di luar bulan aktif
            dateLabel.setForeground(Color.GRAY);
        }

        panel.add(dateLabel, BorderLayout.NORTH);

        // Jika terdapat task pada tanggal tersebut
        if (!tasks.isEmpty()) {

            // Menampilkan jumlah task
            JLabel count = new JLabel(tasks.size() + " task", SwingConstants.CENTER);

            count.setFont(new Font("SansSerif", Font.PLAIN, 11));
            count.setForeground(new Color(0, 123, 255));

            panel.add(count, BorderLayout.CENTER);

            // Mengubah background panel
            panel.setBackground(new Color(240, 248, 255));

            // Mengubah cursor menjadi tangan
            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Event klik panel
            panel.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {

                    // Menampilkan detail task
                    showTasksForDate(date, tasks);
                }
            });
        }

        return panel;
    }

    // Method untuk menampilkan task berdasarkan tanggal
    private void showTasksForDate(LocalDate date, List<Task> tasks) {

        // Jika task kosong
        if (tasks.isEmpty()) return;

        StringBuilder sb = new StringBuilder();

        // Header tanggal
        sb.append("📅 ")
          .append(date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
          .append("\n\n");

        // Menampilkan daftar task
        for (Task task : tasks) {

            sb.append(task.isCompleted() ? "✅ " : "⬜ ")
              .append(task.getType())
              .append(" - ")
              .append(task.getTitle())
              .append("\n");
        }

        // Menampilkan popup task
        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "Task pada " + date,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Method untuk mengelompokkan task berdasarkan tanggal
    private Map<LocalDate, List<Task>> groupTasksByDate() {

        // Map penyimpanan task
        Map<LocalDate, List<Task>> map = new HashMap<>();

        // Mengambil semua task
        List<Task> allTasks = controller.getTasks();

        // Format tanggal
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        // Loop semua task
        for (Task task : allTasks) {

            try {

                // Mengubah deadline menjadi LocalDate
                LocalDate taskDate = LocalDate.parse(task.getDeadline(), formatter);

                // Menambahkan task ke map
                map.computeIfAbsent(taskDate, k -> new ArrayList<>()).add(task);

            } catch (Exception e) {

                // Skip jika format tanggal salah
            }
        }

        return map;
    }
}
