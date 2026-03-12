package com.mycompany.bastaewan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.mycompany.bastaewan.model.RepairLog;
import com.mycompany.bastaewan.service.DataStore;
import com.mycompany.bastaewan.service.RepairLogService;
import com.mycompany.bastaewan.service.EquipmentService;

public class RepairLogsPage extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private RepairLogService service;
    private final String[] columns = {"ID", "Equipment", "Description", "Technician", "Status", "Cost", "Completed"};

    public RepairLogsPage() {
        service = DataStore.getInstance().getRepairLogService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Title
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(245, 247, 250));
        JLabel title = new JLabel("Repair Logs");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleBar.add(title, BorderLayout.WEST);

        // Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        String[] filterOptions = {"All", "Pending", "In Progress", "Completed"};
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

        JButton btnAdd = new JButton("Add Repair");
        btnAdd.setBackground(new Color(39, 174, 96));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> showAddDialog());

        JButton btnComplete = new JButton("Mark Complete");
        btnComplete.setBackground(new Color(0, 102, 204));
        btnComplete.setForeground(Color.WHITE);
        btnComplete.setFocusPainted(false);
        btnComplete.addActionListener(e -> markComplete());

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelected());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnComplete);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (RepairLog r : service.getAll()) {
            tableModel.addRow(r.toTableRow());
        }
    }

    private void filterByStatus(String status) {
        tableModel.setRowCount(0);
        for (RepairLog r : service.getAll()) {
            if ("All".equals(status) || r.getStatus().equals(status)) {
                tableModel.addRow(r.toTableRow());
            }
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Repair Log", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        EquipmentService eqService = DataStore.getInstance().getEquipmentService();
        ArrayList<String> deviceNames = eqService.getDeviceNames();
        JComboBox<String> cmbEquipment = new JComboBox<>(deviceNames.toArray(new String[0]));
        JTextField txtDesc = new JTextField();
        JTextField txtTech = new JTextField();
        JTextField txtCost = new JTextField("0.00");

        form.add(new JLabel("Equipment:"));     form.add(cmbEquipment);
        form.add(new JLabel("Description:"));   form.add(txtDesc);
        form.add(new JLabel("Technician:"));    form.add(txtTech);
        form.add(new JLabel("Cost ($):"));      form.add(txtCost);
        form.add(new JLabel(""));               form.add(new JLabel(""));

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        save.setBackground(new Color(39, 174, 96));
        save.setForeground(Color.WHITE);
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            String desc = txtDesc.getText().trim();
            String tech = txtTech.getText().trim();
            if (desc.isEmpty() || tech.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double cost;
            try {
                cost = Double.parseDouble(txtCost.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Cost must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            RepairLog log = new RepairLog(0,
                    cmbEquipment.getSelectedItem().toString(),
                    desc, tech, "Pending", cost, null);
            service.add(log);
            refreshTable();
            dialog.dispose();
        });
        cancel.addActionListener(e -> dialog.dispose());
        btnPanel.add(save);
        btnPanel.add(cancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void markComplete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a repair log first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        RepairLog log = service.getById(id);
        if (log != null) {
            log.setStatus("Completed");
            log.setDateCompleted(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            service.update(id, log);
            refreshTable();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a repair log to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this repair log?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.delete(id);
            refreshTable();
        }
    }
}
