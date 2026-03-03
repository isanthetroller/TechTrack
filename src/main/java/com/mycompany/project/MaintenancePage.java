package com.mycompany.project;

import com.mycompany.project.backend.model.IssueReport;
import com.mycompany.project.backend.service.DataService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MaintenancePage extends javax.swing.JPanel {
    private DefaultTableModel model;
    private JTable table;

    public MaintenancePage() {
        initComponents();
        setupTable();
        refreshTable();
    }

    private void setupTable() {
        String[] cols = {"Equip ID", "Reported By", "Issue", "Status"};
        model = new DefaultTableModel(cols, 0);
        table.setModel(model);
    }

    public void refreshTable() {
        model.setRowCount(0);
        for (IssueReport r : DataService.getInstance().getReports()) {
            model.addRow(new Object[]{
                r.getEquipmentId(),
                r.getReportedBy(),
                r.getDescription(),
                r.getStatus().getDisplayName()
            });
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Technician Repair Log"));

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        
        JButton btnResolve = new JButton("Mark Selected as Resolved");
        btnResolve.setBackground(new Color(40, 167, 69));
        btnResolve.setForeground(Color.WHITE);
        btnResolve.addActionListener(e -> resolveSelected());

        add(scrollPane, BorderLayout.CENTER);
        add(btnResolve, BorderLayout.SOUTH);
    }

    private void resolveSelected() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String equipId = (String) model.getValueAt(row, 0);
            boolean resolved = DataService.getInstance().getReportService().resolveByEquipmentId(equipId);
            if (resolved) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Issue marked as Resolved.");
            } else {
                JOptionPane.showMessageDialog(this,
                    "No pending report found for this equipment.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a report to resolve.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
}
