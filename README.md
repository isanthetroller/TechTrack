# TechTrack - Hardware Monitoring & Maintenance System (Final Version)

A Java Swing desktop application for managing and monitoring hardware equipment, tracking repairs, scheduling maintenance, and reporting issues. Built using pure Java fundamentals and the four pillars of OOP.

---

## Features
| Module | Description |
|---|---|
| **Login** | Username/password authentication, show/hide password, session management |
| **Dashboard** | Live summary cards (Total Devices, Online, Needs Attention, Open Repairs, Avg Health), Maintenance Intelligence ranking, Activity Log |
| **Equipment** | Full CRUD (Add, Edit, Delete), search/filter by name/type/status, health score tracking |
| **Issue Reports** | Report issues linked to equipment, set priority (Low/Medium/High/Critical), status workflow (Open → In Progress → Resolved → Closed), filter by status |
| **Repair Logs** | Log repairs with technician & cost, status tracking (Pending → In Progress → Completed), auto-fill completion date, filter by status |
| **Maintenance** | Schedule tasks with due dates, status workflow (Scheduled → In Progress → Completed/Overdue), filter by status |
| **Profile** | View profile, change password, logout |

---

## OOP Principles Used
| Pillar | Implementation |
|---|---|
| **Encapsulation** | All model fields are `private` with `public` getters/setters. `SessionManager` hides session state behind static methods. |
| **Inheritance** | `Equipment`, `IssueReport`, `RepairLog`, `MaintenanceTask`, `User` all extend `BaseEntity` abstract class. Common fields (`id`, `createdDate`) are inherited. |
| **Abstraction** | `BaseEntity` is abstract with `toTableRow()` and `getDisplayName()` as abstract methods. `Manageable<T>` interface defines the CRUD contract. |
| **Polymorphism** | Each service (`EquipmentService`, `RepairLogService`, etc.) implements `Manageable<T>` with its own logic. Each model overrides `toTableRow()` differently. |

---

## Final Project Tree
```
TechTrack/
├── .gitignore
├── pom.xml
├── README.md
├── TechTrack_Presentation.pptx
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── mycompany/
        │           └── bastaewan/
        │               │
        │               ├── model/                        # DATA LAYER (Models)
        │               │   ├── BaseEntity.java           #   Abstract parent class (id, createdDate, abstract methods)
        │               │   ├── Equipment.java            #   Equipment model (deviceName, type, status, healthScore, location, category)
        │               │   ├── IssueReport.java          #   Issue report model (equipmentName, description, priority, status)
        │               │   ├── RepairLog.java            #   Repair log model (equipmentName, description, technician, cost, status)
        │               │   ├── MaintenanceTask.java      #   Maintenance task model (equipmentName, task, dueDate, status)
        │               │   └── User.java                 #   User model (username, password, role)
        │               │
        │               ├── service/                      # BUSINESS LAYER (Services)
        │               │   ├── Manageable.java           #   Interface defining CRUD operations (add, update, delete, getById, getAll)
        │               │   ├── DataStore.java            #   Singleton — single access point to all services
        │               │   ├── AuthService.java          #   Authentication & password management
        │               │   ├── EquipmentService.java     #   Equipment CRUD + countByStatus, getAverageHealthScore
        │               │   ├── IssueReportService.java   #   Issue report CRUD + countByStatus
        │               │   ├── RepairLogService.java     #   Repair log CRUD + countOpenTickets
        │               │   └── MaintenanceService.java   #   Maintenance task CRUD + countByStatus
        │               │
        │               ├── Bastaewan.java                # ENTRY POINT — launches LoginFrame
        │               ├── LoginFrame.java               # UI — Login screen with authentication
        │               ├── MainDashboard.java            # UI — Main frame with sidebar navigation & CardLayout
        │               ├── SessionManager.java           # UI — Current user session holder
        │               ├── LogoUtils.java                # UI — Logo image loader utility
        │               ├── Dashboard.java                # UI — Legacy dashboard frame (unused)
        │               ├── DashboardPage.java            # UI — Dashboard panel (live stats, maintenance intelligence)
        │               ├── EquipmentPage.java            # UI — Equipment CRUD panel (add, edit, delete, search)
        │               ├── IssueReportsPage.java         # UI — Issue reports panel (report, resolve, close, filter)
        │               ├── RepairLogsPage.java           # UI — Repair logs panel (add, complete, filter)
        │               └── MaintenancePage.java          # UI — Maintenance panel (schedule, start, complete, filter)
        │
        └── resources/
            ├── NCST-LOGOS-scaled.png
            └── image/
                └── logo.png
```

---

## How to Run
1. Open in NetBeans or any Java IDE
2. Build with Maven: `mvn compile`
3. Run `Bastaewan.java`
4. Login: **admin** / **password**

## Tech Stack
- **Language:** Java (JDK 25)
- **GUI:** Java Swing
- **Build:** Apache Maven
- **Data:** In-memory (ArrayList collections)
- **Pattern:** Singleton (DataStore), MVC-inspired layer separation
- **No external libraries or databases — pure Java fundamentals only**
