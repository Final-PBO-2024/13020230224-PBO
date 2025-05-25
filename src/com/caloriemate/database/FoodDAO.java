package com.caloriemate.database;

import com.caloriemate.model.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {
    public void addFood(int userId, String name, int calories, String category) throws SQLException {
        String query = "INSERT INTO foods (user_id, name, calories, category, date) VALUES (?, ?, ?, ?, CURDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, name);
            stmt.setInt(3, calories);
            stmt.setString(4, category);
            stmt.executeUpdate();
            System.out.println("[FoodDAO] Makanan ditambahkan untuk user_id: " + userId);
        }
    }

    public void updateFood(int id, String name, int calories, String category) throws SQLException {
        String query = "UPDATE foods SET name = ?, calories = ?, category = ? WHERE id = ? AND is_deleted = FALSE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, calories);
            stmt.setString(3, category);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            System.out.println("[FoodDAO] Makanan diupdate, ID: " + id);
        }
    }

    public void deleteFood(int id) throws SQLException {
        String query = "UPDATE foods SET is_deleted = TRUE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("[FoodDAO] Makanan dihapus (soft delete), ID: " + id);
        }
    }

    public void restoreFood(int id) throws SQLException {
        String query = "UPDATE foods SET is_deleted = FALSE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("[FoodDAO] Makanan dipulihkan, ID: " + id);
        }
    }

    public void hardDeleteFood(int id) throws SQLException {
        String query = "DELETE FROM foods WHERE id = ? AND is_deleted = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Gagal menghapus permanen: Makanan tidak dalam status dihapus");
            }
            System.out.println("[FoodDAO] Makanan dihapus permanen, ID: " + id);
        }
    }

    public List<Food> getFoods(int userId, String keyword) throws SQLException {
        List<Food> foods = new ArrayList<>();
        String query = "SELECT * FROM foods WHERE user_id = ? AND name LIKE ? ORDER BY date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Food food = new Food();
                food.setId(rs.getInt("id"));
                food.setUserId(rs.getInt("user_id"));
                food.setName(rs.getString("name"));
                food.setCalories(rs.getInt("calories"));
                food.setCategory(rs.getString("category"));
                food.setDate(rs.getDate("date"));
                food.setDeleted(rs.getBoolean("is_deleted"));
                foods.add(food);
            }
            System.out.println("[FoodDAO] Makanan diambil untuk user_id: " + userId + ", jumlah: " + foods.size());
        }
        return foods;
    }

    public int getTotalCalories(int userId) throws SQLException {
        String query = "SELECT SUM(calories) AS total FROM foods WHERE user_id = ? AND is_deleted = FALSE AND date = CURDATE()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("[FoodDAO] Total kalori diambil untuk user_id: " + userId + ", nilai: " + total);
                return total;
            }
            return 0;
        }
    }
}