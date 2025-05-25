package com.caloriemate.model;

import java.util.Date;

public class Food {
    private int id;
    private int userId;
    private String name;
    private int calories;
    private String category;
    private Date date;
    private boolean isDeleted;

    public Food(int id, int userId, String name, int calories, String category, Date date, boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.calories = calories;
        this.category = category;
        this.date = date;
        this.isDeleted = isDeleted;
    }

    public Food() {
        // Konstruktor kosong default
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
