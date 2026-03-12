"""
Generate TechTrack_Presentation.pdf — mirrors the PPTX content using reportlab.
Landscape widescreen pages (13.333 x 7.5 inches).
"""
from reportlab.lib.pagesizes import landscape
from reportlab.lib.units import inch
from reportlab.lib.colors import Color, HexColor, white, black
from reportlab.pdfgen import canvas
from reportlab.lib.enums import TA_LEFT, TA_CENTER, TA_RIGHT
import os

PAGE_W, PAGE_H = 13.333 * inch, 7.5 * inch

# ── Colors ──
NAVY      = HexColor("#0a1931")
DARK_BLUE = HexColor("#003f87")
BLUE      = HexColor("#0066cc")
LIGHT_BLUE= HexColor("#e0edff")
YELLOW    = HexColor("#ffcc00")
GOLD      = HexColor("#ffb700")
OFF_WHITE = HexColor("#f5f7fc")
BLK       = HexColor("#191923")
GRAY      = HexColor("#788291")
LIGHT_GRAY= HexColor("#d2d7e1")
GREEN     = HexColor("#22a65a")
RED       = HexColor("#dc3c32")
ORANGE    = HexColor("#f59b1e")
PURPLE    = HexColor("#8232b4")

def rect(c, x, y, w, h, fill, stroke=None):
    c.saveState()
    c.setFillColor(fill)
    if stroke:
        c.setStrokeColor(stroke); c.setLineWidth(1)
        c.rect(x, y, w, h, fill=1, stroke=1)
    else:
        c.rect(x, y, w, h, fill=1, stroke=0)
    c.restoreState()

def rounded_rect(c, x, y, w, h, r, fill, stroke=None, sw=1):
    c.saveState()
    c.setFillColor(fill)
    if stroke:
        c.setStrokeColor(stroke); c.setLineWidth(sw)
        c.roundRect(x, y, w, h, r, fill=1, stroke=1)
    else:
        c.roundRect(x, y, w, h, r, fill=1, stroke=0)
    c.restoreState()

def text(c, x, y, txt, sz=14, bold=False, color=BLK, align="left"):
    c.saveState()
    c.setFillColor(color)
    font = "Helvetica-Bold" if bold else "Helvetica"
    c.setFont(font, sz)
    if align == "center":
        c.drawCentredString(x, y, txt)
    elif align == "right":
        c.drawRightString(x, y, txt)
    else:
        c.drawString(x, y, txt)
    c.restoreState()

def multiline(c, x, y, lines, sz=11, color=BLK, leading=16, bold_first=False):
    c.saveState()
    c.setFillColor(color)
    for i, line in enumerate(lines):
        font = "Helvetica-Bold" if (bold_first and i == 0) else "Helvetica"
        c.setFont(font, sz)
        c.drawString(x, y, line)
        y -= leading
    c.restoreState()
    return y

def bg(c, color=OFF_WHITE):
    rect(c, 0, 0, PAGE_W, PAGE_H, color)

def title_band(c, title, subtitle=None):
    rect(c, 0, PAGE_H - 1.05*inch, PAGE_W, 1.05*inch, NAVY)
    rect(c, 0, PAGE_H - 1.1*inch, PAGE_W, 0.05*inch, YELLOW)
    text(c, 0.7*inch, PAGE_H - 0.65*inch, title, sz=26, bold=True, color=white)
    if subtitle:
        text(c, 0.7*inch, PAGE_H - 0.9*inch, subtitle, sz=12, color=YELLOW)
    text(c, 12.2*inch, PAGE_H - 0.6*inch, "TechTrack", sz=10, color=HexColor("#6482b4"), align="right")

def info_card(c, x, y, w, h, title, body_lines, accent=BLUE):
    rounded_rect(c, x, y, w, h, 6, white, stroke=LIGHT_GRAY, sw=1.2)
    rect(c, x + 3, y + h - 5, w - 6, 4, accent)
    text(c, x + 14, y + h - 24, title, sz=13, bold=True, color=accent)
    ly = y + h - 44
    for line in body_lines:
        if ly < y + 8:
            break
        text(c, x + 14, ly, line, sz=9, color=BLK)
        ly -= 14

