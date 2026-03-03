package com.mycompany.project.backend.service;

import com.mycompany.project.backend.model.Equipment;
import com.mycompany.project.backend.model.IssueReport;

import java.time.LocalDate;
import java.util.List;

/**
 * Singleton facade that provides a single access point to all backend services.
 * Demonstrates ENCAPSULATION (singleton pattern with private constructor)
 * and coordinates EquipmentService and ReportService.
 */
public class DataService {
    private static DataService instance;

    private EquipmentService equipmentService;
    private ReportService reportService;

    private DataService() {
        equipmentService = new EquipmentService();
        reportService = new ReportService();
        loadSampleData();
    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    // --- Service Accessors ---

    public EquipmentService getEquipmentService() {
        return equipmentService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    // --- Convenience Methods (delegate to services for simpler UI calls) ---

    public List<Equipment> getInventory() {
        return equipmentService.getAll();
    }

    public void addEquipment(Equipment e) {
        equipmentService.add(e);
    }

    public List<IssueReport> getReports() {
        return reportService.getAll();
    }

    public void addReport(IssueReport r) {
        reportService.add(r);
    }

    // --- Sample Data ---

    private void loadSampleData() {
        equipmentService.add(new Equipment("PC-001", "Lab 101", "i5, 16GB RAM", LocalDate.now(), "Active"));
        equipmentService.add(new Equipment("PC-002", "Lab 101", "i7, 32GB RAM", LocalDate.now(), "Active"));
    }
}
