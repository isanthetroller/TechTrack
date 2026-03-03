package com.mycompany.project;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends javax.swing.JFrame {
    private JPanel cardPanel;
    private DashboardSummary dashboardSummary;
    private RegistryPage registryPage;
    private MaintenancePage maintenancePage;

    public MainDashboard() {
        setTitle("TechTrack - NCST Hardware Maintenance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Sidebar Navigation
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setPreferredSize(new Dimension(200, 700));

        JLabel title = new JLabel("TechTrack");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(40));

        String[] menuItems = {"Dashboard", "Registry", "Reports", "Maintenance"};
        for (String item : menuItems) {
            JButton btn = createMenuButton(item);
            btn.addActionListener(e -> switchCard(item.toLowerCase()));
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(10));
        }

        // Main Content Area
        cardPanel = new JPanel(new CardLayout());
        dashboardSummary = new DashboardSummary();
        registryPage = new RegistryPage();
        maintenancePage = new MaintenancePage();

        cardPanel.add(dashboardSummary, "dashboard");
        cardPanel.add(registryPage, "registry");
        cardPanel.add(new ReportsPage(), "reports");
        cardPanel.add(maintenancePage, "maintenance");

        add(sidebar, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 58, 64));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        return btn;
    }

    private void switchCard(String cardName) {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, cardName);
        if ("dashboard".equals(cardName)) {
            dashboardSummary.refreshStats();
        } else if ("registry".equals(cardName)) {
            registryPage.refreshTable();
        } else if ("maintenance".equals(cardName)) {
            maintenancePage.refreshTable();
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        EventQueue.invokeLater(() -> new MainDashboard().setVisible(true));
    }
}
