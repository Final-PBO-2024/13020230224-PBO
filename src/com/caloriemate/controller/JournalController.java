package com.caloriemate.controller;

import com.caloriemate.database.JournalDAO;
import com.caloriemate.model.Journal;

import java.util.Date;
import java.util.List;

public class JournalController {
    private JournalDAO journalDAO;

    public JournalController() {
        journalDAO = new JournalDAO();
    }

    public void addJournal(String username, String note, String mood) {
        try {
            journalDAO.addJournal(username, note, mood, new Date());
        } catch (Exception e) {
            throw new RuntimeException("Error menambahkan jurnal: " + e.getMessage());
        }
    }

    public void updateJournal(int id, String note, String mood) {
        try {
            journalDAO.updateJournal(id, note, mood);
        } catch (Exception e) {
            throw new RuntimeException("Error mengupdate jurnal: " + e.getMessage());
        }
    }

    public void deleteJournal(int id) {
        try {
            journalDAO.deleteJournal(id);
        } catch (Exception e) {
            throw new RuntimeException("Error menghapus jurnal: " + e.getMessage());
        }
    }

    public void restoreJournal() {
        try {
            journalDAO.restoreJournal();
        } catch (Exception e) {
            throw new RuntimeException("Error memulihkan jurnal: " + e.getMessage());
        }
    }

    public List<Journal> getJournals(String username) {
        try {
            return journalDAO.getJournals(username);
        } catch (Exception e) {
            throw new RuntimeException("Error mengambil data jurnal: " + e.getMessage());
        }
    }

    public String getLatestMood(String username) {
        try {
            return journalDAO.getLatestMood(username);
        } catch (Exception e) {
            throw new RuntimeException("Error mengambil mood terbaru: " + e.getMessage());
        }
    }
}