def _draw_tri(c, pts, color):
    c.saveState()
    c.setFillColor(color)
    p = c.beginPath()
    p.moveTo(pts[0][0], pts[0][1])
    p.lineTo(pts[1][0], pts[1][1])
    p.lineTo(pts[2][0], pts[2][1])
    p.close()
    c.drawPath(p, fill=1, stroke=0)
    c.restoreState()

def arrow_down(c, x, y1, y2, color=GRAY, w=1.5):
    c.saveState(); c.setStrokeColor(color); c.setLineWidth(w)
    c.line(x, y1, x, y2); c.restoreState()
    _draw_tri(c, [(x, y2), (x - 4, y2 + 8), (x + 4, y2 + 8)], color)

def arrow_right(c, x1, y, x2, color=GRAY, w=1.5):
    c.saveState(); c.setStrokeColor(color); c.setLineWidth(w)
    c.line(x1, y, x2, y); c.restoreState()
    _draw_tri(c, [(x2, y), (x2 - 8, y + 4), (x2 - 8, y - 4)], color)

def arrow_left(c, x1, y, x2, color=GRAY, w=1.5):
    c.saveState(); c.setStrokeColor(color); c.setLineWidth(w)
    c.line(x1, y, x2, y); c.restoreState()
    _draw_tri(c, [(x2, y), (x2 + 8, y + 4), (x2 + 8, y - 4)], color)

def arrow_up(c, x, y1, y2, color=GRAY, w=1.5):
    c.saveState(); c.setStrokeColor(color); c.setLineWidth(w)
    c.line(x, y1, x, y2); c.restoreState()
    _draw_tri(c, [(x, y2), (x - 4, y2 - 8), (x + 4, y2 - 8)], color)

def flow_box(c, x, y, w, h, txt, fill=BLUE, txtcolor=white, sz=9):
    rounded_rect(c, x, y, w, h, 6, fill)
    text(c, x + w/2, y + h/2 - 3, txt, sz=sz, bold=True, color=txtcolor, align="center")

def diamond_shape(c, cx, cy, rw, rh, txt, fill=GOLD):
    c.saveState()
    c.setFillColor(fill)
    p = c.beginPath()
    p.moveTo(cx, cy + rh)
    p.lineTo(cx + rw, cy)
    p.lineTo(cx, cy - rh)
    p.lineTo(cx - rw, cy)
    p.close()
    c.drawPath(p, fill=1, stroke=0)
    c.setFillColor(BLK); c.setFont("Helvetica-Bold", 8)
    c.drawCentredString(cx, cy + 4, "Valid")
    c.drawCentredString(cx, cy - 8, "Credentials?")
    c.restoreState()

def erd_box(c, x, y, w, title, fields, color=BLUE):
    row_h = 14
    header_h = 24
    body_h = row_h * len(fields) + 6
    total_h = header_h + body_h
    # Header
    rect(c, x, y - header_h, w, header_h, color)
    text(c, x + w/2, y - header_h + 7, title, sz=9, bold=True, color=white, align="center")
    # Body
    rounded_rect(c, x, y - total_h, w, body_h, 4, white, stroke=color, sw=1.5)
    fy = y - header_h - 12
    for fld in fields:
        font = "Helvetica-Bold" if fld.startswith("PK") or fld.startswith("FK") else "Helvetica"
        c.saveState()
        c.setFont(font, 7.5)
        c.setFillColor(BLK)
        c.drawString(x + 6, fy, fld)
        c.restoreState()
        fy -= row_h
    return y - total_h


# ════════════════════════════════════════════════════════════════════
# BUILD PDF
# ════════════════════════════════════════════════════════════════════
output = os.path.join(os.path.dirname(os.path.abspath(__file__)), "TechTrack_Presentation.pdf")
c = canvas.Canvas(output, pagesize=(PAGE_W, PAGE_H))

