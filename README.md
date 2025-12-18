# Online Course Registration System  

## 📌 Project Overview
This project is a **console-based Online Course Registration System** developed as part of the CSAI-301 course.  
It applies core **object-oriented analysis, modeling, and design principles**, translating UML-based system design into a functional Java prototype.

The system simulates course registration processes for a university environment, supporting **Students, Instructors, and Administrators**, while enforcing academic rules such as prerequisites, capacity limits, and schedule conflicts.

---

## 🎯 Project Objectives
- Apply OOP principles: **Encapsulation, Inheritance, Polymorphism, Abstraction**
- Translate UML design artifacts into working Java code
- Implement a maintainable, modular system architecture
- Simulate real-world course registration workflows
- Practice professional documentation and GitHub collaboration

---

## 👥 System Actors & Features

### Student
- Browse available courses
- Register for courses
- View registered courses
- Automatic validation:
  - Prerequisites
  - Course capacity
  - Schedule conflicts

### Instructor
- View enrolled students per course
- Approve or reject registration requests (if applicable)

### Administrator
- Add, edit, and remove:
  - Courses
  - Users
- Manage registration rules:
  - Maximum course load
  - Schedule constraints

---

## 🏗️ Project Structure
```
Phase 4/
├── out/                       # Compiled output files
├── src/
│   ├── app/                   # Application entry point
│   │   └── Main.java
│   ├── ui/                    # Console-based user interface
│   │   └── ConsoleUI.java
│   ├── service/               # Business logic & use case services
│   │   ├── AdminService.java
│   │   ├── AuthService.java
│   │   ├── CourseService.java
│   │   └── RegistrationService.java
│   ├── data/                  # Data storage & persistence layer
│   │   └── DataStore.java
│   └── model/                 # Domain models & core entities
│       ├── UserAccount.java
│       ├── Student.java
│       ├── Instructor.java
│       ├── Administrator.java
│       ├── Course.java
│       ├── Semester.java
│       ├── Schedule.java
│       ├── TimeSlot.java
│       ├── Prerequisite.java
│       ├── Registration.java
│       ├── RegistrationStatus.java
│       ├── RegistrationRules.java
│       └── Notification.java
└── README.md
```

---

## 🧩 Package Overview

- **app/**  
  Application entry point and system bootstrap.

- **ui/**  
  Console-based menu system handling all user interactions.

- **service/**  
  Core business logic implementing system use cases.

- **model/**  
  Domain entities based directly on UML design diagrams.

- **data/**  
  In-memory data storage simulating persistence.

---

## 🧠 Architecture Highlights
- Layered architecture (UI → Service → Model → Data)
- Strong adherence to OOP principles
- Direct mapping from UML to code
- Console-based prototype for Phase 4


---

## ⚙️ Technologies Used
- **Language:** Java  
- **Paradigm:** Object-Oriented Programming (OOP)  
- **Interface:** Console-based (menu-driven)  
- **Version Control:** Git & GitHub  

---

## ▶️ How to Run the Project

### Prerequisites
- Java JDK 17+ installed
- Terminal or IDE (VS Code / IntelliJ)

### Compile & Run
```bash
cd "Phase 4"
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out app.Main
```

---

## 📄 Documentation & UML
- Phase 1: Requirements & Use Case Analysis  
- Phase 2: System Analysis & UML Modeling  
- Phase 3: System Design & Detailed UML  
- Phase 4: Implementation (this repository)  

All implemented classes directly map to the Design Class Diagram, Sequence Diagrams, and State Machine Diagrams produced in earlier phases.

---

## 👨‍👩‍👧‍👦 Team Members
- Rana Dief  
- Basmala Salah  
