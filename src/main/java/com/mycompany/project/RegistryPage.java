package com.mycompany.project;

import com.mycompany.project.backend.model.Equipment;
import com.mycompany.project.backend.service.DataService;
import com.mycompany.project.backend.util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RegistryPage extends javax.swing.JPanel {
    private DefaultTableModel model;
    private JTextField txtId, txtRoom, txtSpecs, txtWarranty;
    private JTable jTable1;
    private TableRowSorter<DefaultTableModel> sorter;

    public RegistryPage() {
        initComponents();
        setupTable();
        refreshTable();
    }

    private void setupTable() {
        String[] cols = {"Asset Tag", "Room", "Specs", "Warranty", "Status"};
        model = new DefaultTableModel(cols, 0);
        jTable1.setModel(model);
        sorter = new TableRowSorter<>(model);
        jTable1.setRowSorter(sorter);
    }

    public void refreshTable() {
        model.setRowCount(0);
        for (Equipment e : DataService.getInstance().getInventory()) {
            model.addRow(new Object[]{
                e.getId(),
                e.getRoom(),
                e.getSpecs(),
                e.getWarrantyStatus(),
                e.getStatus().getDisplayName()
            });
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Equipment"));
        
        txtId = new JTextField();
        txtRoom = new JTextField();
        txtSpecs = new JTextField();
        txtWarranty = new JTextField();
        
        inputPanel.add(new JLabel(" Asset Tag / ID:")); inputPanel.add(txtId);
        inputPanel.add(new JLabel(" Laboratory Room:")); inputPanel.add(txtRoom);
        inputPanel.add(new JLabel(" Specifications:")); inputPanel.add(txtSpecs);
        inputPanel.add(new JLabel(" Warranty Status:")); inputPanel.add(txtWarranty);
        
        JButton btnAdd = new JButton("Register Equipment");
        btnAdd.addActionListener(e -> registerEquipment());
        inputPanel.add(btnAdd);

        // Search Bar (reuses a single TableRowSorter instead of creating one per keystroke)
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField txtSearch = new JTextField();
        txtSearch.setBorder(BorderFactory.createTitledBorder("Search by Asset Tag / Room"));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = txtSearch.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        jTable1 = new JTable();
        JScrollPane scrollPane = new JScrollPane(jTable1);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void registerEquipment() {
        List<String> errors = Validator.validateEquipment(
            txtId.getText(), txtRoom.getText(), txtSpecs.getText(), txtWarranty.getText()
        );
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                String.join("\n", errors), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Equipment eq = new Equipment(
            txtId.getText().trim(), txtRoom.getText().trim(),
            txtSpecs.getText().trim(), LocalDate.now(), txtWarranty.getText().trim()
        );
        DataService.getInstance().addEquipment(eq);
        refreshTable();
        clearFields();
        JOptionPane.showMessageDialog(this, "Equipment Registered Successfully!");
    }

    private void clearFields() {
        txtId.setText("");
        txtRoom.setText("");
        txtSpecs.setText("");
        txtWarranty.setText("");
    }
}
