package com.mycompany.project.backend.model;

/**
 * Enum representing possible issue report statuses.
 * Demonstrates ENCAPSULATION — type-safe status values replace magic strings.
 */
public enum ReportStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    private final String displayName;

    ReportStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ReportStatus fromDisplayName(String name) {
        for (ReportStatus s : values()) {
            if (s.displayName.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return PENDING;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
