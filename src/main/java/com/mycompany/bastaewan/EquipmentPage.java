package com.mycompany.bastaewan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import com.mycompany.bastaewan.model.Equipment;
import com.mycompany.bastaewan.service.DataStore;
import com.mycompany.bastaewan.service.EquipmentService;

public class EquipmentPage extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private EquipmentService service;
    private final String[] columns = {"ID", "Device Name", "Type", "Status", "Health", "Location", "Category"};

    public EquipmentPage() {
        service = DataStore.getInstance().getEquipmentService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(245, 247, 250));
        JLabel title = new JLabel("Equipment Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleBar.add(title, BorderLayout.WEST);

        // Search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(15);
        searchField.setToolTipText("Search by device name...");
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(0, 102, 204));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> searchEquipment(searchField.getText().trim()));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        titleBar.add(searchPanel, BorderLayout.EAST);

        add(titleBar, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBackground(new Color(39, 174, 96));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> showAddDialog());

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBackground(new Color(0, 102, 204));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> showEditDialog());

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelected());

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> refreshTable());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Equipment> list = service.getAll();
        for (Equipment e : list) {
            tableModel.addRow(e.toTableRow());
        }
    }

    private void searchEquipment(String keyword) {
        tableModel.setRowCount(0);
        ArrayList<Equipment> list = service.getAll();
        for (Equipment e : list) {
            if (keyword.isEmpty() || e.getDeviceName().toLowerCase().contains(keyword.toLowerCase())
                    || e.getType().toLowerCase().contains(keyword.toLowerCase())
                    || e.getStatus().toLowerCase().contains(keyword.toLowerCase())) {
                tableModel.addRow(e.toTableRow());
            }
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Equipment", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel form = createFormPanel(null);
        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        save.setBackground(new Color(39, 174, 96));
        save.setForeground(Color.WHITE);
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            Equipment eq = getFormData(form);
            if (eq != null) {
                service.add(eq);
                refreshTable();
                dialog.dispose();
            }
        });
        cancel.addActionListener(e -> dialog.dispose());
        btnPanel.add(save);
        btnPanel.add(cancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an equipment to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        Equipment existing = service.getById(id);
        if (existing == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Equipment", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel form = createFormPanel(existing);
        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Update");
        save.setBackground(new Color(0, 102, 204));
        save.setForeground(Color.WHITE);
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            Equipment eq = getFormData(form);
            if (eq != null) {
                service.update(id, eq);
                refreshTable();
                dialog.dispose();
            }
        });
        cancel.addActionListener(e -> dialog.dispose());
        btnPanel.add(save);
        btnPanel.add(cancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an equipment to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this equipment?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.delete(id);
            refreshTable();
        }
    }

    private JPanel createFormPanel(Equipment existing) {
        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] types = {"Workstation", "Server", "Network Switch", "Router", "Printer"};
        String[] statuses = {"Online", "Offline", "Needs Attention"};
        String[] categories = {"Hardware", "Software", "Network"};

        JTextField txtName = new JTextField(existing != null ? existing.getDeviceName() : "");
        JComboBox<String> cmbType = new JComboBox<>(types);
        JComboBox<String> cmbStatus = new JComboBox<>(statuses);
        JTextField txtHealth = new JTextField(existing != null ? String.valueOf(existing.getHealthScore()) : "100");
        JTextField txtLocation = new JTextField(existing != null ? existing.getLocation() : "");
        JComboBox<String> cmbCategory = new JComboBox<>(categories);

        if (existing != null) {
            cmbType.setSelectedItem(existing.getType());
            cmbStatus.setSelectedItem(existing.getStatus());
            cmbCategory.setSelectedItem(existing.getCategory());
        }

        form.add(new JLabel("Device Name:"));  form.add(txtName);
        form.add(new JLabel("Type:"));          form.add(cmbType);
        form.add(new JLabel("Status:"));        form.add(cmbStatus);
        form.add(new JLabel("Health Score:"));  form.add(txtHealth);
        form.add(new JLabel("Location:"));      form.add(txtLocation);
        form.add(new JLabel("Category:"));      form.add(cmbCategory);
        form.add(new JLabel(""));               form.add(new JLabel(""));

        form.putClientProperty("txtName", txtName);
        form.putClientProperty("cmbType", cmbType);
        form.putClientProperty("cmbStatus", cmbStatus);
        form.putClientProperty("txtHealth", txtHealth);
        form.putClientProperty("txtLocation", txtLocation);
        form.putClientProperty("cmbCategory", cmbCategory);

        return form;
    }

    private Equipment getFormData(JPanel form) {
        JTextField txtName = (JTextField) form.getClientProperty("txtName");
        JComboBox<?> cmbType = (JComboBox<?>) form.getClientProperty("cmbType");
        JComboBox<?> cmbStatus = (JComboBox<?>) form.getClientProperty("cmbStatus");
        JTextField txtHealth = (JTextField) form.getClientProperty("txtHealth");
        JTextField txtLocation = (JTextField) form.getClientProperty("txtLocation");
        JComboBox<?> cmbCategory = (JComboBox<?>) form.getClientProperty("cmbCategory");

        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Device name is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        int health;
        try {
            health = Integer.parseInt(txtHealth.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Health score must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new Equipment(0, name,
                cmbType.getSelectedItem().toString(),
                cmbStatus.getSelectedItem().toString(),
                health,
                txtLocation.getText().trim(),
                cmbCategory.getSelectedItem().toString());
    }
}
