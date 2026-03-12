package com.mycompany.bastaewan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import com.mycompany.bastaewan.model.IssueReport;
import com.mycompany.bastaewan.service.DataStore;
import com.mycompany.bastaewan.service.IssueReportService;
import com.mycompany.bastaewan.service.EquipmentService;

public class IssueReportsPage extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private IssueReportService service;
    private final String[] columns = {"ID", "Equipment", "Description", "Priority", "Status", "Reported By", "Date"};

    public IssueReportsPage() {
        service = DataStore.getInstance().getIssueReportService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Title
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(245, 247, 250));
        JLabel title = new JLabel("Issue Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleBar.add(title, BorderLayout.WEST);

        // Filter by status
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        String[] filterOptions = {"All", "Open", "In Progress", "Resolved", "Closed"};
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

        JButton btnAdd = new JButton("Report Issue");
        btnAdd.setBackground(new Color(39, 174, 96));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> showAddDialog());

        JButton btnResolve = new JButton("Resolve");
        btnResolve.setBackground(new Color(0, 102, 204));
        btnResolve.setForeground(Color.WHITE);
        btnResolve.setFocusPainted(false);
        btnResolve.addActionListener(e -> changeStatus("Resolved"));

        JButton btnClose = new JButton("Close Issue");
        btnClose.setBackground(new Color(255, 153, 0));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> changeStatus("Closed"));

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelected());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnResolve);
        buttonPanel.add(btnClose);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (IssueReport r : service.getAll()) {
            tableModel.addRow(r.toTableRow());
        }
    }

    private void filterByStatus(String status) {
        tableModel.setRowCount(0);
        for (IssueReport r : service.getAll()) {
            if ("All".equals(status) || r.getStatus().equals(status)) {
                tableModel.addRow(r.toTableRow());
            }
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Report Issue", true);
        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        EquipmentService eqService = DataStore.getInstance().getEquipmentService();
        ArrayList<String> deviceNames = eqService.getDeviceNames();
        JComboBox<String> cmbEquipment = new JComboBox<>(deviceNames.toArray(new String[0]));
        JTextField txtDesc = new JTextField();
        String[] priorities = {"Low", "Medium", "High", "Critical"};
        JComboBox<String> cmbPriority = new JComboBox<>(priorities);
        JTextField txtReportedBy = new JTextField(SessionManager.getUsername());
        txtReportedBy.setEditable(false);

        form.add(new JLabel("Equipment:"));     form.add(cmbEquipment);
        form.add(new JLabel("Description:"));   form.add(txtDesc);
        form.add(new JLabel("Priority:"));      form.add(cmbPriority);
        form.add(new JLabel("Reported By:"));   form.add(txtReportedBy);
        form.add(new JLabel(""));               form.add(new JLabel(""));

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Submit");
        save.setBackground(new Color(39, 174, 96));
        save.setForeground(Color.WHITE);
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            String desc = txtDesc.getText().trim();
            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Description is required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            IssueReport report = new IssueReport(0,
                    cmbEquipment.getSelectedItem().toString(),
                    desc,
                    cmbPriority.getSelectedItem().toString(),
                    "Open",
                    txtReportedBy.getText());
            service.add(report);
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
            JOptionPane.showMessageDialog(this, "Select an issue first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        IssueReport report = service.getById(id);
        if (report != null) {
            report.setStatus(newStatus);
            service.update(id, report);
            refreshTable();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an issue to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this issue report?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.delete(id);
            refreshTable();
        }
    }
}
