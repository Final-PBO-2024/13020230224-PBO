package com.caloriemate.view;

import com.caloriemate.controller.JournalController;
import com.caloriemate.model.Journal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DietJournalPanel extends JPanel {
    private JTextArea noteArea;
    private JComboBox<String> moodCombo;
    private JTable journalTable;
    private DefaultTableModel tableModel;
    private JournalController journalController;
    private String username;

    public DietJournalPanel(String username) {
        this.username = username;
        journalController = new JournalController();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0x1C2526));
        initComponents();
    }

    private void initComponents() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(0x1C2526));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0xD3D3D3)), "Tambah Jurnal",
            0, 0, new Font("Roboto", Font.PLAIN, 14), Color.WHITE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel noteLabel = new JLabel("Catatan:");
        noteLabel.setForeground(Color.WHITE);
        noteLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        inputPanel.add(noteLabel, gbc);
        noteArea = new JTextArea(4, 20);
        noteArea.setFont(new Font("Roboto", Font.PLAIN, 14));
        noteArea.setBackground(new Color(0x4A4A4A));
        noteArea.setForeground(Color.WHITE);
        noteArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD3D3D3), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane noteScroll = new JScrollPane(noteArea);
        gbc.gridx = 1;
        inputPanel.add(noteScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel moodLabel = new JLabel("Mood:");
        moodLabel.setForeground(Color.WHITE);
        moodLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        inputPanel.add(moodLabel, gbc);
        moodCombo = new JComboBox<>(new String[]{"Happy", "Neutral", "Sad"});
        moodCombo.setBackground(new Color(0x4A4A4A));
        moodCombo.setForeground(Color.WHITE);
        moodCombo.setFont(new Font("Roboto", Font.PLAIN, 14));
        gbc.gridx = 1;
        inputPanel.add(moodCombo, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(0x1C2526));
        JButton addButton = new JButton("Tambah");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Hapus");
        JButton restoreButton = new JButton("Pulihkan");
        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(restoreButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(restoreButton);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        String[] columns = {"ID", "Catatan", "Mood", "Tanggal"};
        tableModel = new DefaultTableModel(columns, 0);
        journalTable = new JTable(tableModel);
        journalTable.setBackground(new Color(0x4A4A4A));
        journalTable.setForeground(Color.WHITE);
        journalTable.setGridColor(new Color(0xD3D3D3));
        journalTable.setFont(new Font("Roboto", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(journalTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xD3D3D3)));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadJournalData();

        addButton.addActionListener(e -> {
            String note = noteArea.getText().trim();
            String mood = (String) moodCombo.getSelectedItem();
            if (note.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Catatan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                journalController.addJournal(username, note, mood);
                loadJournalData();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            int selectedRow = journalTable.getSelectedRow();
            if (selectedRow >= 0) {
                String note = noteArea.getText().trim();
                String mood = (String) moodCombo.getSelectedItem();
                if (note.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Catatan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    journalController.updateJournal(id, note, mood);
                    loadJournalData();
                    clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris untuk diupdate!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = journalTable.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    journalController.deleteJournal(id);
                    loadJournalData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris untuk dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        restoreButton.addActionListener(e -> {
            try {
                journalController.restoreJournal();
                loadJournalData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        journalTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = journalTable.getSelectedRow();
            if (selectedRow >= 0) {
                noteArea.setText((String) tableModel.getValueAt(selectedRow, 1));
                moodCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
            }
        });
    }

    private void loadJournalData() {
        tableModel.setRowCount(0);
        try {
            List<Journal> journals = journalController.getJournals(username);
            for (Journal journal : journals) {
                tableModel.addRow(new Object[]{journal.getId(), journal.getNote(), journal.getMood(), journal.getDate()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error mengambil data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        noteArea.setText("");
        moodCombo.setSelectedIndex(0);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setBackground(new Color(0xD3D3D3));
        button.setForeground(new Color(0x1C2526));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}