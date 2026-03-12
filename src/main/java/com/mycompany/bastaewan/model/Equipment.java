package com.mycompany.bastaewan.model;

// Inheritance: extends BaseEntity
public class Equipment extends BaseEntity {
    // Encapsulation: private fields
    private String deviceName;
    private String type;
    private String status;
    private int healthScore;
    private String location;
    private String category;

    public Equipment() {
    }

    public Equipment(int id, String deviceName, String type, String status,
                     int healthScore, String location, String category) {
        super(id);
        this.deviceName = deviceName;
        this.type = type;
        this.status = status;
        this.healthScore = healthScore;
        this.location = location;
        this.category = category;
    }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getHealthScore() { return healthScore; }
    public void setHealthScore(int healthScore) { this.healthScore = healthScore; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    // Polymorphism: overrides abstract method from BaseEntity
    @Override
    public String[] toTableRow() {
        return new String[]{
            String.valueOf(getId()),
            deviceName,
            type,
            status,
            healthScore + "%",
            location,
            category
        };
    }

    @Override
    public String getDisplayName() {
        return deviceName;
    }
}
