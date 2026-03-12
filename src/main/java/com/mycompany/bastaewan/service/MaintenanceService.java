package com.mycompany.bastaewan.service;

import com.mycompany.bastaewan.model.MaintenanceTask;
import java.util.ArrayList;

public class MaintenanceService implements Manageable<MaintenanceTask> {
    private ArrayList<MaintenanceTask> taskList;
    private int nextId;

    public MaintenanceService() {
        taskList = new ArrayList<>();
        nextId = 1;
        loadSampleData();
    }

    private void loadSampleData() {
        add(new MaintenanceTask(0, "WS-ACC-007", "HDD Replace", "2026-01-15", "Overdue"));
        add(new MaintenanceTask(0, "SRV-PROD-02", "Thermal Paste Reapply", "2026-03-20", "Scheduled"));
        add(new MaintenanceTask(0, "NET-SW-001", "Firmware Update", "2026-03-25", "Scheduled"));
        add(new MaintenanceTask(0, "SRV-BACKUP-01", "Backup Verification", "2026-03-15", "In Progress"));
        add(new MaintenanceTask(0, "WS-ACC-012", "Antivirus Update", "2026-03-10", "Completed"));
    }

    @Override
    public void add(MaintenanceTask item) {
        item.setId(nextId++);
        taskList.add(item);
    }

    @Override
    public void update(int id, MaintenanceTask item) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == id) {
                item.setId(id);
                taskList.set(i, item);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == id) {
                taskList.remove(i);
                return;
            }
        }
    }

    @Override
    public MaintenanceTask getById(int id) {
        for (MaintenanceTask t : taskList) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    @Override
    public ArrayList<MaintenanceTask> getAll() {
        return taskList;
    }

    @Override
    public int getNextId() {
        return nextId;
    }

    public int countByStatus(String status) {
        int count = 0;
        for (MaintenanceTask t : taskList) {
            if (t.getStatus().equals(status)) {
                count++;
            }
        }
        return count;
    }
}
