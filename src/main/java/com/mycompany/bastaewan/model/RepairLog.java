package com.mycompany.bastaewan.model;

// Inheritance: extends BaseEntity
public class RepairLog extends BaseEntity {
    private String equipmentName;
    private String description;
    private String technician;
    private String status;
    private double cost;
    private String dateCompleted;

    public RepairLog() {
    }

    public RepairLog(int id, String equipmentName, String description, String technician,
                     String status, double cost, String dateCompleted) {
        super(id);
        this.equipmentName = equipmentName;
        this.description = description;
        this.technician = technician;
        this.status = status;
        this.cost = cost;
        this.dateCompleted = dateCompleted;
    }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTechnician() { return technician; }
    public void setTechnician(String technician) { this.technician = technician; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public String getDateCompleted() { return dateCompleted; }
    public void setDateCompleted(String dateCompleted) { this.dateCompleted = dateCompleted; }

    @Override
    public String[] toTableRow() {
        return new String[]{
            String.valueOf(getId()),
            equipmentName,
            description,
            technician,
            status,
            String.format("$%.2f", cost),
            dateCompleted != null ? dateCompleted : "N/A"
        };
    }

    @Override
    public String getDisplayName() {
        return "Repair #" + getId() + " - " + equipmentName;
    }
}
