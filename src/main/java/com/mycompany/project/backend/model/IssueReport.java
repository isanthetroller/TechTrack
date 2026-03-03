package com.mycompany.project.backend.model;

import java.time.LocalDateTime;

/**
 * Represents a hardware issue report filed by a user.
 * Demonstrates INHERITANCE (extends Entity) and ENCAPSULATION (validated fields).
 */
public class IssueReport extends Entity {
    private static int counter = 0;

    private String equipmentId;
    private String reportedBy;
    private String description;
    private LocalDateTime reportDate;
    private ReportStatus status;

    public IssueReport(String equipmentId, String reportedBy, String description) {
        super("RPT-" + (++counter));
        setEquipmentId(equipmentId);
        setReportedBy(reportedBy);
        setDescription(description);
        this.reportDate = LocalDateTime.now();
        this.status = ReportStatus.PENDING;
    }

    // --- Encapsulated Getters and Setters ---

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        if (equipmentId == null || equipmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment ID cannot be empty.");
        }
        this.equipmentId = equipmentId.trim();
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        if (reportedBy == null || reportedBy.trim().isEmpty()) {
            throw new IllegalArgumentException("Reporter name cannot be empty.");
        }
        this.reportedBy = reportedBy.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        this.description = description.trim();
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    /**
     * Polymorphic summary — IssueReport provides its own implementation.
     */
    @Override
    public String getSummary() {
        return "Report " + getId() + ": Equipment [" + equipmentId + "] - " + description + " (" + status.getDisplayName() + ")";
    }
}
