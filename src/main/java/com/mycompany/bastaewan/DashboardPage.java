package com.mycompany.bastaewan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.mycompany.bastaewan.model.Equipment;
import com.mycompany.bastaewan.model.RepairLog;
import com.mycompany.bastaewan.model.MaintenanceTask;
import com.mycompany.bastaewan.service.DataStore;
import com.mycompany.bastaewan.service.EquipmentService;
import com.mycompany.bastaewan.service.RepairLogService;
import com.mycompany.bastaewan.service.MaintenanceService;

public class DashboardPage extends JPanel {

    private JPanel cardsPanel;
    private JPanel contentPanel;
    private DefaultTableModel maintenanceTableModel;
    private DefaultTableModel activityTableModel;

    public DashboardPage() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Summary cards
        cardsPanel = new JPanel(new GridLayout(1, 5, 12, 12));
        cardsPanel.setBackground(new Color(245, 247, 250));
        buildCards();
        add(cardsPanel, BorderLayout.NORTH);

        // Maintenance Intelligence table
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Maintenance Intelligence");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(title, BorderLayout.NORTH);

        String[] cols = {"RANK", "DEVICE", "REPAIRS", "CATEGORY", "FAILURE RATE"};
        maintenanceTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(maintenanceTableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(36);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        contentPanel.add(sp, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Activity log
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        JLabel actTitle = new JLabel("Activity Log");
        actTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        activityPanel.add(actTitle, BorderLayout.NORTH);

        String[] actCols = {"DATE", "TIME", "ACTION"};
        activityTableModel = new DefaultTableModel(actCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable actTable = new JTable(activityTableModel);
        actTable.setFillsViewportHeight(true);
        actTable.setRowHeight(28);
        JScrollPane actScroll = new JScrollPane(actTable);
        actScroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        activityPanel.add(actScroll, BorderLayout.CENTER);

        add(activityPanel, BorderLayout.SOUTH);

        refreshData();
    }

    // Refresh all dashboard data from services
    public void refreshData() {
        DataStore store = DataStore.getInstance();
        EquipmentService eqService = store.getEquipmentService();
        RepairLogService repairService = store.getRepairLogService();

        // Update cards
        int total = eqService.getAll().size();
        int online = eqService.countByStatus("Online");
        int needsAttention = eqService.countByStatus("Needs Attention");
        int openRepairs = repairService.countOpenTickets();
        int avgHealth = eqService.getAverageHealthScore();

        cardsPanel.removeAll();
        cardsPanel.add(createCard("TOTAL DEVICES", String.valueOf(total), new Color(0, 122, 204)));
        cardsPanel.add(createCard("ONLINE & HEALTHY", String.valueOf(online), new Color(0, 153, 76)));
        cardsPanel.add(createCard("NEEDS ATTENTION", String.valueOf(needsAttention), new Color(255, 204, 0)));
        cardsPanel.add(createCard("OPEN REPAIR TICKETS", String.valueOf(openRepairs), new Color(204, 0, 0)));
        cardsPanel.add(createCard("AVG HEALTH SCORE", avgHealth + "%", new Color(255, 153, 0)));
        cardsPanel.revalidate();
        cardsPanel.repaint();

        // Maintenance intelligence - rank devices by repair count
        maintenanceTableModel.setRowCount(0);
        HashMap<String, Integer> repairCounts = new HashMap<>();
        for (RepairLog r : repairService.getAll()) {
            repairCounts.put(r.getEquipmentName(),
                    repairCounts.getOrDefault(r.getEquipmentName(), 0) + 1);
        }

        // Sort by repair count descending
        ArrayList<String> devices = new ArrayList<>(repairCounts.keySet());
        devices.sort((a, b) -> repairCounts.get(b) - repairCounts.get(a));

        int rank = 1;
        for (String device : devices) {
            Equipment eq = null;
            for (Equipment e : eqService.getAll()) {
                if (e.getDeviceName().equals(device)) {
                    eq = e;
                    break;
                }
            }
            String category = eq != null ? eq.getCategory() : "Unknown";
            int repairs = repairCounts.get(device);
            int failRate = Math.min(100, repairs * 15 + 20);
            maintenanceTableModel.addRow(new Object[]{rank++, device, repairs, category, failRate + "%"});
        }

        // Activity log
        activityTableModel.setRowCount(0);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        activityTableModel.addRow(new Object[]{today, time, "Dashboard refreshed"});
        activityTableModel.addRow(new Object[]{today, time, "Logged in as " + SessionManager.getUsername()});
    }

    private void buildCards() {
        cardsPanel.add(createCard("TOTAL DEVICES", "...", new Color(0, 122, 204)));
        cardsPanel.add(createCard("ONLINE & HEALTHY", "...", new Color(0, 153, 76)));
        cardsPanel.add(createCard("NEEDS ATTENTION", "...", new Color(255, 204, 0)));
        cardsPanel.add(createCard("OPEN REPAIR TICKETS", "...", new Color(204, 0, 0)));
        cardsPanel.add(createCard("AVG HEALTH SCORE", "...", new Color(255, 153, 0)));
    }

    private JPanel createCard(String title, String value, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JPanel strip = new JPanel();
        strip.setBackground(accent);
        strip.setPreferredSize(new Dimension(0, 4));
        p.add(strip, BorderLayout.NORTH);

        JLabel lValue = new JLabel(value, SwingConstants.CENTER);
        lValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lValue.setForeground(accent.darker());
        p.add(lValue, BorderLayout.CENTER);

        JLabel lTitle = new JLabel(title, SwingConstants.CENTER);
        lTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lTitle.setForeground(accent.darker().darker());
        p.add(lTitle, BorderLayout.SOUTH);

        return p;
    }
}
