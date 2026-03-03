package com.mycompany.project.backend.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for input validation.
 * Demonstrates ENCAPSULATION — centralizes all validation logic in one place
 * and prevents instantiation (private constructor).
 */
public class Validator {

    private Validator() {
        // Prevent instantiation — utility class
    }

    /**
     * Check if a string is null or contains only whitespace.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validate equipment registration input fields.
     * @return list of error messages (empty list if all fields are valid).
     */
    public static List<String> validateEquipment(String id, String room, String specs, String warranty) {
        List<String> errors = new ArrayList<>();
        if (isNullOrEmpty(id))       errors.add("Asset Tag / ID is required.");
        if (isNullOrEmpty(room))     errors.add("Laboratory Room is required.");
        if (isNullOrEmpty(specs))    errors.add("Specifications are required.");
        if (isNullOrEmpty(warranty)) errors.add("Warranty Status is required.");
        return errors;
    }

    /**
     * Validate issue report input fields.
     * @return list of error messages (empty list if all fields are valid).
     */
    public static List<String> validateReport(String equipmentId, String reportedBy, String description) {
        List<String> errors = new ArrayList<>();
        if (isNullOrEmpty(equipmentId))  errors.add("Equipment Asset Tag is required.");
        if (isNullOrEmpty(reportedBy))   errors.add("Reporter name is required.");
        if (isNullOrEmpty(description))  errors.add("Issue description is required.");
        return errors;
    }
}
