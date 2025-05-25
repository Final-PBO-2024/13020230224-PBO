package com.caloriemate.database;

import com.caloriemate.model.Weight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeightDAO {
    private DatabaseConnection dbConnection;

    public WeightDAO() {
        dbConnection = new DatabaseConnection();
    }

    public void addWeight(String username, double weight, Date date) throws SQLException {
        String sql = "INSERT INTO weights (user_id, weight, date) " +
                     "SELECT id, ?, ? FROM users WHERE username = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, weight);
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            stmt.setString(3, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal menambahkan berat, user tidak ditemukan!");
            }
        }
    }

    public void updateWeight(int id, double weight) throws SQLException {
        String sql = "UPDATE weights SET weight = ? WHERE id = ? AND is_deleted = FALSE";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, weight);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void deleteWeight(int id) throws SQLException {
        String sql = "UPDATE weights SET is_deleted = TRUE WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void restoreWeight() throws SQLException {
        String sql = "UPDATE weights SET is_deleted = FALSE";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    public List<Weight> getWeights(String username) throws SQLException {
        List<Weight> weights = new ArrayList<>();
        String sql = "SELECT w.* FROM weights w JOIN users u ON w.user_id = u.id " +
                     "WHERE u.username = ? AND w.is_deleted = FALSE";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Weight weight = new Weight(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDouble("weight"),
                    rs.getDate("date"),
                    rs.getBoolean("is_deleted")
                );
                weights.add(weight);
            }
        }
        return weights;
    }

    public Double getLatestWeight(String username) throws SQLException {
        String sql = "SELECT w.weight FROM weights w JOIN users u ON w.user_id = u.id " +
                     "WHERE u.username = ? AND w.is_deleted = FALSE ORDER BY w.date DESC LIMIT 1";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("weight");
            }
            return null;
        }
    }
}