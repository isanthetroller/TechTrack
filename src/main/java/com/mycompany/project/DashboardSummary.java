package com.mycompany.project;

import com.mycompany.project.backend.model.EquipmentStatus;
import com.mycompany.project.backend.model.ReportStatus;
import com.mycompany.project.backend.service.DataService;

import javax.swing.*;
import java.awt.*;

public class DashboardSummary extends javax.swing.JPanel {
    private JLabel lblTotal, lblOperational, lblMaintenance, lblReports;

    public DashboardSummary() {
        initComponents();
        refreshStats();
    }

    private void initComponents() {
        setLayout(new GridLayout(2, 2, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        lblTotal = createStatLabel("Total Equipment");
        lblOperational = createStatLabel("Operational");
        lblMaintenance = createStatLabel("Under Maintenance");
        lblReports = createStatLabel("Pending Reports");

        add(lblTotal);
        add(lblOperational);
        add(lblMaintenance);
        add(lblReports);
    }

    private JLabel createStatLabel(String title) {
        JLabel label = new JLabel(title + ": 0", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        label.setBackground(new Color(240, 240, 240));
        label.setOpaque(true);
        return label;
    }

    public void refreshStats() {
        DataService ds = DataService.getInstance();
        int total = ds.getEquipmentService().getCount();
        long operational = ds.getEquipmentService().countByStatus(EquipmentStatus.OPERATIONAL);
        long maintenance = ds.getEquipmentService().countByStatus(EquipmentStatus.UNDER_MAINTENANCE);
        long reports = ds.getReportService().countByStatus(ReportStatus.PENDING);

        lblTotal.setText("Total Equipment: " + total);
        lblOperational.setText("Operational: " + operational);
        lblMaintenance.setText("Maintenance: " + maintenance);
        lblReports.setText("Pending Reports: " + reports);
    }
}
