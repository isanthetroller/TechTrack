package com.mycompany.bastaewan.model;

// Inheritance: extends BaseEntity
public class MaintenanceTask extends BaseEntity {
    private String equipmentName;
    private String task;
    private String dueDate;
    private String status;

    public MaintenanceTask() {
    }

    public MaintenanceTask(int id, String equipmentName, String task,
                           String dueDate, String status) {
        super(id);
        this.equipmentName = equipmentName;
        this.task = task;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String[] toTableRow() {
        return new String[]{
            String.valueOf(getId()),
            equipmentName,
            task,
            dueDate,
            status
        };
    }

    @Override
    public String getDisplayName() {
        return task + " - " + equipmentName;
    }
}
