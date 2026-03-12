package com.mycompany.bastaewan.service;

import com.mycompany.bastaewan.model.IssueReport;
import java.util.ArrayList;

public class IssueReportService implements Manageable<IssueReport> {
    private ArrayList<IssueReport> issueList;
    private int nextId;

    public IssueReportService() {
        issueList = new ArrayList<>();
        nextId = 1;
        loadSampleData();
    }

    private void loadSampleData() {
        add(new IssueReport(0, "WS-ACC-007", "Frequent BSOD crashes", "Critical", "Open", "admin"));
        add(new IssueReport(0, "SRV-PROD-02", "High CPU temperature", "High", "In Progress", "admin"));
        add(new IssueReport(0, "WS-ACC-003", "Won't boot after update", "Critical", "Open", "admin"));
        add(new IssueReport(0, "PRT-001", "Paper jam recurring", "Medium", "Open", "admin"));
        add(new IssueReport(0, "NET-SW-001", "Intermittent connection drops", "Low", "Resolved", "admin"));
    }

    @Override
    public void add(IssueReport item) {
        item.setId(nextId++);
        issueList.add(item);
    }

    @Override
    public void update(int id, IssueReport item) {
        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getId() == id) {
                item.setId(id);
                issueList.set(i, item);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getId() == id) {
                issueList.remove(i);
                return;
            }
        }
    }

    @Override
    public IssueReport getById(int id) {
        for (IssueReport r : issueList) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    @Override
    public ArrayList<IssueReport> getAll() {
        return issueList;
    }

    @Override
    public int getNextId() {
        return nextId;
    }

    public int countByStatus(String status) {
        int count = 0;
        for (IssueReport r : issueList) {
            if (r.getStatus().equals(status)) {
                count++;
            }
        }
        return count;
    }
}
