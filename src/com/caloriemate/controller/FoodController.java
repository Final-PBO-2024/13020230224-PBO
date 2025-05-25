package com.caloriemate.controller;

import com.caloriemate.database.FoodDAO;
import com.caloriemate.database.UserDAO;
import com.caloriemate.model.Food;

import java.sql.SQLException;
import java.util.List;

public class FoodController {
    private FoodDAO foodDAO;
    private UserDAO userDAO;

    public FoodController() {
        foodDAO = new FoodDAO();
        userDAO = new UserDAO();
    }

    public void addFood(String username, String name, int calories, String category) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[FoodController] addFood gagal: Username kosong");
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        try {
            int userId = userDAO.getUserId(username);
            foodDAO.addFood(userId, name, calories, category);
            System.out.println("[FoodController] Makanan ditambahkan untuk username: " + username);
        } catch (SQLException e) {
            System.err.println("[FoodController] SQL error di addFood: " + e.getMessage());
            throw new SQLException("Gagal menambah makanan: " + e.getMessage());
        }
    }

    public void updateFood(int id, String name, int calories, String category) throws SQLException {
        try {
            foodDAO.updateFood(id, name, calories, category);
            System.out.println("[FoodController] Makanan diupdate, ID: " + id);
        } catch (SQLException e) {
            System.err.println("[FoodController] SQL error di updateFood: " + e.getMessage());
            throw new SQLException("Gagal mengupdate makanan: " + e.getMessage());
        }
    }

    public void deleteFood(int id) throws SQLException {
        try {
            foodDAO.deleteFood(id);
            System.out.println("[FoodController] Makanan dihapus (soft delete), ID: " + id);
        } catch (SQLException e) {
            System.err.println("[FoodController] SQL error di deleteFood: " + e.getMessage());
            throw new SQLException("Gagal menghapus makanan: " + e.getMessage());
        }
    }

    public void restoreFood(int id) throws SQLException {
        try {
            foodDAO.restoreFood(id);
            System.out.println("[FoodController] Makanan dipulihkan, ID: " + id);
        } catch (SQLException e) {
            System.err.println("[FoodController] SQL error di restoreFood: " + e.getMessage());
            throw new SQLException("Gagal memulihkan makanan: " + e.getMessage());
        }
    }

    public void hardDeleteFood(int id) throws SQLException {
        try {
            foodDAO.hardDeleteFood(id);
            System.out.println("[FoodController] Makanan dihapus permanen, ID: " + id);
        } catch (SQLException e) {
            System.err.println("[FoodController] SQL error di hardDeleteFood: " + e.getMessage());
            throw new SQLException("Gagal menghapus permanen makanan: " + e.getMessage());
        }
    }

    public List<Food> getFoods(String username, String keyword) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[FoodController] getFoods gagal: Username kosong");
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        try {
            int userId = userDAO.getUserId(username);
            List<Food> foods = foodDAO.getFoods(userId, keyword);
            System.out.println("[FoodController] Makanan diambil untuk username: " + username + ", jumlah: " + foods.size());
            return foods;
        } catch (SQLException e) {
            System.err.println("[FoodController] SQL error di getFoods: " + e.getMessage());
            throw new SQLException("Gagal mengambil makanan: " + e.getMessage());
        }
    }

    public int getTotalCalories(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[FoodController] getTotalCalories gagal: Username kosong");
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        try {
            int userId = userDAO.getUserId(username);
            int totalCalories = foodDAO.getTotalCalories(userId);
            System.out.println("[FoodController] Total kalori diambil untuk username: " + username + ", nilai: " + totalCalories);
            return totalCalories;
        } catch (SQLException e) {
            System.err.println("[FoodController] SQL error di getTotalCalories: " + e.getMessage());
            throw new SQLException("Gagal mengambil total kalori: " + e.getMessage());
        }
    }
}