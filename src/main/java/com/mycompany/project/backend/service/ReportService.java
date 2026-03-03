package com.mycompany.project.backend.service;

import com.mycompany.project.backend.model.IssueReport;
import com.mycompany.project.backend.model.ReportStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Service that manages all issue report data and business logic.
 * Demonstrates ENCAPSULATION — the report list is private and
 * business logic (like resolving reports) is handled here, not in the UI.
 */
public class ReportService {
    private List<IssueReport> reports = new ArrayList<>();

    // Returns the full list of reports
    public List<IssueReport> getAll() {
        return reports;
    }

    // Adds a new report
    public void add(IssueReport report) {
        if (report != null) {
            reports.add(report);
        }
    }

    // Removes a report by its ID
    public boolean removeById(String id) {
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getId().equals(id)) {
                reports.remove(i);
                return true;
            }
        }
        return false;
    }

    // Finds a report by its ID
    public IssueReport findById(String id) {
        for (int i = 0; i < reports.size(); i++) {
            IssueReport r = reports.get(i);
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    // Returns total number of reports
    public int getCount() {
        return reports.size();
    }

    // Counts how many reports have a specific status
    public int countByStatus(ReportStatus status) {
        int count = 0;
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    // Resolves the first pending report for a given equipment ID
    public boolean resolveByEquipmentId(String equipmentId) {
        for (int i = 0; i < reports.size(); i++) {
            IssueReport r = reports.get(i);
            if (r.getEquipmentId().equals(equipmentId) && r.getStatus() == ReportStatus.PENDING) {
                r.setStatus(ReportStatus.RESOLVED);
                return true;
            }
        }
        return false;
    }

    // Gets all reports for a specific equipment
    public List<IssueReport> getReportsByEquipmentId(String equipmentId) {
        List<IssueReport> result = new ArrayList<>();
        for (int i = 0; i < reports.size(); i++) {
            IssueReport r = reports.get(i);
            if (r.getEquipmentId().equals(equipmentId)) {
                result.add(r);
            }
        }
        return result;
    }
}
