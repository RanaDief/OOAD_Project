package data;

import java.time.DayOfWeek;
import java.util.*;
import model.*;

//In-memory data store

public class DataStore {
    private final List<UserAccount> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final RegistrationRules rules = new RegistrationRules();

    public RegistrationRules getRules() { return rules; }

    public Collection<UserAccount> allUsers() { return Collections.unmodifiableList(users); }
    public Collection<Course> allCourses() { return Collections.unmodifiableList(courses); }

    public Optional<UserAccount> findUserByEmail(String email) {
        if (email == null) return Optional.empty();
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public void addUser(UserAccount u) {
        removeUserByEmail(u.getEmail());
        users.add(u);
    }

    public void removeUserByEmail(String email) {
        if (email == null) return;
        users.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public Optional<Course> findCourseByCode(String code) {
        if (code == null) return Optional.empty();
        String normalized = code.trim().toUpperCase();
        return courses.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(normalized))
                .findFirst();
    }

    public void addCourse(Course c) {
        removeCourseByCode(c.getCode());
        courses.add(c);
    }

    public void removeCourseByCode(String code) {
        if (code == null) return;
        String normalized = code.trim().toUpperCase();
        courses.removeIf(c -> c.getCode().equalsIgnoreCase(normalized));
    }

    /** Demo seed aligned with OCRS requirements (prereqs, capacity, conflicts). */
    public static DataStore seedDemoData() {
        DataStore store = new DataStore();

        Administrator admin = new Administrator("A001", "System Admin", "admin@uni.edu", "admin123");
        Instructor instr = new Instructor("I001", "Dr. Sara", "dr.sara@uni.edu", "instr123");
        Student student = new Student("S001", "Rana", "rana@student.edu", "stud123");

        // Student has completed CS100 to satisfy CS101 prereq demo
        student.markCompleted("CS100");

        store.addUser(admin);
        store.addUser(instr);
        store.addUser(student);

        Semester fall = new Semester("Fall 2025");

        Course cs100 = new Course("CS100", "Intro to Programming", "CS", 1, 2, instr, fall);
        cs100.addTimeSlot(new TimeSlot(DayOfWeek.SUNDAY, 9 * 60, 11 * 60));

        Course cs101 = new Course("CS101", "Data Structures", "CS", 2, 2, instr, fall);
        cs101.addPrerequisite(new Prerequisite("CS100"));
        cs101.addTimeSlot(new TimeSlot(DayOfWeek.SUNDAY, 11 * 60, 13 * 60));

        // Another course with time conflict with CS101 (Sunday 12-14)
        Course cs102 = new Course("CS102", "Discrete Math", "CS", 2, 2, instr, fall);
        cs102.addTimeSlot(new TimeSlot(DayOfWeek.SUNDAY, 12 * 60, 14 * 60));

        store.addCourse(cs100);
        store.addCourse(cs101);
        store.addCourse(cs102);

        // Example: pre-approve student into CS100 so schedule is non-empty
        Registration r0 = new Registration(student, cs100);
        r0.approve();
        cs100.addRegistration(r0);
        student.getSchedule().addCourse(cs100);

        return store;
    }
}
