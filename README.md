# Smart Complaint Tracker System

A modern, intelligent desktop application designed to track and manage public complaints. Built using **Java 21**, **Swing**, **MySQL**, **JDBC**, and APIs (OpenWeather & Google Gemini AI), the system allows both citizens and administrators to streamline complaint handling with real-time weather checks and AI-based suggestions.

> Developed in **IntelliJ IDEA**, packaged with **Maven**, and follows the principles of **OOP**, **MVC architecture**, and **DAO pattern**.

---

## 🔹 Table of Contents

* [Project Structure](#project-structure)
* [Technologies Used](#technologies-used)
* [Maven Dependencies](#maven-dependencies)
* [Features](#features)
* [Database Schema](#database-schema)
* [Setup Instructions](#setup-instructions)
* [Screenshots](#screenshots)
* [Future Scope](#future-scope)
* [License](#license)

---

## 📁 Project Structure

```plaintext
SmartComplaintSystem
├── src
│   └── main/java/com.rishi.complaintsystem
│       ├── ai/                  # API integrations (Gemini & OpenWeather)
│       ├── dao/                 # Data Access Objects (DB operations)
│       ├── db/                  # DBConnection class
│       ├── gui/                 # Java Swing UI components
│       │   └── images/          # Weather icons & UI assets
│       ├── models/              # JavaBeans (POJOs)
│       └── utils/               # Config reader and Main class
├── resources/
│   └── config.properties        # API keys and DB configuration
├── pom.xml                      # Maven dependency file
└── README.md                    # Project documentation
```

---

## 🚀 Technologies Used

* **Java Swing**: GUI framework for desktop applications.
* **JDBC**: Direct interaction with MySQL database.
* **MySQL**: RDBMS for structured data storage.
* **OkHttp**: HTTP client for calling OpenWeather and Gemini APIs.
* **Gson**: Parsing JSON responses from APIs.
* **FlatLaf**: Stylish dark-mode look-and-feel for Swing UI.
* **OOP Concepts**: Classes, Inheritance, Encapsulation.
* **MVC Pattern**: Clean separation between model, view, and controller.
* **DAO Pattern**: Isolating database access logic.

---

## 📊 Maven Dependencies

```xml
<!-- MySQL JDBC Driver -->
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.33</version>
</dependency>

<!-- OkHttp for HTTP requests -->
<dependency>
  <groupId>com.squareup.okhttp3</groupId>
  <artifactId>okhttp</artifactId>
  <version>4.12.0</version>
</dependency>

<!-- Gson for JSON parsing -->
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.10.1</version>
</dependency>

<!-- FlatLaf for UI styling -->
<dependency>
  <groupId>com.formdev</groupId>
  <artifactId>flatlaf</artifactId>
  <version>3.2</version>
</dependency>
```

---

## 🌐 Features

### 🔑 Authentication

* User & Admin login system with role-based access
* Registration system with validation

### 📄 User Dashboard

* File new complaints with category, priority, and location
* Track complaint status (Pending, In Progress, Resolved)
* View assigned staff

### 🧑‍💼 Admin Dashboard

* View all user complaints (joined view with user details)
* Assign staff based on availability
* Real-time weather check before assignment (OpenWeather API)
* AI-based suggestions for complaint urgency (Gemini API)

### ⛅️ Weather Integration

* Visual indicators (icons like sunny, cloudy, rain) for weather status

### 🧠 AI Suggestion Dialog

* Google Gemini model gives complaint urgency insight and recommendations

---

## 📈 Database Schema

### Create Database

```sql
CREATE DATABASE smart_complaint_system_db;
USE smart_complaint_system_db;
```

### Tables

```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    role ENUM('user', 'admin'),
    password VARCHAR(100),
    contact VARCHAR(20)
);

CREATE TABLE staffs (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    contact VARCHAR(15),
    is_available TINYINT(1) DEFAULT 1
);

CREATE TABLE complaints (
    complaint_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    category VARCHAR(50),
    description TEXT,
    city VARCHAR(50),
    priority ENUM('Low', 'Normal', 'Urgent') DEFAULT 'Normal',
    status ENUM('Pending', 'In Progress', 'Resolved') DEFAULT 'Pending',
    date_submitted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE assigned_complaints (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id INT,
    staff_id INT,
    assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (complaint_id) REFERENCES complaints(complaint_id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staffs(staff_id) ON DELETE CASCADE
);
```

---

## ⚖️ Setup Instructions

### Prerequisites

* Java 21
* Maven
* IntelliJ IDEA
* MySQL Server

### Configuration

1. Clone this repository

```bash
git clone https://github.com/yourusername/SmartComplaintSystem.git
```

2. Open project in IntelliJ
3. Setup your `resources/config.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/smart_complaint_system_db
db.username=root
db.password=your_password
weather.api.key=YOUR_OPENWEATHER_API_KEY
gemini.api.key=YOUR_GOOGLE_GEMINI_API_KEY
```

4. Run the SQL script to create the schema manually or import `script.sql`
5. Run `Main.java` to start the application

---

## 📷 Screenshots

> Add UI screenshots here to show login, dashboards, complaint view, AI dialog, etc.



---

## 🚀 Future Scope

* Add password hashing
* Enable push/email notifications
* Implement live complaint updates with polling
* Add filtering/sorting in admin panel
* Add complaint analytics

---

## 🌍 License

This project is for academic use. Contact the author before using it for commercial purposes.

---

> Created with passion by **Rishi Rana**
