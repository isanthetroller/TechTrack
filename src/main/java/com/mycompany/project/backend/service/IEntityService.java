package com.mycompany.project.backend.service;

import com.mycompany.project.backend.model.Entity;
import java.util.List;

/**
 * Generic service interface defining CRUD operations for any entity type.
 * Demonstrates ABSTRACTION — defines a contract that all services must fulfill.
 * Demonstrates POLYMORPHISM — EquipmentService and ReportService are interchangeable
 * through this common interface when working with their respective entity types.
 *
 * @param <T> The entity type this service manages (must extend Entity).
 */
public interface IEntityService<T extends Entity> {

    /**
     * Retrieve all entities managed by this service.
     */
    List<T> getAll();

    /**
     * Add a new entity.
     */
    void add(T entity);

    /**
     * Remove an entity by its unique ID.
     * @return true if the entity was found and removed.
     */
    boolean removeById(String id);

    /**
     * Find an entity by its unique ID.
     * @return the entity, or null if not found.
     */
    T findById(String id);

    /**
     * Get the total count of entities.
     */
    int getCount();
}