# ── SLIDE 1: TITLE ──
bg(c, NAVY)
rect(c, 0, 0, 0.15*inch, PAGE_H, YELLOW)
rect(c, 0.15*inch, 0, 0.04*inch, PAGE_H, GOLD)

text(c, 1.5*inch, PAGE_H - 2.2*inch, "TechTrack", sz=56, bold=True, color=white)
rect(c, 1.5*inch, PAGE_H - 2.45*inch, 3.5*inch, 4, YELLOW)
text(c, 1.5*inch, PAGE_H - 2.85*inch, "Hardware Monitoring & Maintenance System", sz=20, color=YELLOW)
multiline(c, 1.5*inch, PAGE_H - 3.5*inch, [
    "Java Swing Desktop Application",
    "Built with OOP Fundamentals  \u2022  No External Dependencies",
    "Final Version"
], sz=13, color=HexColor("#a0b4dc"), leading=20)

rect(c, 0, 0, PAGE_W, 0.6*inch, DARK_BLUE)
text(c, 1.5*inch, 0.2*inch, "CS / IT Project Presentation  \u2022  March 2026", sz=11, color=HexColor("#8ca0c8"))
c.showPage()

# ── SLIDE 2: SYSTEM OVERVIEW ──
bg(c)
title_band(c, "System Overview", "What is TechTrack?")

multiline(c, 0.7*inch, PAGE_H - 1.7*inch, [
    "TechTrack is a desktop application for monitoring hardware equipment,",
    "scheduling maintenance, logging repairs, and managing issue reports \u2014 all in one system."
], sz=14, color=BLK, leading=22)

cards = [
    ("Login & Auth", ["Secure username/password login", "Session management", "Change password"], BLUE),
    ("Dashboard", ["Live summary statistics", "Maintenance intelligence", "Activity log"], DARK_BLUE),
    ("Equipment", ["CRUD operations", "Search & filter", "Health score tracking"], GREEN),
    ("Issue Reports", ["Report issues by equipment", "Priority levels", "Status workflow"], ORANGE),
    ("Repair Logs", ["Log repairs with costs", "Track technicians", "Mark completions"], RED),
    ("Maintenance", ["Schedule tasks", "Due date tracking", "Status management"], PURPLE),
]
for i, (t, lines, accent) in enumerate(cards):
    col = i % 3
    row = i // 3
    cx_ = 0.7*inch + col * 4.1*inch
    cy_ = PAGE_H - 4.0*inch - row * 1.9*inch
    info_card(c, cx_, cy_, 3.8*inch, 1.7*inch, t, lines, accent)
c.showPage()

# ── SLIDE 3: CODE STRUCTURE ──
bg(c)
title_band(c, "Code Structure", "3-Layer Architecture")

layers = [
    ("UI LAYER (Presentation)", BLUE, [
        "LoginFrame.java", "MainDashboard.java",
        "DashboardPage.java", "EquipmentPage.java",
        "IssueReportsPage.java", "RepairLogsPage.java",
        "MaintenancePage.java", "SessionManager.java",
        "LogoUtils.java"
    ]),
    ("SERVICE LAYER (Business Logic)", GREEN, [
        "Manageable<T>  (Interface)",
        "DataStore  (Singleton)",
        "AuthService",
        "EquipmentService",
        "IssueReportService",
        "RepairLogService",
        "MaintenanceService"
    ]),
    ("MODEL LAYER (Data)", ORANGE, [
        "BaseEntity  (Abstract Class)",
        "Equipment",
        "IssueReport",
        "RepairLog",
        "MaintenanceTask",
        "User"
    ]),
]

