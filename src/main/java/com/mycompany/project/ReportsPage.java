package com.mycompany.project;

import com.mycompany.project.backend.model.IssueReport;
import com.mycompany.project.backend.service.DataService;
import com.mycompany.project.backend.util.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReportsPage extends javax.swing.JPanel {
    private JTextField txtEqId, txtReportedBy;
    private JTextArea txtDescription;

    public ReportsPage() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 1));
        formPanel.setBorder(BorderFactory.createTitledBorder("Report Hardware Issue"));
        
        txtEqId = new JTextField();
        txtReportedBy = new JTextField();
        txtDescription = new JTextArea(5, 20);
        
        formPanel.add(new JLabel(" Equipment Asset Tag:"));
        formPanel.add(txtEqId);
        formPanel.add(new JLabel(" Reported By (Name):"));
        formPanel.add(txtReportedBy);
        
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JLabel(" Issue Description:"), BorderLayout.NORTH);
        descPanel.add(new JScrollPane(txtDescription), BorderLayout.CENTER);
        
        JButton btnSubmit = new JButton("Submit Issue Report");
        btnSubmit.addActionListener(e -> submitReport());

        add(formPanel, BorderLayout.NORTH);
        add(descPanel, BorderLayout.CENTER);
        add(btnSubmit, BorderLayout.SOUTH);
    }

    private void submitReport() {
        List<String> errors = Validator.validateReport(
            txtEqId.getText(), txtReportedBy.getText(), txtDescription.getText()
        );
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                String.join("\n", errors), "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        IssueReport report = new IssueReport(
            txtEqId.getText().trim(), txtReportedBy.getText().trim(), txtDescription.getText().trim()
        );
        DataService.getInstance().addReport(report);
        clearFields();
        JOptionPane.showMessageDialog(this, "Issue Reported. Technician will be assigned.");
    }

    private void clearFields() {
        txtEqId.setText("");
        txtReportedBy.setText("");
        txtDescription.setText("");
    }
}
