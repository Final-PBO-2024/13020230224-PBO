package com.caloriemate.controller;

import com.caloriemate.database.WeightDAO;
import com.caloriemate.model.Weight;

import java.util.Date;
import java.util.List;

public class WeightController {
    private WeightDAO weightDAO;

    public WeightController() {
        weightDAO = new WeightDAO();
    }

    public void addWeight(String username, double weight) {
        try {
            weightDAO.addWeight(username, weight, new Date());
        } catch (Exception e) {
            throw new RuntimeException("Error menambahkan berat: " + e.getMessage());
        }
    }

    public void updateWeight(int id, double weight) {
        try {
            weightDAO.updateWeight(id, weight);
        } catch (Exception e) {
            throw new RuntimeException("Error mengupdate berat: " + e.getMessage());
        }
    }

    public void deleteWeight(int id) {
        try {
            weightDAO.deleteWeight(id);
        } catch (Exception e) {
            throw new RuntimeException("Error menghapus berat: " + e.getMessage());
        }
    }

    public void restoreWeight() {
        try {
            weightDAO.restoreWeight();
        } catch (Exception e) {
            throw new RuntimeException("Error memulihkan berat: " + e.getMessage());
        }
    }

    public List<Weight> getWeights(String username) {
        try {
            return weightDAO.getWeights(username);
        } catch (Exception e) {
            throw new RuntimeException("Error mengambil data berat: " + e.getMessage());
        }
    }

    public Double getLatestWeight(String username) {
        try {
            return weightDAO.getLatestWeight(username);
        } catch (Exception e) {
            throw new RuntimeException("Error mengambil berat terbaru: " + e.getMessage());
        }
    }
}