for i, (lbl, accent, files) in enumerate(layers):
    lx = 0.5*inch + i * 4.2*inch
    ly_top = PAGE_H - 1.6*inch
    # Header
    rounded_rect(c, lx, ly_top - 0.45*inch, 3.9*inch, 0.45*inch, 6, accent)
    text(c, lx + 1.95*inch, ly_top - 0.32*inch, lbl, sz=12, bold=True, color=white, align="center")
    # Files
    fy = ly_top - 0.65*inch
    for f in files:
        rounded_rect(c, lx + 0.1*inch, fy - 0.28*inch, 3.7*inch, 0.3*inch, 4, white, stroke=LIGHT_GRAY)
        text(c, lx + 0.25*inch, fy - 0.18*inch, f, sz=9, color=BLK)
        fy -= 0.35*inch

# Arrows between layers
mid_y1 = PAGE_H - 3.4*inch
mid_y2 = PAGE_H - 3.65*inch
for i in range(2):
    ax = 0.5*inch + (i+1)*4.2*inch
    arrow_right(c, ax - 0.4*inch, mid_y1, ax + 0.05*inch, GRAY, 2)
    arrow_left(c, ax + 0.05*inch, mid_y2, ax - 0.4*inch, GRAY, 2)

text(c, 4.35*inch, mid_y1 + 4, "calls \u25b8", sz=8, color=GRAY, align="center")
text(c, 4.35*inch, mid_y2 + 4, "\u25c2 returns", sz=8, color=GRAY, align="center")
text(c, 8.55*inch, mid_y1 + 4, "uses \u25b8", sz=8, color=GRAY, align="center")
text(c, 8.55*inch, mid_y2 + 4, "\u25c2 data", sz=8, color=GRAY, align="center")
c.showPage()

# ── SLIDE 4: OOP PILLARS ──
bg(c)
title_band(c, "Four Pillars of OOP")

pillars = [
    ("Encapsulation", BLUE, [
        "All model fields are private.",
        "Access only through public",
        "getters and setters.",
        "",
        "SessionManager hides",
        "session state behind",
        "static methods."
    ]),
    ("Inheritance", GREEN, [
        "Equipment, IssueReport,",
        "RepairLog, MaintenanceTask,",
        "and User all extend the",
        "BaseEntity abstract class.",
        "",
        "Common fields (id, createdDate)",
        "are inherited automatically."
    ]),
    ("Abstraction", ORANGE, [
        "BaseEntity defines abstract",
        "methods: toTableRow() and",
        "getDisplayName().",
        "",
        "Manageable<T> interface",
        "hides CRUD implementation",
        "details from the UI layer."
    ]),
    ("Polymorphism", PURPLE, [
        "Each service implements",
        "Manageable<T> differently.",
        "",
        "Each model overrides",
        "toTableRow() with its own",
        "column data.",
        "",
        "Same interface, different",
        "behavior per type."
    ]),
]

for i, (name, accent, desc_lines) in enumerate(pillars):
    px = 0.5*inch + i * 3.15*inch
    py = PAGE_H - 6.9*inch
    pw = 2.95*inch
    ph = 5.2*inch
    rounded_rect(c, px, py, pw, ph, 6, white, stroke=LIGHT_GRAY)
    rect(c, px + 3, py + ph - 5, pw - 6, 4, accent)
    # Number circle
    cx_ = px + pw/2
    cy_ = py + ph - 0.7*inch
    c.saveState()
    c.setFillColor(accent)
    c.circle(cx_, cy_, 0.3*inch, fill=1, stroke=0)
    c.setFillColor(white); c.setFont("Helvetica-Bold", 20)
    c.drawCentredString(cx_, cy_ - 7, str(i+1))
    c.restoreState()
    # Title
    text(c, cx_, py + ph - 1.25*inch, name, sz=15, bold=True, color=accent, align="center")
    # Description
    dy = py + ph - 1.65*inch
    for line in desc_lines:
        text(c, cx_, dy, line, sz=9.5, color=BLK, align="center")
        dy -= 16
c.showPage()

# ── SLIDE 5: ERD ──
bg(c)
title_band(c, "Entity Relationship Diagram (ERD)")

