package com.caloriemate.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends AbstractFrame {
    private final String username;
    private JTabbedPane tabbedPane;

    public MainFrame(String username) {
        super("CalorieMate - Main");
        System.out.println("[MainFrame] NEW INSTANCE CREATED with username: '" + username + "', length: " + (username != null ? username.length() : "null") + ", Thread: " + Thread.currentThread().getName());
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[MainFrame] FATAL ERROR: Username null atau kosong. Stack trace:");
            new Exception("Username null detected").printStackTrace();
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        this.username = username;
        System.out.println("[MainFrame] Username diset: " + username);
        initComponents();
        System.out.println("[MainFrame] MainFrame selesai diinisialisasi dengan username: " + username);
    }

    @Override
    protected void initComponents() {
        System.out.println("[MainFrame] Memulai initComponents dengan username: " + username);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(0x1C2526));
        JLabel welcomeLabel = new JLabel("Selamat Datang, " + username);
        welcomeLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Roboto", Font.BOLD, 14));
        logoutButton.setBackground(new Color(0xD3D3D3));
        logoutButton.setForeground(new Color(0x1C2526));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(logoutButton);
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Roboto", Font.PLAIN, 14));
        tabbedPane.setBackground(new Color(0x1C2526));
        tabbedPane.setForeground(Color.WHITE);

        try {
            System.out.println("[MainFrame] Menambahkan tab DashboardPanel dengan username: " + username);
            tabbedPane.addTab("Dashboard", new DashboardPanel(username));
            System.out.println("[MainFrame] Menambahkan tab FoodLogPanel dengan username: " + username);
            tabbedPane.addTab("Food Log", new FoodLogPanel(username));
            System.out.println("[MainFrame] Menambahkan tab DietJournalPanel dengan username: " + username);
            tabbedPane.addTab("Diet Journal", new DietJournalPanel(username));
            System.out.println("[MainFrame] Menambahkan tab WeightTrackingPanel dengan username: " + username);
            tabbedPane.addTab("Weight Tracking", new WeightTrackingPanel(username));
            System.out.println("[MainFrame] Menambahkan tab ReportsPanel dengan username: " + username);
            tabbedPane.addTab("Reports", new ReportsPanel(username));
        } catch (Exception e) {
            System.err.println("[MainFrame] Error saat menambahkan tab: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal inisialisasi tab: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(tabbedPane, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> {
            System.out.println("[MainFrame] Logout dilakukan untuk username: " + username);
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}