package com.mycompany.bastaewan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import com.mycompany.bastaewan.model.MaintenanceTask;
import com.mycompany.bastaewan.service.DataStore;
import com.mycompany.bastaewan.service.MaintenanceService;
import com.mycompany.bastaewan.service.EquipmentService;

public class MaintenancePage extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private MaintenanceService service;
    private final String[] columns = {"ID", "Equipment", "Task", "Due Date", "Status"};

    public MaintenancePage() {
        service = DataStore.getInstance().getMaintenanceService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Title
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(245, 247, 250));
        JLabel title = new JLabel("Maintenance Schedule");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleBar.add(title, BorderLayout.WEST);

        // Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        String[] filterOptions = {"All", "Scheduled", "In Progress", "Completed", "Overdue"};
        JComboBox<String> cmbFilter = new JComboBox<>(filterOptions);
        cmbFilter.addActionListener(e -> filterByStatus(cmbFilter.getSelectedItem().toString()));
        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(cmbFilter);
        titleBar.add(filterPanel, BorderLayout.EAST);

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
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Schedule Task");
        btnAdd.setBackground(new Color(39, 174, 96));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> showAddDialog());

        JButton btnStart = new JButton("Start");
        btnStart.setBackground(new Color(0, 102, 204));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.addActionListener(e -> changeStatus("In Progress"));

        JButton btnComplete = new JButton("Complete");
        btnComplete.setBackground(new Color(255, 153, 0));
        btnComplete.setForeground(Color.WHITE);
        btnComplete.setFocusPainted(false);
        btnComplete.addActionListener(e -> changeStatus("Completed"));

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelected());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnStart);
        buttonPanel.add(btnComplete);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (MaintenanceTask t : service.getAll()) {
            tableModel.addRow(t.toTableRow());
        }
    }

    private void filterByStatus(String status) {
        tableModel.setRowCount(0);
        for (MaintenanceTask t : service.getAll()) {
            if ("All".equals(status) || t.getStatus().equals(status)) {
                tableModel.addRow(t.toTableRow());
            }
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Schedule Maintenance", true);
        dialog.setSize(420, 280);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        EquipmentService eqService = DataStore.getInstance().getEquipmentService();
        ArrayList<String> deviceNames = eqService.getDeviceNames();
        JComboBox<String> cmbEquipment = new JComboBox<>(deviceNames.toArray(new String[0]));
        JTextField txtTask = new JTextField();
        JTextField txtDue = new JTextField("yyyy-MM-dd");

        form.add(new JLabel("Equipment:"));     form.add(cmbEquipment);
        form.add(new JLabel("Task:"));          form.add(txtTask);
        form.add(new JLabel("Due Date:"));      form.add(txtDue);
        form.add(new JLabel(""));               form.add(new JLabel(""));

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Schedule");
        save.setBackground(new Color(39, 174, 96));
        save.setForeground(Color.WHITE);
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            String task = txtTask.getText().trim();
            String due = txtDue.getText().trim();
            if (task.isEmpty() || due.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            MaintenanceTask mt = new MaintenanceTask(0,
                    cmbEquipment.getSelectedItem().toString(),
                    task, due, "Scheduled");
            service.add(mt);
            refreshTable();
            dialog.dispose();
        });
        cancel.addActionListener(e -> dialog.dispose());
        btnPanel.add(save);
        btnPanel.add(cancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void changeStatus(String newStatus) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a task first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        MaintenanceTask task = service.getById(id);
        if (task != null) {
            task.setStatus(newStatus);
            service.update(id, task);
            refreshTable();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a task to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this maintenance task?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.delete(id);
            refreshTable();
        }
    }
}
