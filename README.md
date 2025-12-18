# OOAD_Project

File Structure:

OnlineCourseRegistrationSystem/
│
├── README.md
│
├── report/
│   ├── Phase4_Implementation_Report.pdf
│   └── screenshots/
│       ├── student_registration.png
│       ├── instructor_approval.png
│       └── admin_course_management.png
│
├── src/
│   └── main/
│       └── java/
│           └── ocrs/                     ← (Online Course Registration System)
│
│               ├── app/
│               │   ├── Main.java          ← Program entry point
│               │   └── ConsoleMenu.java   ← Menu-driven UI (Student / Instructor / Admin)
│
│               ├── user/
│               │   ├── UserAccount.java   ← Superclass
│               │   ├── Student.java
│               │   ├── Instructor.java
│               │   └── Administrator.java
│
│               ├── course/
│               │   ├── Course.java
│               │   ├── Prerequisite.java
│               │   ├── TimeSlot.java
│               │   └── Semester.java
│
│               ├── registration/
│               │   ├── Registration.java
│               │   ├── RegistrationStatus.java
│               │   └── RegistrationEngine.java
│
│               ├── schedule/
│               │   └── Schedule.java
│
│               ├── notification/
│               │   ├── Notification.java
│               │   └── NotificationService.java
│
│               ├── service/
│               │   ├── CourseManagementService.java
│               │   ├── UserManagementService.java
│               │   └── SchedulingService.java
│
│               └── util/
│                   ├── IdGenerator.java
│                   └── DateTimeUtil.java
│
└── .gitignore
