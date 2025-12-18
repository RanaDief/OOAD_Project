package service;

import data.DataStore;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistrationService {
    private final DataStore store;

    public RegistrationService(DataStore store) {
        this.store = store;
    }

    /** Student requests registration -> creates PENDING registration (then instructor can approve). */
    public Notification requestRegistration(Student student, Course course) {
        if (!store.getRules().isRegistrationOpen()) {
            return new Notification("Registration period is CLOSED.");
        }

        // Already registered?
        Optional<Registration> existing = course.findRegistrationFor(student);
        if (existing.isPresent()) {
            return new Notification("You already have a registration for this course: " + existing.get().getStatus());
        }

        // Max load (based on approved courses in schedule)
        if (student.getSchedule().load() >= store.getRules().getMaxLoadCourses()) {
            return new Notification("Cannot register: max course load reached (" + store.getRules().getMaxLoadCourses() + ").");
        }

        // Prerequisites
        for (Prerequisite p : course.getPrerequisites()) {
            if (!student.hasCompleted(p.getRequiredCourseCode())) {
                return new Notification("Missing prerequisite: " + p.getRequiredCourseCode());
            }
        }

        // Schedule conflict with student's current APPROVED courses
        if (student.getSchedule().hasConflict(course)) {
            return new Notification("Time conflict detected with your current schedule.");
        }

        // Capacity check (using approved count; pending doesn't occupy seat yet)
        if (course.isFull()) {
            return new Notification("Course is FULL.");
        }

        Registration reg = new Registration(student, course);
        course.addRegistration(reg);
        return new Notification("Registration request submitted (PENDING). Wait for instructor approval.");
    }

    public Notification approveRegistration(Instructor instructor, Course course, String studentId) {
        if (course.getInstructor() == null || !course.getInstructor().getEmail().equals(instructor.getEmail())) {
            return new Notification("Denied: You are not the instructor for this course.");
        }
        if (course.isFull()) {
            return new Notification("Cannot approve: course is already full.");
        }

        for (Registration r : course.getRegistrations()) {
            if (r.getStudent().getUserId().equalsIgnoreCase(studentId) && r.getStatus() == RegistrationStatus.PENDING) {
                // Final conflict check (in case student changed schedule after request)
                if (r.getStudent().getSchedule().hasConflict(course)) {
                    r.reject();
                    return new Notification("Rejected due to time conflict at approval time.");
                }
                r.approve();
                r.getStudent().getSchedule().addCourse(course);
                return new Notification("Approved: " + r.getStudent().getName() + " is now enrolled in " + course.getCode());
            }
        }
        return new Notification("No matching PENDING request found for student ID: " + studentId);
    }

    public Notification rejectRegistration(Instructor instructor, Course course, String studentId) {
        if (course.getInstructor() == null || !course.getInstructor().getEmail().equals(instructor.getEmail())) {
            return new Notification("Denied: You are not the instructor for this course.");
        }
        for (Registration r : course.getRegistrations()) {
            if (r.getStudent().getUserId().equalsIgnoreCase(studentId) && r.getStatus() == RegistrationStatus.PENDING) {
                r.reject();
                return new Notification("Rejected: " + r.getStudent().getName() + " request for " + course.getCode());
            }
        }
        return new Notification("No matching PENDING request found for student ID: " + studentId);
    }

    public List<Registration> pendingRequestsForCourse(Course course) {
        List<Registration> out = new ArrayList<>();
        for (Registration r : course.getRegistrations()) {
            if (r.getStatus() == RegistrationStatus.PENDING) out.add(r);
        }
        return out;
    }

    public List<Student> approvedStudentsForCourse(Course course) {
        List<Student> out = new ArrayList<>();
        for (Registration r : course.getRegistrations()) {
            if (r.getStatus() == RegistrationStatus.APPROVED) out.add(r.getStudent());
        }
        return out;
    }

    public Notification dropCourse(Student student, Course course) {
        if (!store.getRules().isRegistrationOpen()) {
            return new Notification("Add/Drop period is CLOSED.");
        }

        Optional<Registration> regOpt = course.findRegistrationFor(student);
        if (regOpt.isEmpty()) {
            return new Notification("You are not registered in this course.");
        }

        Registration reg = regOpt.get();
        if (reg.getStatus() == RegistrationStatus.PENDING) {
            // remove pending request
            course.removeRegistration(reg);
            return new Notification("Pending request removed for " + course.getCode());
        } else if (reg.getStatus() == RegistrationStatus.APPROVED) {
            course.removeRegistration(reg);
            student.getSchedule().removeCourse(course);
            return new Notification("Dropped course: " + course.getCode());
        } else {
            return new Notification("Cannot drop: registration status is REJECTED.");
        }
    }
}
