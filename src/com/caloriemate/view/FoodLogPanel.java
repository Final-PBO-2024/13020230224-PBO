package com.caloriemate.view;

import com.caloriemate.controller.FoodController;
import com.caloriemate.model.Food;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class FoodLogPanel extends JPanel {
    private final String username;
    private final FoodController foodController;
    private JTextField nameField, caloriesField, searchField;
    private JComboBox<String> categoryCombo;
    private JTable foodTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton, restoreButton, hardDeleteButton;

    public FoodLogPanel(String username) {
        System.out.println("[FoodLogPanel] NEW INSTANCE CREATED with username: '" + username + "', length: " + (username != null ? username.length() : "null"));
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[FoodLogPanel] FATAL ERROR: Username null atau kosong. Stack trace:");
            new Exception("Username null detected").printStackTrace();
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        this.username = username;
        this.foodController = new FoodController();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0x1C2526));
        initComponents();
        loadFoods();
        System.out.println("[FoodLogPanel] FoodLogPanel selesai diinisialisasi untuk username: " + username);
    }

    private void initComponents() {
        System.out.println("[FoodLogPanel] Memulai initComponents untuk username: " + username);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(0x1C2526));
        JLabel titleLabel = new JLabel("Food Log");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(0x1C2526));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Nama:");
        nameLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Roboto", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(nameField, gbc);

        JLabel caloriesLabel = new JLabel("Kalori:");
        caloriesLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        caloriesLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(caloriesLabel, gbc);

        caloriesField = new JTextField(20);
        caloriesField.setFont(new Font("Roboto", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(caloriesField, gbc);

        JLabel categoryLabel = new JLabel("Kategori:");
        categoryLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        categoryLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(categoryLabel, gbc);

        String[] categories = {"Breakfast", "Lunch", "Dinner", "Snack"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Roboto", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(categoryCombo, gbc);

        addButton = new JButton("Tambah");
        addButton.setFont(new Font("Roboto", Font.BOLD, 14));
        addButton.setBackground(new Color(0xD3D3D3));
        addButton.setForeground(new Color(0x1C2526));
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(addButton, gbc);

        updateButton = new JButton("Update");
        updateButton.setFont(new Font("Roboto", Font.BOLD, 14));
        updateButton.setBackground(new Color(0xD3D3D3));
        updateButton.setForeground(new Color(0x1C2526));
        gbc.gridx = 1;
        gbc.gridy = 3;
        inputPanel.add(updateButton, gbc);

        deleteButton = new JButton("Hapus");
        deleteButton.setFont(new Font("Roboto", Font.BOLD, 14));
        deleteButton.setBackground(new Color(0xD3D3D3));
        deleteButton.setForeground(new Color(0x1C2526));
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(deleteButton, gbc);

        restoreButton = new JButton("Pulihkan");
        restoreButton.setFont(new Font("Roboto", Font.BOLD, 14));
        restoreButton.setBackground(new Color(0xD3D3D3));
        restoreButton.setForeground(new Color(0x1C2526));
        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(restoreButton, gbc);

        hardDeleteButton = new JButton("Hapus Permanen");
        hardDeleteButton.setFont(new Font("Roboto", Font.BOLD, 14));
        hardDeleteButton.setBackground(new Color(0xFF4040));
        hardDeleteButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        inputPanel.add(hardDeleteButton, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(0x1C2526));
        JLabel searchLabel = new JLabel("Cari:");
        searchLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        searchLabel.setForeground(Color.WHITE);
        searchPanel.add(searchLabel);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Roboto", Font.PLAIN, 14));
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Cari");
        searchButton.setFont(new Font("Roboto", Font.BOLD, 14));
        searchButton.setBackground(new Color(0xD3D3D3));
        searchButton.setForeground(new Color(0x1C2526));
        searchPanel.add(searchButton);

        // Table
        String[] columns = {"ID", "Nama", "Kalori", "Kategori", "Tanggal", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        foodTable = new JTable(tableModel);
        foodTable.setFont(new Font("Roboto", Font.PLAIN, 14));
        foodTable.setBackground(new Color(0x4A4A4A));
        foodTable.setForeground(Color.WHITE);
        foodTable.setSelectionBackground(new Color(0xD3D3D3));
        foodTable.setSelectionForeground(new Color(0x1C2526));
        foodTable.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(foodTable);

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(0x1C2526));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // Event Listeners
        addButton.addActionListener(e -> addFood());
        updateButton.addActionListener(e -> updateFood());
        deleteButton.addActionListener(e -> deleteFood());
        restoreButton.addActionListener(e -> restoreFood());
        hardDeleteButton.addActionListener(e -> hardDeleteFood());
        searchButton.addActionListener(e -> loadFoods());
        searchField.addActionListener(e -> loadFoods());
        foodTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFields();
            }
        });
    }

    private void addFood() {
        try {
            String name = nameField.getText().trim();
            String caloriesText = caloriesField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            if (name.isEmpty() || caloriesText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan kalori harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int calories = Integer.parseInt(caloriesText);
            foodController.addFood(username, name, calories, category);
            JOptionPane.showMessageDialog(this, "Makanan ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadFoods();
            System.out.println("[FoodLogPanel] Makanan ditambahkan: " + name + ", kalori: " + calories);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kalori harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("[FoodLogPanel] Error format angka: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambah makanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("[FoodLogPanel] SQL error di addFood: " + e.getMessage());
        }
    }

    private void updateFood() {
        try {
            int selectedRow = foodTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih makanan terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText().trim();
            String caloriesText = caloriesField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            if (name.isEmpty() || caloriesText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan kalori harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int calories = Integer.parseInt(caloriesText);
            foodController.updateFood(id, name, calories, category);
            JOptionPane.showMessageDialog(this, "Makanan diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadFoods();
            System.out.println("[FoodLogPanel] Makanan diupdate, ID: " + id);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kalori harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("[FoodLogPanel] Error format angka: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate makanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("[FoodLogPanel] SQL error di updateFood: " + e.getMessage());
        }
    }

    private void deleteFood() {
        try {
            int selectedRow = foodTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih makanan terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus makanan ini? (Bisa dipulihkan)", "Konfirmasi Hapus", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                foodController.deleteFood(id);
                JOptionPane.showMessageDialog(this, "Makanan dihapus! (Bisa dipulihkan)", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadFoods();
                System.out.println("[FoodLogPanel] Makanan dihapus, ID: " + id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus makanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("[FoodLogPanel] SQL error di deleteFood: " + e.getMessage());
        }
    }

    private void restoreFood() {
        try {
            int selectedRow = foodTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih makanan yang dihapus terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String status = (String) tableModel.getValueAt(selectedRow, 5);
            if (!status.equals("Dihapus")) {
                JOptionPane.showMessageDialog(this, "Pilih makanan yang sudah dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            foodController.restoreFood(id);
            JOptionPane.showMessageDialog(this, "Makanan dipulihkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadFoods();
            System.out.println("[FoodLogPanel] Makanan dipulihkan, ID: " + id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memulihkan makanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("[FoodLogPanel] SQL error di restoreFood: " + e.getMessage());
        }
    }

    private void hardDeleteFood() {
        try {
            int selectedRow = foodTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih makanan yang dihapus terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String status = (String) tableModel.getValueAt(selectedRow, 5);
            if (!status.equals("Dihapus")) {
                JOptionPane.showMessageDialog(this, "Pilih makanan yang sudah dihapus untuk penghapusan permanen!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus makanan ini secara permanen? (Tidak bisa dipulihkan)", "Konfirmasi Hapus Permanen", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                foodController.hardDeleteFood(id);
                JOptionPane.showMessageDialog(this, "Makanan dihapus secara permanen!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadFoods();
                System.out.println("[FoodLogPanel] Makanan dihapus permanen, ID: " + id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus permanen makanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("[FoodLogPanel] SQL error di hardDeleteFood: " + e.getMessage());
        }
    }

    private void loadFoods() {
        try {
            String keyword = searchField.getText().trim();
            List<Food> foods = foodController.getFoods(username, keyword);
            tableModel.setRowCount(0);
            for (Food food : foods) {
                String status = food.isDeleted() ? "Dihapus" : "Aktif";
                tableModel.addRow(new Object[]{food.getId(), food.getName(), food.getCalories(), food.getCategory(), food.getDate(), status});
            }
            System.out.println("[FoodLogPanel] Makanan diambil untuk username: " + username + ", jumlah: " + foods.size());
            updateRowColors();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat makanan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("[FoodLogPanel] SQL error di loadFoods: " + e.getMessage());
        }
    }

    private void updateRowColors() {
        foodTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) table.getValueAt(row, 5);
                if (status.equals("Dihapus")) {
                    c.setForeground(Color.GRAY);
                } else {
                    c.setForeground(Color.WHITE);
                }
                if (isSelected) {
                    c.setBackground(new Color(0xD3D3D3));
                    c.setForeground(new Color(0x1C2526));
                } else {
                    c.setBackground(new Color(0x4A4A4A));
                }
                return c;
            }
        });
    }

    private void populateFields() {
        int selectedRow = foodTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            caloriesField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
            categoryCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
        }
    }

    private void clearFields() {
        nameField.setText("");
        caloriesField.setText("");
        categoryCombo.setSelectedIndex(0);
        foodTable.clearSelection();
    }
}