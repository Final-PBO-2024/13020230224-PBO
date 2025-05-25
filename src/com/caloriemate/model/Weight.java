package com.caloriemate.model;

import java.util.Date;

public class Weight {
    private int id;
    private int userId;
    private double weight;
    private Date date;
    private boolean isDeleted;

    // Constructor
    public Weight(int id, int userId, double weight, Date date, boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.date = date;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}