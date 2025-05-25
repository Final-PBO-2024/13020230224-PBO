package com.caloriemate.view;

import com.caloriemate.controller.WeightController;
import com.caloriemate.model.Weight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WeightTrackingPanel extends JPanel {
    private JTextField weightField;
    private JTable weightTable;
    private DefaultTableModel tableModel;
    private WeightController weightController;
    private String username;

    public WeightTrackingPanel(String username) {
        this.username = username;
        weightController = new WeightController();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0x1C2526));
        initComponents();
    }

    private void initComponents() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(0x1C2526));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0xD3D3D3)), "Tambah Berat",
            0, 0, new Font("Roboto", Font.PLAIN, 14), Color.WHITE));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel weightLabel = new JLabel("Berat (kg):");
        weightLabel.setForeground(Color.WHITE);
        weightLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        inputPanel.add(weightLabel, gbc);
        weightField = new JTextField(20);
        styleTextField(weightField);
        gbc.gridx = 1;
        inputPanel.add(weightField, gbc);

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
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        String[] columns = {"ID", "Berat", "Tanggal"};
        tableModel = new DefaultTableModel(columns, 0);
        weightTable = new JTable(tableModel);
        weightTable.setBackground(new Color(0x4A4A4A));
        weightTable.setForeground(Color.WHITE);
        weightTable.setGridColor(new Color(0xD3D3D3));
        weightTable.setFont(new Font("Roboto", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(weightTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xD3D3D3)));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadWeightData();

        addButton.addActionListener(e -> {
            String weightText = weightField.getText().trim();
            if (weightText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Berat tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double weight = Double.parseDouble(weightText);
                if (weight <= 0) {
                    JOptionPane.showMessageDialog(this, "Berat harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                weightController.addWeight(username, weight);
                loadWeightData();
                clearFields();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Berat harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            int selectedRow = weightTable.getSelectedRow();
            if (selectedRow >= 0) {
                String weightText = weightField.getText().trim();
                if (weightText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Berat tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    double weight = Double.parseDouble(weightText);
                    if (weight <= 0) {
                        JOptionPane.showMessageDialog(this, "Berat harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    weightController.updateWeight(id, weight);
                    loadWeightData();
                    clearFields();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Berat harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris untuk diupdate!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = weightTable.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    weightController.deleteWeight(id);
                    loadWeightData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris untuk dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        restoreButton.addActionListener(e -> {
            try {
                weightController.restoreWeight();
                loadWeightData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        weightTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = weightTable.getSelectedRow();
            if (selectedRow >= 0) {
                weightField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
            }
        });
    }

    private void loadWeightData() {
        tableModel.setRowCount(0);
        try {
            List<Weight> weights = weightController.getWeights(username);
            for (Weight weight : weights) {
                tableModel.addRow(new Object[]{weight.getId(), weight.getWeight(), weight.getDate()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error mengambil data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        weightField.setText("");
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Roboto", Font.PLAIN, 14));
        field.setBackground(new Color(0x4A4A4A));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD3D3D3), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
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