# BaseEntity at top center
base_x = 5.0*inch
base_top = PAGE_H - 1.5*inch
base_w = 3.0*inch
base_bottom = erd_box(c, base_x, base_top, base_w, "BaseEntity  \u00ABabstract\u00BB", [
    "PK  id : int",
    "    createdDate : String",
    "    toTableRow() : String[]  \u00ABabstract\u00BB",
    "    getDisplayName() : String  \u00ABabstract\u00BB"
], NAVY)

# Children
children = [
    (0.3*inch, "User", [
        "PK  id : int",
        "    username : String",
        "    password : String",
        "    role : String"
    ], BLUE),
    (2.9*inch, "Equipment", [
        "PK  id : int",
        "    deviceName : String",
        "    type : String",
        "    status : String",
        "    healthScore : int",
        "    location : String",
        "    category : String"
    ], GREEN),
    (5.5*inch, "IssueReport", [
        "PK  id : int",
        "FK  equipmentName : String",
        "    description : String",
        "    priority : String",
        "    status : String",
        "    reportedBy : String"
    ], ORANGE),
    (8.1*inch, "RepairLog", [
        "PK  id : int",
        "FK  equipmentName : String",
        "    description : String",
        "    technician : String",
        "    status : String",
        "    cost : double",
        "    dateCompleted : String"
    ], RED),
    (10.7*inch, "MaintenanceTask", [
        "PK  id : int",
        "FK  equipmentName : String",
        "    task : String",
        "    dueDate : String",
        "    status : String"
    ], PURPLE),
]

child_top = PAGE_H - 3.8*inch
child_w = 2.4*inch
child_bottoms = []
for cx_, title_, fields_, color_ in children:
    cb = erd_box(c, cx_, child_top, child_w, title_, fields_, color_)
    child_bottoms.append(cb)
    # Inheritance arrow from child up to base
    ccx = cx_ + child_w / 2
    arrow_up(c, ccx, child_top, base_bottom + 4, color_, 1.5)

text(c, 6.5*inch, base_bottom - 12, "\u25b2  extends (inheritance)", sz=9, bold=True, color=GRAY, align="center")

text(c, PAGE_W/2, 0.5*inch,
     "IssueReport, RepairLog, and MaintenanceTask reference Equipment by equipmentName  (1 Equipment \u2192 many Issues/Repairs/Tasks)",
     sz=9.5, color=GRAY, align="center")
c.showPage()

# ── SLIDE 6: FLOWCHART ──
bg(c)
title_band(c, "System Flowchart")

# Left column — login flow
lcx = 2.8*inch  # center x
bw = 1.8*inch
bh = 0.42*inch

# Start
sy = PAGE_H - 1.7*inch
flow_box(c, lcx - bw/2, sy, bw, bh, "START", NAVY, white, 10)
arrow_down(c, lcx, sy, sy - 0.55*inch, NAVY)

# Login Screen
ly = sy - 0.55*inch
flow_box(c, lcx - bw/2, ly - bh, bw, bh, "Login Screen", BLUE, white, 9)
arrow_down(c, lcx, ly - bh, ly - bh - 0.35*inch, NAVY)

# Enter credentials
ey = ly - bh - 0.35*inch
ew = 2.2*inch
flow_box(c, lcx - ew/2, ey - bh, ew, bh, "Enter Username & Password", LIGHT_BLUE, BLK, 9)
arrow_down(c, lcx, ey - bh, ey - bh - 0.3*inch, NAVY)

# Diamond
dy = ey - bh - 0.3*inch
diamond_shape(c, lcx, dy - 0.4*inch, 0.7*inch, 0.42*inch, "Valid\nCredentials?")

