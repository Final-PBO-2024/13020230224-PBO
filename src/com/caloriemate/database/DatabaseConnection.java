package com.caloriemate.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/caloriemate_database?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Ganti dengan password Anda

    public static Connection getConnection() throws SQLException {
        System.out.println("[DatabaseConnection] Mencoba koneksi ke database: " + URL);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DatabaseConnection] Koneksi database berhasil, URL: " + URL);
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseConnection] Driver MySQL tidak ditemukan: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Gagal koneksi database: " + e.getMessage() + ", SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
            throw new SQLException("Gagal koneksi database: " + e.getMessage());
        }
    }
}