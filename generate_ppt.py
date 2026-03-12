from pptx import Presentation
from pptx.util import Inches, Pt, Emu
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN
import os

prs = Presentation()
prs.slide_width = Inches(13.333)
prs.slide_height = Inches(7.5)

BLUE = RGBColor(0, 102, 204)
DARK_BLUE = RGBColor(0, 73, 153)
YELLOW = RGBColor(255, 204, 0)
WHITE = RGBColor(255, 255, 255)
BLACK = RGBColor(30, 30, 60)
GRAY = RGBColor(100, 100, 100)
GREEN = RGBColor(39, 174, 96)
RED = RGBColor(231, 76, 60)
BG = RGBColor(245, 247, 250)

def add_bg(slide):
    bg = slide.background
    fill = bg.fill
    fill.solid()
    fill.fore_color.rgb = BG

def add_title_bar(slide, text):
    left = Inches(0)
    top = Inches(0)
    width = prs.slide_width
    height = Inches(1.2)
    shape = slide.shapes.add_shape(1, left, top, width, height)  # rectangle
    shape.fill.solid()
    shape.fill.fore_color.rgb = DARK_BLUE
    shape.line.fill.background()
    tf = shape.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = text
    p.font.size = Pt(32)
    p.font.bold = True
    p.font.color.rgb = WHITE
    p.alignment = PP_ALIGN.LEFT
    # Yellow accent line
    accent = slide.shapes.add_shape(1, Inches(0), Inches(1.2), prs.slide_width, Inches(0.06))
    accent.fill.solid()
    accent.fill.fore_color.rgb = YELLOW
    accent.line.fill.background()

def add_body_text(slide, text, top=1.6, left=0.8, width=11.5, size=18, bold=False, color=BLACK):
    txBox = slide.shapes.add_textbox(Inches(left), Inches(top), Inches(width), Inches(5))
    tf = txBox.text_frame
    tf.word_wrap = True
    for i, line in enumerate(text.strip().split('\n')):
        if i == 0:
            p = tf.paragraphs[0]
        else:
            p = tf.add_paragraph()
        p.text = line
        p.font.size = Pt(size)
        p.font.bold = bold
        p.font.color.rgb = color
        p.space_after = Pt(6)

def add_bullet_list(slide, items, top=1.8, left=0.8, width=11.5, size=16):
    txBox = slide.shapes.add_textbox(Inches(left), Inches(top), Inches(width), Inches(5))
    tf = txBox.text_frame
    tf.word_wrap = True
    for i, item in enumerate(items):
        if i == 0:
            p = tf.paragraphs[0]
        else:
            p = tf.add_paragraph()
        p.text = item
        p.font.size = Pt(size)
        p.font.color.rgb = BLACK
        p.space_after = Pt(8)
        p.level = 0

# ---- SLIDE 1: Title Slide ----
slide = prs.slides.add_slide(prs.slide_layouts[6])  # blank
bg = slide.background
fill = bg.fill
fill.solid()
fill.fore_color.rgb = DARK_BLUE

# Title
txBox = slide.shapes.add_textbox(Inches(1), Inches(2), Inches(11), Inches(1.5))
tf = txBox.text_frame
p = tf.paragraphs[0]
p.text = "TechTrack"
p.font.size = Pt(54)
p.font.bold = True
p.font.color.rgb = WHITE
p.alignment = PP_ALIGN.CENTER

# Subtitle
txBox2 = slide.shapes.add_textbox(Inches(1), Inches(3.5), Inches(11), Inches(1))
tf2 = txBox2.text_frame
p2 = tf2.paragraphs[0]
p2.text = "Hardware Monitoring & Maintenance Management System"
p2.font.size = Pt(24)
p2.font.color.rgb = YELLOW
p2.alignment = PP_ALIGN.CENTER

# Accent line
accent = slide.shapes.add_shape(1, Inches(4), Inches(3.3), Inches(5), Inches(0.06))
accent.fill.solid()
accent.fill.fore_color.rgb = YELLOW
accent.line.fill.background()