# No branch — right
no_y = dy - 0.4*inch
text(c, lcx + 0.75*inch, no_y + 2, "No", sz=9, bold=True, color=RED)
arrow_right(c, lcx + 0.65*inch, no_y, lcx + 1.5*inch, RED)
flow_box(c, lcx + 1.5*inch, no_y - bh/2, 1.5*inch, bh, "Show Error", RED, white, 9)
# Error arrow up and left back to login
err_x = lcx + 2.25*inch
c.saveState()
c.setStrokeColor(RED); c.setLineWidth(1.5)
c.line(err_x, no_y + bh/2, err_x, ly + 3)  # up
c.line(err_x, ly + 3, lcx + bw/2, ly + 3)  # left
c.restoreState()
# arrow head at login
_draw_tri(c, [(lcx + bw/2, ly + 3), (lcx + bw/2 + 8, ly + 7), (lcx + bw/2 + 8, ly - 1)], RED)

# Yes branch — down
text(c, lcx + 0.05*inch, dy - 0.85*inch, "Yes", sz=9, bold=True, color=GREEN)
arrow_down(c, lcx, dy - 0.82*inch, dy - 1.05*inch, GREEN)

# Set Session
ss_y = dy - 1.05*inch
flow_box(c, lcx - bw/2, ss_y - bh, bw, bh, "Set Session", GREEN, white, 9)
arrow_down(c, lcx, ss_y - bh, ss_y - bh - 0.3*inch, NAVY)

# Main Dashboard
md_y = ss_y - bh - 0.3*inch
flow_box(c, lcx - ew/2, md_y - 0.5*inch, ew, 0.5*inch, "Main Dashboard", NAVY, white, 10)

# Navigate arrow to right
nav_end_x = 6.9*inch
md_center_y = md_y - 0.25*inch
text(c, 5.0*inch, md_center_y + 3, "Navigate \u25b8", sz=9, bold=True, color=GRAY, align="center")
arrow_right(c, lcx + ew/2, md_center_y, nav_end_x, GRAY)

# Logout arrow to left
text(c, lcx - 2.0*inch, md_center_y + 3, "Logout", sz=9, bold=True, color=RED)
lo_x = lcx - 1.5*inch
c.saveState(); c.setStrokeColor(RED); c.setLineWidth(1.5)
c.line(lcx - ew/2, md_center_y, lo_x, md_center_y)  # left
c.line(lo_x, md_center_y, lo_x, ly + 3)  # up
c.line(lo_x, ly + 3, lcx - bw/2, ly + 3)  # right to login
c.restoreState()
_draw_tri(c, [(lcx - bw/2, ly + 3), (lcx - bw/2 - 8, ly + 7), (lcx - bw/2 - 8, ly - 1)], RED)

# Right side — module pages
rcx = 8.5*inch
modules = [
    ("Dashboard Page", DARK_BLUE),
    ("Equipment Page", GREEN),
    ("Issue Reports Page", ORANGE),
    ("Repair Logs Page", RED),
    ("Maintenance Page", PURPLE),
]
mbw = 2.0*inch
mbh = 0.38*inch

for i, (mod_name, mod_color) in enumerate(modules):
    my = PAGE_H - 1.7*inch - i * 0.55*inch
    flow_box(c, rcx - mbw/2, my - mbh, mbw, mbh, mod_name, mod_color, white, 9)
    arrow_right(c, nav_end_x, my - mbh/2, rcx - mbw/2, GRAY, 1)

# Vertical distribution line from navigate endpoint up to module pages
first_mod_mid = PAGE_H - 1.7*inch - mbh/2
last_mod_mid = PAGE_H - 1.7*inch - 4*0.55*inch - mbh/2
c.saveState()
c.setStrokeColor(GRAY); c.setLineWidth(1)
c.line(nav_end_x, md_center_y, nav_end_x, first_mod_mid)
c.restoreState()

# CRUD box
crud_x = 11.0*inch
crud_y = PAGE_H - 1.5*inch
crud_h = 3.3*inch
rounded_rect(c, crud_x, crud_y - crud_h, 2.0*inch, crud_h, 6, white, stroke=LIGHT_GRAY)
rect(c, crud_x + 2, crud_y - 5, 1.96*inch, 4, BLUE)
text(c, crud_x + 1.0*inch, crud_y - 22, "CRUD Operations", sz=10, bold=True, color=BLUE, align="center")

