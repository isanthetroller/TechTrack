package com.mycompany.project.backend.service;

import com.mycompany.project.backend.model.IssueReport;
import com.mycompany.project.backend.model.ReportStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Service handling all issue report business logic and data operations.
 * Demonstrates POLYMORPHISM (implements IEntityService&lt;IssueReport&gt;)
 * and ENCAPSULATION (private list, business logic methods).
 */
public class ReportService implements IEntityService<IssueReport> {
    private final List<IssueReport> reports = new ArrayList<>();

    @Override
    public List<IssueReport> getAll() {
        return new ArrayList<>(reports); // Defensive copy
    }

    @Override
    public void add(IssueReport report) {
        if (report == null) {
            throw new IllegalArgumentException("Report cannot be null.");
        }
        reports.add(report);
    }

    @Override
    public boolean removeById(String id) {
        return reports.removeIf(r -> r.getId().equals(id));
    }

    @Override
    public IssueReport findById(String id) {
        for (IssueReport r : reports) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return reports.size();
    }

    /**
     * Count reports with a specific status.
     */
    public long countByStatus(ReportStatus status) {
        return reports.stream()
                .filter(r -> r.getStatus() == status)
                .count();
    }

    /**
     * Resolve the first pending report for a given equipment ID.
     * Business logic is encapsulated here rather than in the UI layer.
     * @return true if a report was found and resolved.
     */
    public boolean resolveByEquipmentId(String equipmentId) {
        for (IssueReport r : reports) {
            if (r.getEquipmentId().equals(equipmentId) && r.getStatus() == ReportStatus.PENDING) {
                r.setStatus(ReportStatus.RESOLVED);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all reports for a specific equipment.
     */
    public List<IssueReport> getReportsByEquipmentId(String equipmentId) {
        List<IssueReport> result = new ArrayList<>();
        for (IssueReport r : reports) {
            if (r.getEquipmentId().equals(equipmentId)) {
                result.add(r);
            }
        }
        return result;
    }
}
