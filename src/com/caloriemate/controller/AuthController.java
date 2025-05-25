package com.caloriemate.controller;

import com.caloriemate.database.UserDAO;

import java.sql.SQLException;

public class AuthController {
    private UserDAO userDAO;

    public AuthController() {
        userDAO = new UserDAO();
    }

    public boolean login(String username, String password) {
        System.out.println("[AuthController] Login called - Username: '" + username + "', length: " + (username != null ? username.length() : "null"));
        System.out.println("[AuthController] Password: '" + (password.isEmpty() ? "empty" : "non-empty") + "', length: " + (password != null ? password.length() : "null"));
        
        if (username == null || username.isEmpty()) {
            System.err.println("[AuthController] Login gagal: Username kosong");
            return false;
        }
        if (password == null || password.isEmpty()) {
            System.err.println("[AuthController] Login gagal: Password kosong");
            return false;
        }
        
        try {
            boolean isValid = userDAO.validateUser(username, password);
            System.out.println("[AuthController] Login untuk username: " + username + ", valid: " + isValid);
            return isValid;
        } catch (SQLException e) {
            System.err.println("[AuthController] SQL error di login: " + e.getMessage());
            throw new RuntimeException("Gagal memvalidasi user: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[AuthController] Error di login: " + e.getMessage());
            throw new RuntimeException("Error saat login: " + e.getMessage());
        }
    }

    public boolean register(String username, String password, String email) {
        System.out.println("[AuthController] Register called - Username: '" + username + "', length: " + (username != null ? username.length() : "null"));
        
        if (username == null || username.isEmpty()) {
            System.err.println("[AuthController] Registrasi gagal: Username kosong");
            return false;
        }
        if (password == null || password.isEmpty()) {
            System.err.println("[AuthController] Registrasi gagal: Password kosong");
            return false;
        }
        if (email == null || email.isEmpty()) {
            System.err.println("[AuthController] Registrasi gagal: Email kosong");
            return false;
        }
        
        try {
            boolean success = userDAO.registerUser(username, password, email);
            System.out.println("[AuthController] Registrasi untuk username: " + username + ", sukses: " + success);
            return success;
        } catch (SQLException e) {
            System.err.println("[AuthController] SQL error di register: " + e.getMessage());
            throw new RuntimeException("Gagal mendaftarkan user: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[AuthController] Error di register: " + e.getMessage());
            throw new RuntimeException("Error saat registrasi: " + e.getMessage());
        }
    }
}