crud_items = ["Add / Create", "View / Read", "Edit / Update", "Delete", "Search / Filter"]
for j, ci in enumerate(crud_items):
    cy_ = crud_y - 0.5*inch - j * 0.48*inch
    flow_box(c, crud_x + 0.15*inch, cy_ - 0.3*inch, 1.7*inch, 0.32*inch, ci, LIGHT_BLUE, BLK, 9)

# Arrow from modules to CRUD
mid_mod_y = PAGE_H - 3.0*inch
arrow_right(c, rcx + mbw/2, mid_mod_y, crud_x, GRAY, 1)
text(c, 10.0*inch, mid_mod_y + 4, "performs \u25b8", sz=8, color=GRAY, align="center")
c.showPage()

# ── SLIDE 7: LOGIN & DASHBOARD ──
bg(c)
title_band(c, "Features: Login & Dashboard")

info_card(c, 0.5*inch, PAGE_H - 6.9*inch, 5.8*inch, 5.2*inch, "Login System", [
    "\u2022 Username and password authentication",
    "\u2022 Credentials validated through AuthService",
    "\u2022 Show / hide password toggle checkbox",
    "\u2022 Session created via SessionManager on success",
    "\u2022 Invalid login shows error dialog",
    "\u2022 Logout clears session and returns to login",
    "",
    "Default credentials:",
    "  Username: admin",
    "  Password: password"
], BLUE)

info_card(c, 6.8*inch, PAGE_H - 6.9*inch, 6.0*inch, 5.2*inch, "Dashboard", [
    "Live Summary Cards (from services):",
    "  \u2022 Total Devices \u2014 count of all equipment",
    "  \u2022 Online & Healthy \u2014 count by status",
    "  \u2022 Needs Attention \u2014 flagged devices",
    "  \u2022 Open Repair Tickets \u2014 incomplete repairs",
    "  \u2022 Avg Health Score \u2014 average across all",
    "",
    "Maintenance Intelligence:",
    "  \u2022 Ranks devices by repair frequency",
    "  \u2022 Shows category and failure rate",
    "",
    "Activity Log \u2014 session events and timestamps"
], DARK_BLUE)
c.showPage()

# ── SLIDE 8: EQUIPMENT & ISSUES ──
bg(c)
title_band(c, "Features: Equipment & Issue Reports")

info_card(c, 0.5*inch, PAGE_H - 6.9*inch, 5.8*inch, 5.3*inch, "Equipment Management", [
    "Full CRUD with form dialogs:",
    "  \u2022 Add \u2014 device name, type, status, health, location, category",
    "  \u2022 Edit \u2014 select row, modify via dialog",
    "  \u2022 Delete \u2014 confirmation before removal",
    "",
    "Search:",
    "  \u2022 Filter by device name, type, or status",
    "  \u2022 Real-time table refresh",
    "",
    "Types: Workstation, Server, Network Switch, Router, Printer",
    "Statuses: Online, Offline, Needs Attention",
    "Categories: Hardware, Software, Network"
], GREEN)

info_card(c, 6.8*inch, PAGE_H - 6.9*inch, 6.0*inch, 5.3*inch, "Issue Reports", [
    "Report issues linked to registered equipment:",
    "  \u2022 Select equipment from dropdown",
    "  \u2022 Enter description and set priority",
    "  \u2022 Auto-set reporter from current session",
    "",
    "Priority Levels: Low, Medium, High, Critical",
    "",
    "Status Workflow:",
    "  Open \u2192 In Progress \u2192 Resolved \u2192 Closed",
    "",
    "Actions:",
    "  \u2022 Report Issue \u2014 open submission form",
    "  \u2022 Resolve \u2014 mark as resolved",
    "  \u2022 Close Issue \u2014 mark as closed",
    "  \u2022 Delete \u2014 remove with confirmation",
    "  \u2022 Filter \u2014 dropdown filter by status"
], ORANGE)
c.showPage()

# ── SLIDE 9: REPAIRS & MAINTENANCE ──
bg(c)
title_band(c, "Features: Repair Logs & Maintenance")

