package com.caloriemate.model;

import java.util.Date;

public class Journal {
    private int id;
    private int userId;
    private String note;
    private String mood;
    private Date date;
    private boolean isDeleted;

    // Constructor
    public Journal(int id, int userId, String note, String mood, Date date, boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.note = note;
        this.mood = mood;
        this.date = date;
        this.isDeleted = isDeleted;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}