# Footer
txBox3 = slide.shapes.add_textbox(Inches(1), Inches(5.5), Inches(11), Inches(0.5))
tf3 = txBox3.text_frame
p3 = tf3.paragraphs[0]
p3.text = "Java Swing Desktop Application  |  OOP Architecture"
p3.font.size = Pt(16)
p3.font.color.rgb = RGBColor(180, 200, 255)
p3.alignment = PP_ALIGN.CENTER

# ---- SLIDE 2: System Overview ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(slide)
add_title_bar(slide, "  System Overview")
add_bullet_list(slide, [
    "• TechTrack is a desktop application for monitoring and managing hardware equipment",
    "• Built with Java Swing (GUI) and pure Java backend (no external database)",
    "• Tracks equipment status, maintenance schedules, repair logs, and issue reports",
    "• Role-based login system with session management",
    "• Dashboard provides real-time summary statistics from all modules",
    "• All data managed in-memory using ArrayList collections",
    "• Designed using the four pillars of OOP: Encapsulation, Inheritance, Abstraction, Polymorphism"
], top=1.8, size=17)

# ---- SLIDE 3: Architecture / Code Structure ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(slide)
add_title_bar(slide, "  Code Structure")
add_body_text(slide, "Package Organization:", top=1.5, size=20, bold=True, color=BLUE)
add_bullet_list(slide, [
    "com.mycompany.bastaewan.model/       — Data model classes (Equipment, IssueReport, RepairLog, etc.)",
    "com.mycompany.bastaewan.service/     — Business logic & data management (CRUD services)",
    "com.mycompany.bastaewan/                  — UI layer (Swing pages, frames, utilities)",
    "",
    "Key Files:",
    "  • BaseEntity.java — Abstract parent class for all models",
    "  • Manageable.java — Interface defining CRUD operations",
    "  • DataStore.java — Singleton providing access to all services",
    "  • MainDashboard.java — Main application frame with navigation",
    "  • SessionManager.java — Manages current user session state"
], top=2.2, size=15)

# ---- SLIDE 4: OOP Pillars Used ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(slide)
add_title_bar(slide, "  Four Pillars of OOP")

items = [
    "ENCAPSULATION",
    "  All model fields are private with public getters/setters",
    "  SessionManager hides session state behind static methods",
    "",
    "INHERITANCE",
    "  BaseEntity is the parent class; Equipment, IssueReport, RepairLog, MaintenanceTask, User extend it",
    "  Common properties (id, createdDate) are inherited by all models",
    "",
    "ABSTRACTION",
    "  BaseEntity is abstract — defines toTableRow() and getDisplayName() as abstract methods",
    "  Manageable<T> interface hides CRUD implementation details",
    "",
    "POLYMORPHISM",
    "  Each service implements Manageable<T> differently for its own data type",
    "  Each model overrides toTableRow() and getDisplayName() with its own logic"
]
txBox = slide.shapes.add_textbox(Inches(0.8), Inches(1.6), Inches(11.5), Inches(5.5))
tf = txBox.text_frame
tf.word_wrap = True
for i, line in enumerate(items):
    if i == 0:
        p = tf.paragraphs[0]
    else:
        p = tf.add_paragraph()
    p.text = line
    if line and not line.startswith("  "):
        p.font.size = Pt(17)
        p.font.bold = True
        p.font.color.rgb = BLUE
    else:
        p.font.size = Pt(14)
        p.font.color.rgb = BLACK
    p.space_after = Pt(3)

# ---- SLIDE 5: Features - Login & Dashboard ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(slide)
add_title_bar(slide, "  Features: Login & Dashboard")
add_bullet_list(slide, [
    "LOGIN",
    "  • Username/password authentication via AuthService",
    "  • Show/hide password toggle",
    "  • Session stored in SessionManager after successful login",
    "",
    "DASHBOARD",
    "  • 5 summary cards: Total Devices, Online, Needs Attention, Open Repairs, Avg Health",
    "  • All numbers are calculated live from service data",
    "  • Maintenance Intelligence table ranks devices by repair frequency",
    "  • Activity log shows session events"
], top=1.8, size=15)