info_card(c, 0.5*inch, PAGE_H - 6.9*inch, 5.8*inch, 5.3*inch, "Repair Logs", [
    "Log repairs for equipment:",
    "  \u2022 Select equipment from dropdown",
    "  \u2022 Enter description, technician name, cost",
    "  \u2022 New repairs default to Pending status",
    "",
    "Status Workflow:",
    "  Pending \u2192 In Progress \u2192 Completed",
    "",
    "Mark Complete:",
    "  \u2022 Auto-fills completion date (today's date)",
    "  \u2022 Updates status to Completed",
    "",
    "Actions: Add Repair, Mark Complete, Delete",
    "Filter: by status (All / Pending / In Progress / Completed)"
], RED)

info_card(c, 6.8*inch, PAGE_H - 6.9*inch, 6.0*inch, 5.3*inch, "Maintenance Schedule", [
    "Schedule maintenance tasks:",
    "  \u2022 Select equipment from dropdown",
    "  \u2022 Enter task description and due date",
    "  \u2022 New tasks default to Scheduled status",
    "",
    "Status Workflow:",
    "  Scheduled \u2192 In Progress \u2192 Completed / Overdue",
    "",
    "Actions:",
    "  \u2022 Schedule Task \u2014 create new task",
    "  \u2022 Start \u2014 change status to In Progress",
    "  \u2022 Complete \u2014 mark as Completed",
    "  \u2022 Delete \u2014 remove with confirmation",
    "",
    "Filter: dropdown by status"
], PURPLE)
c.showPage()

# ── SLIDE 10: TECH STACK ──
bg(c)
title_band(c, "Technology Stack")

tech_items = [
    ("Language", "Java (JDK 25)", BLUE),
    ("GUI", "Java Swing", GREEN),
    ("Build Tool", "Apache Maven", ORANGE),
    ("Data Storage", "In-memory (ArrayList)", PURPLE),
    ("Design Pattern", "Singleton, MVC-inspired", DARK_BLUE),
    ("Dependencies", "None \u2014 pure Java only", RED),
]

for i, (label, value, accent) in enumerate(tech_items):
    row = i // 2
    col = i % 2
    cx_ = 0.7*inch + col * 6.2*inch
    cy_ = PAGE_H - 2.2*inch - row * 1.65*inch
    rounded_rect(c, cx_, cy_ - 1.2*inch, 5.8*inch, 1.3*inch, 6, white, stroke=LIGHT_GRAY)
    rect(c, cx_ + 3, cy_ + 0.05*inch, 5.74*inch, 4, accent)
    text(c, cx_ + 0.25*inch, cy_ - 0.25*inch, label, sz=12, bold=True, color=accent)
    text(c, cx_ + 0.25*inch, cy_ - 0.65*inch, value, sz=17, bold=True, color=BLK)
c.showPage()

# ── SLIDE 11: THANK YOU ──
bg(c, NAVY)
rect(c, 0, 0, 0.15*inch, PAGE_H, YELLOW)
rect(c, 0.15*inch, 0, 0.04*inch, PAGE_H, GOLD)

text(c, PAGE_W/2, PAGE_H/2 + 0.6*inch, "Thank You", sz=52, bold=True, color=white, align="center")
rect(c, 5.0*inch, PAGE_H/2 + 0.25*inch, 3.0*inch, 4, YELLOW)
text(c, PAGE_W/2, PAGE_H/2 - 0.15*inch,
     "TechTrack \u2014 Hardware Monitoring & Maintenance System", sz=17, color=YELLOW, align="center")
text(c, PAGE_W/2, PAGE_H/2 - 0.65*inch,
     "Java  \u2022  Swing  \u2022  OOP  \u2022  Maven", sz=13, color=HexColor("#8ca0c8"), align="center")
rect(c, 0, 0, PAGE_W, 0.6*inch, DARK_BLUE)
c.showPage()

c.save()
print(f"Saved: {output}")
