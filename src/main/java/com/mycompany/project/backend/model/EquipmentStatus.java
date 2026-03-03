package com.mycompany.project.backend.model;

/**
 * Enum representing possible equipment statuses.
 * Demonstrates ENCAPSULATION — restricts status values to a controlled, type-safe set
 * instead of using raw Strings scattered throughout the codebase.
 */
public enum EquipmentStatus {
    OPERATIONAL("Operational"),
    UNDER_MAINTENANCE("Under Maintenance"),
    FOR_REPLACEMENT("For Replacement");

    private final String displayName;

    EquipmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a display string back to the enum constant.
     * Returns OPERATIONAL as a safe default if no match is found.
     */
    public static EquipmentStatus fromDisplayName(String name) {
        for (EquipmentStatus s : values()) {
            if (s.displayName.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return OPERATIONAL;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
