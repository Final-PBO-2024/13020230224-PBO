package com.caloriemate.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean validateUser(String username, String password) throws SQLException {
        System.out.println("[UserDAO] validateUser - Username: '" + username + "', length: " + (username != null ? username.length() : "null"));
        System.out.println("[UserDAO] Password: '" + (password.isEmpty() ? "empty" : "non-empty") + "', length: " + (password != null ? password.length() : "null"));
        
        if (username == null || username.isEmpty()) {
            System.err.println("[UserDAO] Validasi gagal: Username kosong");
            return false;
        }
        if (password == null || password.isEmpty()) {
            System.err.println("[UserDAO] Validasi gagal: Password kosong");
            return false;
        }
        
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            System.out.println("[UserDAO] Validasi user: " + username + ", ditemukan: " + exists);
            return exists;
        } catch (SQLException e) {
            System.err.println("[UserDAO] SQL error di validateUser: " + e.getMessage());
            throw new SQLException("Gagal memvalidasi user: " + e.getMessage());
        }
    }

    public boolean registerUser(String username, String password, String email) throws SQLException {
        System.out.println("[UserDAO] registerUser - Username: '" + username + "', length: " + (username != null ? username.length() : "null"));
        
        String checkQuery = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.err.println("[UserDAO] Registrasi gagal: Username sudah ada: " + username);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] SQL error di registerUser (check): " + e.getMessage());
            throw new SQLException("Gagal memeriksa username: " + e.getMessage());
        }

        String insertQuery = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            int rows = stmt.executeUpdate();
            System.out.println("[UserDAO] Registrasi user: " + username + ", rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] SQL error di registerUser (insert): " + e.getMessage());
            throw new SQLException("Gagal mendaftarkan user: " + e.getMessage());
        }
    }

    public int getUserId(String username) throws SQLException {
        System.out.println("[UserDAO] getUserId - Username: '" + username + "', length: " + (username != null ? username.length() : "null"));
        
        if (username == null || username.isEmpty()) {
            System.err.println("[UserDAO] getUserId gagal: Username kosong");
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                System.out.println("[UserDAO] User ID ditemukan: " + userId + " untuk username: " + username);
                return userId;
            } else {
                System.err.println("[UserDAO] User tidak ditemukan di database: " + username);
                throw new SQLException("User tidak ditemukan: " + username);
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] SQL error di getUserId: " + e.getMessage());
            throw new SQLException("Gagal mengambil user ID: " + e.getMessage());
        }
    }
}