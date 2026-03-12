package com.mycompany.bastaewan.service;

// Singleton pattern: single point of access to all services
public class DataStore {
    private static DataStore instance;

    // Encapsulation: private service instances
    private EquipmentService equipmentService;
    private IssueReportService issueReportService;
    private RepairLogService repairLogService;
    private MaintenanceService maintenanceService;
    private AuthService authService;

    private DataStore() {
        equipmentService = new EquipmentService();
        issueReportService = new IssueReportService();
        repairLogService = new RepairLogService();
        maintenanceService = new MaintenanceService();
        authService = new AuthService();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public EquipmentService getEquipmentService() {
        return equipmentService;
    }

    public IssueReportService getIssueReportService() {
        return issueReportService;
    }

    public RepairLogService getRepairLogService() {
        return repairLogService;
    }

    public MaintenanceService getMaintenanceService() {
        return maintenanceService;
    }

    public AuthService getAuthService() {
        return authService;
    }
}
