package com.mycompany.bastaewan.service;

import com.mycompany.bastaewan.model.RepairLog;
import java.util.ArrayList;

public class RepairLogService implements Manageable<RepairLog> {
    private ArrayList<RepairLog> repairList;
    private int nextId;

    public RepairLogService() {
        repairList = new ArrayList<>();
        nextId = 1;
        loadSampleData();
    }

    private void loadSampleData() {
        add(new RepairLog(0, "WS-ACC-007", "HDD Replacement", "John", "Completed", 150.00, "2026-02-15"));
        add(new RepairLog(0, "SRV-PROD-02", "RAM Upgrade", "Mike", "Completed", 300.00, "2026-02-20"));
        add(new RepairLog(0, "WS-ACC-003", "Motherboard Repair", "John", "In Progress", 250.00, null));
        add(new RepairLog(0, "WS-ACC-007", "Fan Replacement", "Sarah", "Pending", 50.00, null));
        add(new RepairLog(0, "PRT-001", "Paper Jam Fix", "Mike", "Pending", 30.00, null));
        add(new RepairLog(0, "WS-ACC-010", "SSD Upgrade", "Sarah", "Completed", 120.00, "2026-03-01"));
    }

    @Override
    public void add(RepairLog item) {
        item.setId(nextId++);
        repairList.add(item);
    }

    @Override
    public void update(int id, RepairLog item) {
        for (int i = 0; i < repairList.size(); i++) {
            if (repairList.get(i).getId() == id) {
                item.setId(id);
                repairList.set(i, item);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < repairList.size(); i++) {
            if (repairList.get(i).getId() == id) {
                repairList.remove(i);
                return;
            }
        }
    }

    @Override
    public RepairLog getById(int id) {
        for (RepairLog r : repairList) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    @Override
    public ArrayList<RepairLog> getAll() {
        return repairList;
    }

    @Override
    public int getNextId() {
        return nextId;
    }

    public int countOpenTickets() {
        int count = 0;
        for (RepairLog r : repairList) {
            if (!r.getStatus().equals("Completed")) {
                count++;
            }
        }
        return count;
    }
}
