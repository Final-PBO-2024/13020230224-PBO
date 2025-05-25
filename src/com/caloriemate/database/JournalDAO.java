package com.caloriemate.database;

import com.caloriemate.model.Journal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JournalDAO {
    private DatabaseConnection dbConnection;

    public JournalDAO() {
        dbConnection = new DatabaseConnection();
    }

    public void addJournal(String username, String note, String mood, Date date) throws SQLException {
        String sql = "INSERT INTO journals (user_id, note, mood, date) " +
                     "SELECT id, ?, ?, ? FROM users WHERE username = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, note);
            stmt.setString(2, mood);
            stmt.setDate(3, new java.sql.Date(date.getTime()));
            stmt.setString(4, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal menambahkan jurnal, user tidak ditemukan!");
            }
        }
    }

    public void updateJournal(int id, String note, String mood) throws SQLException {
        String sql = "UPDATE journals SET note = ?, mood = ? WHERE id = ? AND is_deleted = FALSE";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, note);
            stmt.setString(2, mood);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    public void deleteJournal(int id) throws SQLException {
        String sql = "UPDATE journals SET is_deleted = TRUE WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void restoreJournal() throws SQLException {
        String sql = "UPDATE journals SET is_deleted = FALSE";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    public List<Journal> getJournals(String username) throws SQLException {
        List<Journal> journals = new ArrayList<>();
        String sql = "SELECT j.* FROM journals j JOIN users u ON j.user_id = u.id " +
                     "WHERE u.username = ? AND j.is_deleted = FALSE";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Journal journal = new Journal(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("note"),
                    rs.getString("mood"),
                    rs.getDate("date"),
                    rs.getBoolean("is_deleted")
                );
                journals.add(journal);
            }
        }
        return journals;
    }

    public String getLatestMood(String username) throws SQLException {
        String sql = "SELECT j.mood FROM journals j JOIN users u ON j.user_id = u.id " +
                     "WHERE u.username = ? AND j.is_deleted = FALSE ORDER BY j.date DESC LIMIT 1";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("mood");
            }
            return null;
        }
    }
}