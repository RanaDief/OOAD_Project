package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Registration {
    private final Student student;
    private final Course course;
    private RegistrationStatus status;
    private final LocalDateTime createdAt;

    public Registration(Student student, Course course) {
        this.student = Objects.requireNonNull(student);
        this.course = Objects.requireNonNull(course);
        this.status = RegistrationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public RegistrationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void approve() { status = RegistrationStatus.APPROVED; }
    public void reject() { status = RegistrationStatus.REJECTED; }

    @Override
    public String toString() {
        return "Registration{" + student.getUserId() + " -> " + course.getCode() + ", " + status + "}";
    }
}