# ---- SLIDE 6: Features - Equipment & Issues ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(slide)
add_title_bar(slide, "  Features: Equipment & Issue Reports")
add_bullet_list(slide, [
    "EQUIPMENT MANAGEMENT",
    "  • View all equipment in a searchable table",
    "  • Add new equipment (name, type, status, health score, location, category)",
    "  • Edit existing equipment details",
    "  • Delete equipment with confirmation",
    "  • Search/filter by device name, type, or status",
    "",
    "ISSUE REPORTS",
    "  • Report issues linked to registered equipment",
    "  • Set priority (Low, Medium, High, Critical)",
    "  • Change status: Open → In Progress → Resolved → Closed",
    "  • Filter by status, delete with confirmation"
], top=1.8, size=15)

# ---- SLIDE 7: Features - Repairs & Maintenance ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(slide)
add_title_bar(slide, "  Features: Repair Logs & Maintenance")
add_bullet_list(slide, [
    "REPAIR LOGS",
    "  • Log repairs for equipment (description, technician, cost)",
    "  • Track status: Pending → In Progress → Completed",
    "  • Mark as complete (auto-fills completion date)",
    "  • Filter by status, delete entries",
    "",
    "MAINTENANCE SCHEDULE",
    "  • Schedule maintenance tasks with due dates",
    "  • Status tracking: Scheduled → In Progress → Completed / Overdue",
    "  • Start and complete tasks from the table",
    "  • Filter by status, add/delete tasks"
], top=1.8, size=15)

# ---- SLIDE 8: UI Design ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(slide)
add_title_bar(slide, "  UI Design")
add_bullet_list(slide, [
    "• Blue & Yellow color theme throughout the application",
    "• Sidebar navigation with highlighted active page",
    "• Top bar with app title, welcome message, and logout button",
    "• User profile section in sidebar with popup menu (View Profile, Settings, Change Password)",
    "• CardLayout for page switching — all pages stay in memory for fast navigation",
    "• Consistent button styling: Green (Add), Blue (Edit/Action), Red (Delete), Orange (Status)",
    "• Responsive tables with row selection for actions",
    "• Modal dialogs for data entry forms",
    "• Status bar at the bottom showing version and date"
], top=1.8, size=16)

# ---- SLIDE 9: Technology Stack ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(slide)
add_title_bar(slide, "  Technology Stack")
add_bullet_list(slide, [
    "• Language: Java (JDK 25)",
    "• GUI Framework: Java Swing",
    "• Build Tool: Apache Maven",
    "• Data Storage: In-memory (ArrayList collections)",
    "• Design Pattern: Singleton (DataStore), MVC-inspired separation",
    "• IDE: NetBeans / VS Code",
    "• No external libraries or databases — pure Java fundamentals only"
], top=1.8, size=17)

# ---- SLIDE 10: Thank You ----
slide = prs.slides.add_slide(prs.slide_layouts[6])
bg = slide.background
fill = bg.fill
fill.solid()
fill.fore_color.rgb = DARK_BLUE

txBox = slide.shapes.add_textbox(Inches(1), Inches(2.5), Inches(11), Inches(1.5))
tf = txBox.text_frame
p = tf.paragraphs[0]
p.text = "Thank You"
p.font.size = Pt(48)
p.font.bold = True
p.font.color.rgb = WHITE
p.alignment = PP_ALIGN.CENTER

accent = slide.shapes.add_shape(1, Inches(4), Inches(3.8), Inches(5), Inches(0.06))
accent.fill.solid()
accent.fill.fore_color.rgb = YELLOW
accent.line.fill.background()

txBox2 = slide.shapes.add_textbox(Inches(1), Inches(4.2), Inches(11), Inches(1))
tf2 = txBox2.text_frame
p2 = tf2.paragraphs[0]
p2.text = "TechTrack — Hardware Monitoring & Maintenance System"
p2.font.size = Pt(20)
p2.font.color.rgb = RGBColor(180, 200, 255)
p2.alignment = PP_ALIGN.CENTER

# Save
output_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "TechTrack_Presentation.pptx")
prs.save(output_path)
print(f"Presentation saved to: {output_path}")
