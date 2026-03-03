package com.mycompany.project.backend.model;

import java.time.LocalDateTime;

/**
 * Abstract base class for all domain entities in the system.
 * Demonstrates ABSTRACTION — defines a common template that all entities must follow.
 * Demonstrates INHERITANCE — Equipment and IssueReport extend this class.
 */
public abstract class Entity {
    private String id;
    private LocalDateTime createdDate;

    protected Entity(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID cannot be null or empty.");
        }
        this.id = id.trim();
        this.createdDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Abstract method — each entity subclass must provide its own summary.
     * Demonstrates POLYMORPHISM — same method call, different behavior per subclass.
     */
    public abstract String getSummary();

    @Override
    public String toString() {
        return getSummary();
    }
}
