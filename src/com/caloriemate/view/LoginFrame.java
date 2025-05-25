package com.caloriemate.view;

import com.caloriemate.controller.AuthController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends AbstractFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private AuthController authController;

    public LoginFrame() {
        super("CalorieMate - Login");
        authController = new AuthController();
        initComponents();
    }

    @Override
    protected void initComponents() {
        System.out.println("[LoginFrame] Inisialisasi LoginFrame");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(0x1C2526));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("CalorieMate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        styleTextField(usernameField);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(0x1C2526));
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        styleButton(loginButton);
        styleButton(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            System.out.println("[LoginFrame] Login attempt - Raw username: '" + username + "', length: " + (username != null ? username.length() : "null"));
            
            if (username == null || username.isEmpty()) {
                System.err.println("[LoginFrame] Login gagal: Username kosong");
                JOptionPane.showMessageDialog(this, "Username tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password == null || password.isEmpty()) {
                System.err.println("[LoginFrame] Login gagal: Password kosong");
                JOptionPane.showMessageDialog(this, "Password tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String processedUsername = username;
                System.out.println("[LoginFrame] Processed username: '" + processedUsername + "', length: " + processedUsername.length());
                if (authController.login(processedUsername, password)) {
                    System.out.println("[LoginFrame] Login berhasil, membuka MainFrame dengan username: " + processedUsername);
                    dispose();
                    MainFrame mainFrame = new MainFrame(processedUsername);
                    mainFrame.setVisible(true);
                } else {
                    System.err.println("[LoginFrame] Login gagal: Username atau password salah");
                    JOptionPane.showMessageDialog(this, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                System.err.println("[LoginFrame] Login error: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saat login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            System.out.println("[LoginFrame] Register attempt - Raw username: '" + username + "', length: " + (username != null ? username.length() : "null"));
            
            if (username == null || username.isEmpty()) {
                System.err.println("[LoginFrame] Registrasi gagal: Username kosong");
                JOptionPane.showMessageDialog(this, "Username tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password == null || password.isEmpty()) {
                System.err.println("[LoginFrame] Registrasi gagal: Password kosong");
                JOptionPane.showMessageDialog(this, "Password tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (username.length() < 3 || password.length() < 6) {
                System.err.println("[LoginFrame] Registrasi gagal: Username < 3 atau password < 6 karakter");
                JOptionPane.showMessageDialog(this, "Username minimal 3 karakter, password minimal 6 karakter!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String processedUsername = username;
                if (authController.register(processedUsername, password, processedUsername + "@example.com")) {
                    System.out.println("[LoginFrame] Registrasi berhasil: " + processedUsername);
                    JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    System.err.println("[LoginFrame] Registrasi gagal: Username sudah terdaftar");
                    JOptionPane.showMessageDialog(this, "Username sudah terdaftar!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                System.err.println("[LoginFrame] Register error: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saat registrasi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String currentText = usernameField.getText();
                System.out.println("[LoginFrame] Username field changed: '" + currentText + "', length: " + (currentText != null ? currentText.length() : "null"));
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String currentText = new String(passwordField.getPassword());
                System.out.println("[LoginFrame] Password field changed: '" + (currentText.isEmpty() ? "empty" : "non-empty") + "', length: " + (currentText != null ? currentText.length() : "null"));
            }
        });
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Roboto", Font.PLAIN, 14));
        field.setBackground(new Color(0x4A4A4A));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD3D3D3), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setCaretColor(Color.WHITE);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBackground(new Color(0xD3D3D3));
        button.setForeground(new Color(0x1C2526));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}