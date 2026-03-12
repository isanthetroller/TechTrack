package com.mycompany.bastaewan.model;

import java.text.SimpleDateFormat;
import java.util.Date;

// Abstraction: abstract class defines common structure for all entities
public abstract class BaseEntity {
    // Encapsulation: private fields with public getters/setters
    private int id;
    private String createdDate;

    public BaseEntity() {
        this.createdDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public BaseEntity(int id) {
        this();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    // Abstraction: subclasses must implement these
    public abstract String[] toTableRow();

    public abstract String getDisplayName();
}
