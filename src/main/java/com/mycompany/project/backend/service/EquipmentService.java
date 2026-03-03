package com.mycompany.project.backend.service;

import com.mycompany.project.backend.model.Equipment;
import com.mycompany.project.backend.model.EquipmentStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Service handling all equipment-related business logic and data operations.
 * Demonstrates POLYMORPHISM (implements IEntityService&lt;Equipment&gt;)
 * and ENCAPSULATION (private list, controlled access).
 */
public class EquipmentService implements IEntityService<Equipment> {
    private final List<Equipment> inventory = new ArrayList<>();

    @Override
    public List<Equipment> getAll() {
        return new ArrayList<>(inventory); // Defensive copy — protects internal state
    }

    @Override
    public void add(Equipment equipment) {
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment cannot be null.");
        }
        inventory.add(equipment);
    }

    @Override
    public boolean removeById(String id) {
        return inventory.removeIf(e -> e.getId().equals(id));
    }

    @Override
    public Equipment findById(String id) {
        for (Equipment e : inventory) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return inventory.size();
    }

    /**
     * Count equipment filtered by a specific status.
     */
    public long countByStatus(EquipmentStatus status) {
        return inventory.stream()
                .filter(e -> e.getStatus() == status)
                .count();
    }

    /**
     * Update the status of an equipment item by its ID.
     * @return true if the equipment was found and updated.
     */
    public boolean updateStatus(String id, EquipmentStatus newStatus) {
        Equipment eq = findById(id);
        if (eq != null) {
            eq.setStatus(newStatus);
            return true;
        }
        return false;
    }
}
