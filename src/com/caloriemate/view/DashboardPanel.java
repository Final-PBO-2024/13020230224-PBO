package com.caloriemate.view;

import com.caloriemate.controller.FoodController;
import com.caloriemate.controller.JournalController;
import com.caloriemate.controller.WeightController;
import com.caloriemate.util.ChartGenerator;
import com.caloriemate.model.Food;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardPanel extends JPanel {
    private final String username;
    private final FoodController foodController;
    private final JournalController journalController;
    private final WeightController weightController;
    private JLabel totalCaloriesLabel;
    private JLabel latestMoodLabel;
    private JLabel latestWeightLabel;
    private JTextArea reportArea;
    private ChartPanel chartPanel;

    public DashboardPanel(String username) {
        System.out.println("[DashboardPanel] NEW INSTANCE CREATED with username: '" + username + "', length: " + (username != null ? username.length() : "null") + ", Thread: " + Thread.currentThread().getName());
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[DashboardPanel] FATAL ERROR: Username null atau kosong. Stack trace:");
            new Exception("Username null detected").printStackTrace();
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        this.username = username;
        this.foodController = new FoodController();
        this.journalController = new JournalController();
        this.weightController = new WeightController();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0x1C2526));
        initComponents();
        updateDashboard();
        startAutoUpdate();
        System.out.println("[DashboardPanel] DashboardPanel selesai diinisialisasi untuk username: " + username);
    }

    private void initComponents() {
        System.out.println("[DashboardPanel] Memulai initComponents untuk username: " + username);

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(0x1C2526));
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(0x1C2526));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Total Calories
        totalCaloriesLabel = new JLabel("Total Kalori: Memuat...");
        totalCaloriesLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
        totalCaloriesLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(totalCaloriesLabel, gbc);

        // Latest Mood
        latestMoodLabel = new JLabel("Mood Terbaru: Memuat...");
        latestMoodLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
        latestMoodLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        contentPanel.add(latestMoodLabel, gbc);

        // Latest Weight
        latestWeightLabel = new JLabel("Berat Terbaru: Memuat...");
        latestWeightLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
        latestWeightLabel.setForeground(Color.WHITE);
        gbc.gridy = 2;
        contentPanel.add(latestWeightLabel, gbc);

        // Daily Report
        reportArea = new JTextArea(5, 30);
        reportArea.setFont(new Font("Roboto", Font.PLAIN, 14));
        reportArea.setBackground(new Color(0x4A4A4A));
        reportArea.setForeground(Color.WHITE);
        reportArea.setEditable(false);
        reportArea.setText("Laporan Harian: Memuat...");
        JScrollPane reportScroll = new JScrollPane(reportArea);
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.3;
        contentPanel.add(reportScroll, gbc);

        // Calorie Chart
        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(400, 200));
        gbc.gridy = 4;
        gbc.weighty = 0.7;
        contentPanel.add(chartPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void updateDashboard() {
        System.out.println("[DashboardPanel] Mengambil data dashboard untuk username: " + username);
        try {
            // Update total calories
            int totalCalories = foodController.getTotalCalories(username);
            totalCaloriesLabel.setText("Total Kalori: " + totalCalories + " kcal");
            System.out.println("[DashboardPanel] Total kalori diambil: " + totalCalories);

            // Update latest mood
            String latestMood = journalController.getLatestMood(username);
            latestMoodLabel.setText("Mood Terbaru: " + (latestMood != null ? latestMood : "Tidak ada data"));
            System.out.println("[DashboardPanel] Mood terbaru diambil: " + latestMood);

            // Update latest weight
            Double latestWeight = weightController.getLatestWeight(username);
            latestWeightLabel.setText("Berat Terbaru: " + (latestWeight != null ? latestWeight + " kg" : "Tidak ada data"));
            System.out.println("[DashboardPanel] Berat terbaru diambil: " + latestWeight);

            // Update report
            String report = "Laporan Harian untuk " + username + ":\n";
            report += "- Total Kalori: " + totalCalories + " kcal\n";
            report += "- Mood Terbaru: " + (latestMood != null ? latestMood : "Tidak ada data") + "\n";
            report += "- Berat Terbaru: " + (latestWeight != null ? latestWeight + " kg" : "Tidak ada data") + "\n";
            report += "- Catatan: Tambahkan data di Food Log, Diet Journal, atau Weight Tracking untuk pembaruan.\n";
            reportArea.setText(report);

            // Update chart
            List<Food> foods = foodController.getFoods(username, "");
            chartPanel.setChart(ChartGenerator.createCalorieChart(foods));
        } catch (SQLException e) {
            System.err.println("[DashboardPanel] SQL error di updateDashboard: " + e.getMessage());
            e.printStackTrace();
            totalCaloriesLabel.setText("Total Kalori: Error");
            latestMoodLabel.setText("Mood Terbaru: Error");
            latestWeightLabel.setText("Berat Terbaru: Error");
            reportArea.setText("Laporan Harian: Error saat memuat data.");
            JOptionPane.showMessageDialog(this, "Gagal memuat data dashboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("[DashboardPanel] Error umum di updateDashboard: " + e.getMessage());
            e.printStackTrace();
            totalCaloriesLabel.setText("Total Kalori: Error");
            latestMoodLabel.setText("Mood Terbaru: Error");
            latestWeightLabel.setText("Berat Terbaru: Error");
            reportArea.setText("Laporan Harian: Error saat memuat data.");
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startAutoUpdate() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> updateDashboard());
            }
        }, 0, 5000); // Update setiap 5 detik
        System.out.println("[DashboardPanel] Auto-update dashboard dimulai untuk username: " + username);
    }
}