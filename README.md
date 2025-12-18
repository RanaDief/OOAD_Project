# Online Course Registration System (OCRS) — Phase 4 (Console Prototype)

## Overview
This is a minimal yet functional **console-based** prototype for an Online Course Registration System.
It demonstrates the core logic required in Phase 4:
- **Students** browse courses, request registration, view registered courses, and drop courses.
- **Instructors** approve/reject registration requests and view enrolled students.
- **Administrators** manage courses/users and configure registration rules (e.g., max load, open/closed).

The implementation follows OO principles using:
- Inheritance: `UserAccount -> Student / Instructor / Administrator`
- Associations: `Course` holds `TimeSlot`, `Prerequisite`, and registration lists; `Student` holds `Schedule`
- A separate `Registration` entity to model many-to-many enrollment cleanly.
##File Structure
OOAD_Project/
├── README.md
└── src/
    └── ocrs/
        ├── app/
        │   └── Main.java
        │
        ├── data/
        │   └── DataStore.java
        │
        ├── model/
        │   ├── Administrator.java
        │   ├── Course.java
        │   ├── Instructor.java
        │   ├── Notification.java
        │   ├── Prerequisite.java
        │   ├── Registration.java
        │   ├── RegistrationRules.java
        │   ├── RegistrationStatus.java
        │   ├── Schedule.java
        │   ├── Semester.java
        │   ├── Student.java
        │   ├── TimeSlot.java
        │   └── UserAccount.java
        │
        ├── service/
        │   ├── AdminService.java
        │   ├── AuthService.java
        │   ├── CourseService.java
        │   └── RegistrationService.java
        │
        └── ui/
            └── ConsoleUI.java

## Setup / Run
### Requirements
- Java 17+ (works on Java 11+ as well)

### Compile & Run
From the project root:
```bash
javac -d out $(find src -name "*.java")
java -cp out ocrs.app.Main
```

## Demo Accounts (seeded in code)
- Admin: `admin@uni.edu` / `admin123`
- Instructor: `dr.sara@uni.edu` / `instr123`
- Student: `rana@student.edu` / `stud123`

## Team Members & Contributions
- Basmala Salah Mohamed — analysis/modeling + UML (Phases 1–3)
- Rana Abdelhamid Dief — implementation (Phase 4 prototype) + console UI

> Update these names/roles as needed to match your team.

