package com.mycompany.bastaewan.service;

import com.mycompany.bastaewan.model.Equipment;
import java.util.ArrayList;

// Polymorphism: implements Manageable interface for Equipment type
public class EquipmentService implements Manageable<Equipment> {
    private ArrayList<Equipment> equipmentList;
    private int nextId;

    public EquipmentService() {
        equipmentList = new ArrayList<>();
        nextId = 1;
        loadSampleData();
    }

    private void loadSampleData() {
        add(new Equipment(0, "WS-ACC-001", "Workstation", "Online", 95, "Office A", "Hardware"));
        add(new Equipment(0, "WS-ACC-002", "Workstation", "Online", 88, "Office A", "Hardware"));
        add(new Equipment(0, "WS-ACC-003", "Workstation", "Offline", 45, "Office B", "Hardware"));
        add(new Equipment(0, "SRV-PROD-01", "Server", "Online", 92, "Server Room", "Hardware"));
        add(new Equipment(0, "SRV-PROD-02", "Server", "Needs Attention", 67, "Server Room", "Hardware"));
        add(new Equipment(0, "WS-ACC-007", "Workstation", "Needs Attention", 32, "Office C", "Hardware"));
        add(new Equipment(0, "WS-ACC-010", "Workstation", "Online", 78, "Office B", "Hardware"));
        add(new Equipment(0, "NET-SW-001", "Network Switch", "Online", 99, "Server Room", "Network"));
        add(new Equipment(0, "NET-RT-001", "Router", "Online", 94, "Server Room", "Network"));
        add(new Equipment(0, "PRT-001", "Printer", "Needs Attention", 55, "Office A", "Hardware"));
        add(new Equipment(0, "SRV-BACKUP-01", "Server", "Online", 91, "Server Room", "Hardware"));
        add(new Equipment(0, "WS-ACC-012", "Workstation", "Online", 85, "Office C", "Hardware"));
    }

    @Override
    public void add(Equipment item) {
        item.setId(nextId++);
        equipmentList.add(item);
    }

    @Override
    public void update(int id, Equipment item) {
        for (int i = 0; i < equipmentList.size(); i++) {
            if (equipmentList.get(i).getId() == id) {
                item.setId(id);
                equipmentList.set(i, item);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < equipmentList.size(); i++) {
            if (equipmentList.get(i).getId() == id) {
                equipmentList.remove(i);
                return;
            }
        }
    }

    @Override
    public Equipment getById(int id) {
        for (Equipment e : equipmentList) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Equipment> getAll() {
        return equipmentList;
    }

    @Override
    public int getNextId() {
        return nextId;
    }

    // Domain-specific methods
    public int countByStatus(String status) {
        int count = 0;
        for (Equipment e : equipmentList) {
            if (e.getStatus().equals(status)) {
                count++;
            }
        }
        return count;
    }

    public int getAverageHealthScore() {
        if (equipmentList.isEmpty()) return 0;
        int total = 0;
        for (Equipment e : equipmentList) {
            total += e.getHealthScore();
        }
        return total / equipmentList.size();
    }

    public ArrayList<String> getDeviceNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Equipment e : equipmentList) {
            names.add(e.getDeviceName());
        }
        return names;
    }
}
