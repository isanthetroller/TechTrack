package com.mycompany.project.backend.model;

import java.time.LocalDate;

/**
 * Represents a piece of hardware equipment registered in the system.
 * Demonstrates INHERITANCE (extends Entity) and ENCAPSULATION (validated fields, controlled access).
 */
public class Equipment extends Entity {
    private String room;
    private String specs;
    private LocalDate purchaseDate;
    private String warrantyStatus;
    private EquipmentStatus status;

    public Equipment(String id, String room, String specs, LocalDate purchaseDate, String warrantyStatus) {
        super(id); // Inherited ID validation from Entity
        setRoom(room);
        setSpecs(specs);
        this.purchaseDate = purchaseDate;
        this.warrantyStatus = warrantyStatus;
        this.status = EquipmentStatus.OPERATIONAL;
    }

    // --- Encapsulated Getters and Setters with validation ---

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        if (room == null || room.trim().isEmpty()) {
            throw new IllegalArgumentException("Room cannot be empty.");
        }
        this.room = room.trim();
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        if (specs == null || specs.trim().isEmpty()) {
            throw new IllegalArgumentException("Specifications cannot be empty.");
        }
        this.specs = specs.trim();
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    /**
     * Polymorphic summary — Equipment provides its own implementation.
     */
    @Override
    public String getSummary() {
        return "[" + getId() + "] Room: " + room + " | " + specs + " | " + status.getDisplayName();
    }
}
