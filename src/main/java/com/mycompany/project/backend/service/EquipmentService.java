package com.mycompany.project.backend.service;

import com.mycompany.project.backend.model.Equipment;
import com.mycompany.project.backend.model.EquipmentStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Service that manages all equipment data and business logic.
 * Demonstrates ENCAPSULATION — the equipment list is private and
 * can only be accessed or modified through controlled methods.
 */
public class EquipmentService {
    private List<Equipment> inventory = new ArrayList<>();

    // Returns the full list of equipment
    public List<Equipment> getAll() {
        return inventory;
    }

    // Adds a new equipment to the inventory
    public void add(Equipment equipment) {
        if (equipment != null) {
            inventory.add(equipment);
        }
    }

    // Removes equipment by its ID
    public boolean removeById(String id) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getId().equals(id)) {
                inventory.remove(i);
                return true;
            }
        }
        return false;
    }

    // Finds equipment by its ID
    public Equipment findById(String id) {
        for (int i = 0; i < inventory.size(); i++) {
            Equipment e = inventory.get(i);
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    // Returns total number of equipment
    public int getCount() {
        return inventory.size();
    }

    // Counts how many equipment items have a specific status
    public int countByStatus(EquipmentStatus status) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    // Updates the status of an equipment item by its ID
    public boolean updateStatus(String id, EquipmentStatus newStatus) {
        Equipment eq = findById(id);
        if (eq != null) {
            eq.setStatus(newStatus);
            return true;
        }
        return false;
    }
}
