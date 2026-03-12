# TechTrack - Hardware Monitoring System

A Java Swing desktop application for managing and monitoring hardware equipment, tracking repairs, scheduling maintenance, and reporting issues.

## Features
- **Dashboard** - Real-time summary cards (total devices, health scores, open tickets)
- **Equipment Management** - Full CRUD for hardware devices with search/filter
- **Issue Reports** - Report, track, resolve, and close hardware issues
- **Repair Logs** - Log repairs, track costs, mark completions
- **Maintenance Scheduling** - Schedule, start, and complete maintenance tasks
- **Authentication** - Login system with session management and password change

## OOP Principles Used
- **Encapsulation** - Private fields with public getters/setters in all model classes
- **Inheritance** - All models extend `BaseEntity` abstract class
- **Polymorphism** - `Manageable<T>` interface implemented by all service classes; `toTableRow()` overridden per model
- **Abstraction** - `BaseEntity` abstract class and `Manageable<T>` interface define contracts

## Project Structure
```
src/main/java/com/mycompany/bastaewan/
├── model/          # Data models (BaseEntity, Equipment, IssueReport, etc.)
├── service/        # Business logic (EquipmentService, DataStore, etc.)
├── Bastaewan.java  # Main entry point
├── LoginFrame.java # Login screen
├── MainDashboard.java # Main frame with navigation
├── DashboardPage.java # Dashboard overview
├── EquipmentPage.java # Equipment CRUD
├── IssueReportsPage.java # Issue tracking
├── RepairLogsPage.java # Repair logs
└── MaintenancePage.java # Maintenance scheduling
```

## How to Run
1. Open in NetBeans or any Java IDE
2. Build with Maven: `mvn compile`
3. Run `Bastaewan.java`
4. Login: **admin** / **password**

## Tech Stack
- Java (Swing GUI)
- Maven (build tool)
