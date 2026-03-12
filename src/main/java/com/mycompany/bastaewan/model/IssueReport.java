package com.mycompany.bastaewan.model;

// Inheritance: extends BaseEntity
public class IssueReport extends BaseEntity {
    private String equipmentName;
    private String description;
    private String priority;
    private String status;
    private String reportedBy;

    public IssueReport() {
    }

    public IssueReport(int id, String equipmentName, String description,
                       String priority, String status, String reportedBy) {
        super(id);
        this.equipmentName = equipmentName;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.reportedBy = reportedBy;
    }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    @Override
    public String[] toTableRow() {
        return new String[]{
            String.valueOf(getId()),
            equipmentName,
            description,
            priority,
            status,
            reportedBy,
            getCreatedDate()
        };
    }

    @Override
    public String getDisplayName() {
        return "Issue #" + getId() + " - " + equipmentName;
    }
}
