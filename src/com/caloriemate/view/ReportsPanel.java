package com.caloriemate.view;

import com.caloriemate.controller.FoodController;
import com.caloriemate.model.Food;
import com.caloriemate.util.PDFGenerator;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportsPanel extends JPanel {
    private final String username;
    private final FoodController foodController;
    private JTextArea weeklyReportArea;
    private JButton generatePdfButton;

    public ReportsPanel(String username) {
        System.out.println("[ReportsPanel] NEW INSTANCE CREATED with username: '" + username + "', length: " + (username != null ? username.length() : "null"));
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[ReportsPanel] FATAL ERROR: Username null atau kosong. Stack trace:");
            new Exception("Username null detected").printStackTrace();
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        this.username = username;
        this.foodController = new FoodController();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0x1C2526));
        initComponents();
        updateWeeklyReport();
        System.out.println("[ReportsPanel] ReportsPanel selesai diinisialisasi untuk username: " + username);
    }

    private void initComponents() {
        System.out.println("[ReportsPanel] Memulai initComponents untuk username: " + username);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(0x1C2526));
        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Weekly Report
        weeklyReportArea = new JTextArea(10, 40);
        weeklyReportArea.setFont(new Font("Roboto", Font.PLAIN, 14));
        weeklyReportArea.setBackground(new Color(0x4A4A4A));
        weeklyReportArea.setForeground(Color.WHITE);
        weeklyReportArea.setEditable(false);
        weeklyReportArea.setText("Laporan Mingguan: Memuat...");
        JScrollPane reportScroll = new JScrollPane(weeklyReportArea);
        add(reportScroll, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(0x1C2526));
        generatePdfButton = new JButton("Generate PDF Report");
        generatePdfButton.setFont(new Font("Roboto", Font.BOLD, 14));
        generatePdfButton.setBackground(new Color(0xD3D3D3));
        generatePdfButton.setForeground(new Color(0x1C2526));
        generatePdfButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        generatePdfButton.setFocusPainted(false);
        generatePdfButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(generatePdfButton);
        add(buttonPanel, BorderLayout.SOUTH);

        generatePdfButton.addActionListener(e -> {
            try {
                PDFGenerator.generateReport(username);
                JOptionPane.showMessageDialog(this, "Laporan PDF berhasil dibuat: report.pdf", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                System.err.println("[ReportsPanel] Error saat menghasilkan PDF: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Gagal menghasilkan PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void updateWeeklyReport() {
        System.out.println("[ReportsPanel] Mengambil laporan mingguan untuk username: " + username);
        try {
            List<Food> foods = foodController.getFoods(username, "");
            Map<String, Integer> weeklyCalories = foods.stream()
                .filter(f -> f.getDate() != null)
                .collect(Collectors.groupingBy(
                    f -> f.getDate().toString(),
                    Collectors.summingInt(Food::getCalories)
                ));

            StringBuilder report = new StringBuilder("Laporan Mingguan untuk " + username + ":\n\n");
            if (weeklyCalories.isEmpty()) {
                report.append("Tidak ada data makanan untuk minggu ini.\n");
            } else {
                weeklyCalories.forEach((date, calories) ->
                    report.append("Tanggal: ").append(date)
                          .append(", Total Kalori: ").append(calories).append(" kcal\n")
                );
                int totalWeekly = weeklyCalories.values().stream().mapToInt(Integer::intValue).sum();
                report.append("\nTotal Kalori Mingguan: ").append(totalWeekly).append(" kcal");
            }
            weeklyReportArea.setText(report.toString());
        } catch (SQLException e) {
            System.err.println("[ReportsPanel] SQL error di updateWeeklyReport: " + e.getMessage());
            e.printStackTrace();
            weeklyReportArea.setText("Laporan Mingguan: Error saat memuat data.");
            JOptionPane.showMessageDialog(this, "Gagal memuat laporan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("[ReportsPanel] Error umum di updateWeeklyReport: " + e.getMessage());
            e.printStackTrace();
            weeklyReportArea.setText("Laporan Mingguan: Error saat memuat data.");
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}