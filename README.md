# 🎓 College Feedback System

**🚀 Live Demo:** [https://feedback-system-w0gj.onrender.com](https://feedback-system-w0gj.onrender.com)

A full-stack, enterprise-grade Feedback Management System built with Java Spring Boot, Thymeleaf, and MySQL. It features a modern, responsive Glassmorphism UI and a robust Role-Based Access Control (RBAC) architecture for processing academic feedback.

---

## 🌟 Key Features

*   **Role-Based Access Control (RBAC):** Distinct portals and capabilities for Admin, Faculty, and Students.
*   **Modern Glassmorphism UI:** A sleek, premium dark-mode aesthetic with frosted glass elements and dynamic micro-animations.
*   **Voice-to-Text Integration:** Students can dictate their qualitative feedback using the integrated Web Speech API.
*   **Dynamic Data Visualizations:** Faculty can view real-time feedback ratings aggregated and visualized via interactive Chart.js graphs.
*   **NLP Summarization Ready:** The database architecture includes fields designed to support automated NLP-based summarization of qualitative feedback (`feedback_comment` table).
*   **Bulk Student Uploads:** Admins can quickly onboard entire sections by uploading a CSV file containing student credentials.

---

## 👥 User Roles & Permissions

### 1. 🛡️ Administrator (`/admin/dashboard`)
The core orchestrator of the platform.
*   **Manage Courses & Departments:** Add departments, courses, and sections.
*   **Faculty Onboarding:** Add new faculty members directly via the Quick Actions dashboard.
*   **Course Assignments:** Assign a faculty member to teach a specific course in a specific section.
*   **Manage Questionnaire:** Dynamically build and edit the rating questions that students will answer (e.g., "How would you rate the teaching style?"). 
*   **Bulk Actions:** Upload a CSV to bulk-add students or instantly wipe all student data at the end of an academic year.

### 2. 👩‍🏫 Faculty (`/faculty/dashboard`)
The reviewers of the feedback.
*   **Real-time Analytics:** View average ratings across their assigned courses plotted on a dynamic bar chart.
*   **Detailed Responses:** Drill down into specific courses to read the individual written (or voice-dictated) comments left by students.

### 3. 👨‍🎓 Student (`/student/dashboard`)
The providers of the feedback.
*   **Course Selection:** Select their currently enrolled courses to submit feedback. (A student can only submit feedback for a specific course *once*).
*   **Star Ratings:** Intuitive 5-star rating scale for each question configured by the Admin.
*   **Voice Dictation:** A built-in microphone feature allowing students to easily speak their qualitative feedback instead of typing.

---

## 🛠️ Technology Stack

*   **Backend:** Java 21, Spring Boot 3.2.5, Spring Security, Spring Data JPA
*   **Frontend:** HTML5, CSS3 (Vanilla + Glassmorphism Theme), Thymeleaf
*   **Database:** MySQL, HikariCP Connection Pooling
*   **Libraries:** Chart.js, FontAwesome

---

## 🚀 Running the Project Locally

### Prerequisites
*   Java Development Kit (JDK) 21
*   Maven 3.9+
*   MySQL Server (running locally on port 3306)

### 1. Database Setup
1. Open your MySQL client (e.g., MySQL Workbench).
2. Execute the included SQL dump file to create the database schema:
   ```bash
   mysql -u root -p < database_dump/initial_schema_and_data.sql
   ```
   *(Note: This dump contains the required tables, default departments, and a master admin user. It contains NO dummy data for students or faculty).*

### 2. Configure Application Properties
Ensure your MySQL credentials are correct in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/feedback_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build and Run
Navigate to the project root and start the application:
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Access the Application
Open your browser and navigate to:
```
http://localhost:8080/login
```

**Default Admin Credentials:**
*   **Username:** admin
*   **Password:** admin

---

## 🌐 Live Deployment
The application is currently hosted and live at:
**[https://feedback-system-w0gj.onrender.com](https://feedback-system-w0gj.onrender.com)**
