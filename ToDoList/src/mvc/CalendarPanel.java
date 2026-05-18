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

public class CalendarPanel extends JPanel {

    private TaskController controller;
    private JLabel monthLabel;
    private JPanel gridPanel;
    private YearMonth currentMonth;
    private Map<LocalDate, List<Task>> tasksByDate;

    public CalendarPanel(TaskController controller) {
        this.controller = controller;
        this.currentMonth = YearMonth.now();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
        refreshCalendar();
    }

    private void initComponents() {
        // Header Kalender
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 250, 250));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton btnPrev = new JButton("◀");
        JButton btnNext = new JButton("▶");
        monthLabel = new JLabel();
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);
        navPanel.add(btnPrev);
        navPanel.add(monthLabel);
        navPanel.add(btnNext);

        header.add(navPanel, BorderLayout.CENTER);

        // Grid Kalender
        gridPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Nama Hari
        String[] days = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dayLabel.setForeground(new Color(100, 100, 100));
            gridPanel.add(dayLabel);
        }

        add(header, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        // Action Listener Navigasi
        btnPrev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            refreshCalendar();
        });

        btnNext.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            refreshCalendar();
        });
    }

    public void refreshCalendar() {
        tasksByDate = groupTasksByDate();
        gridPanel.removeAll(); // hapus semua kecuali header hari

        // Tambah nama hari lagi
        String[] days = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
        for (String day : days) {
            JLabel lbl = new JLabel(day, SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
            lbl.setForeground(new Color(100, 100, 100));
            gridPanel.add(lbl);
        }

        monthLabel.setText(currentMonth.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.forLanguageTag("id")) 
                + " " + currentMonth.getYear());

        LocalDate firstOfMonth = currentMonth.atDay(1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() % 7; // 0 = Minggu

        // Isi tanggal kosong sebelum tanggal 1
        for (int i = 0; i < startDay; i++) {
            gridPanel.add(new JLabel(""));
        }

        // Isi tanggal bulan ini
        int daysInMonth = currentMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            JPanel dayPanel = createDayPanel(date, today);
            gridPanel.add(dayPanel);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createDayPanel(LocalDate date, LocalDate today) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(80, 80));
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        boolean isToday = date.equals(today);
        List<Task> tasks = tasksByDate.getOrDefault(date, new ArrayList<>());

        JLabel dateLabel = new JLabel(String.valueOf(date.getDayOfMonth()), SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        if (isToday) {
            panel.setBackground(new Color(230, 243, 255));
            dateLabel.setForeground(new Color(0, 102, 204));
        } else if (!date.getMonth().equals(currentMonth.getMonth())) {
            dateLabel.setForeground(Color.GRAY);
        }

        panel.add(dateLabel, BorderLayout.NORTH);

        // Jika ada task
        if (!tasks.isEmpty()) {
            JLabel count = new JLabel(tasks.size() + " task", SwingConstants.CENTER);
            count.setFont(new Font("SansSerif", Font.PLAIN, 11));
            count.setForeground(new Color(0, 123, 255));
            panel.add(count, BorderLayout.CENTER);

            panel.setBackground(new Color(240, 248, 255));
            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Klik untuk melihat detail
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showTasksForDate(date, tasks);
                }
            });
        }

        return panel;
    }

    private void showTasksForDate(LocalDate date, List<Task> tasks) {
        if (tasks.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        sb.append("📅 ").append(date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))).append("\n\n");

        for (Task task : tasks) {
            sb.append(task.isCompleted() ? "✅ " : "⬜ ")
              .append(task.getType())
              .append(" - ")
              .append(task.getTitle())
              .append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Task pada " + date, JOptionPane.INFORMATION_MESSAGE);
    }

    private Map<LocalDate, List<Task>> groupTasksByDate() {
        Map<LocalDate, List<Task>> map = new HashMap<>();
        List<Task> allTasks = controller.getTasks();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        for (Task task : allTasks) {
            try {
                LocalDate taskDate = LocalDate.parse(task.getDeadline(), formatter);
                map.computeIfAbsent(taskDate, k -> new ArrayList<>()).add(task);
            } catch (Exception e) {
                // Skip jika format tanggal salah
            }
        }
        return map;
    }
}