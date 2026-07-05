# 🎓 College Feedback System

![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Java 21](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white)

A comprehensive, robust, and secure web-based application designed to streamline the academic feedback process for colleges and universities.

## 🚀 Live Demo

**Deployed Application URL:** [Waiting for link...]()

## 📖 About The Project

The College Feedback System bridges the gap between students, faculty, and administration by providing a structured platform for course and instructor evaluation. It eliminates the hassle of paper-based feedback and replaces it with an intuitive, automated digital solution.

### ✨ Key Features
- **🔐 Role-Based Access Control:** Distinct dashboards and permissions for Admins, Faculty, and Students (Secured with Spring Security & BCrypt).
- **👨‍🎓 Student Portal:** Students can view their enrolled courses, assigned faculty, and seamlessly submit ratings and voice/text feedback.
- **👨‍🏫 Faculty Dashboard:** Instructors get access to real-time analytics, average ratings, and anonymous student comments.
- **⚙️ Admin Management:** Administrators can easily add courses, assign faculty to specific sections, manage questions, and bulk-upload student data via CSV.
- **🛡️ Secure:** Built-in protection against CSRF attacks and automated password hashing on startup.

## 🛠️ Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites
- [Java 21](https://www.oracle.com/java/technologies/downloads/#java21) or higher
- [Maven](https://maven.apache.org/download.cgi)
- [MySQL](https://dev.mysql.com/downloads/mysql/) Server

### 📦 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/rohith-sheregar/Feedback-System.git
   cd Feedback-System
   ```

2. **Configure the Database**
   Create a new MySQL database named `feedback_db`.
   ```sql
   CREATE DATABASE feedback_db;
   ```
   *Optional:* Import the provided `database_dump/Dump20250518.sql` to populate the database with test data.

3. **Environment Variables**
   By default, the application looks for a local MySQL instance. To override this for deployment or custom local setups, set the following environment variables:
   - `DB_URL`: JDBC URL (e.g., `jdbc:mysql://localhost:3306/feedback_db`)
   - `DB_USERNAME`: Database username
   - `DB_PASSWORD`: Database password

4. **Run the Application**
   Use the Maven wrapper to start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(On Windows, you can use `mvnw.cmd spring-boot:run`)*

5. **Access the Application**
   Open your browser and navigate to: `http://localhost:8080`

---
*Built with ❤️ for better academic experiences.*
