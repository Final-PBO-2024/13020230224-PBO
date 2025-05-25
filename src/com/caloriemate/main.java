package com.caloriemate;

import com.caloriemate.view.LoginFrame;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        System.out.println("[Main] Memulai aplikasi CalorieMate...");
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
            System.out.println("[Main] Look and feel FlatDarkLaf berhasil diterapkan");
        } catch (Exception e) {
            System.err.println("[Main] Gagal set look and feel: " + e.getMessage());
        }
        java.awt.EventQueue.invokeLater(() -> {
            System.out.println("[Main] Membuka LoginFrame");
            new LoginFrame().setVisible(true);
        });
    }
}