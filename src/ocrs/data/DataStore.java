package ocrs.data;

import ocrs.model.*;

import java.time.DayOfWeek;
import java.util.*;

/**
 * In-memory data store (Phase 4 prototype).
 * In a real system this would be replaced by DB repositories.
 */
public class DataStore {
    private final Map<String, UserAccount> usersByEmail = new HashMap<>();
    private final Map<String, Course> coursesByCode = new HashMap<>();
    private final RegistrationRules rules = new RegistrationRules();

    public RegistrationRules getRules() { return rules; }

    public Collection<UserAccount> allUsers() { return Collections.unmodifiableCollection(usersByEmail.values()); }
    public Collection<Course> allCourses() { return Collections.unmodifiableCollection(coursesByCode.values()); }

    public Optional<UserAccount> findUserByEmail(String email) {
        if (email == null) return Optional.empty();
        return Optional.ofNullable(usersByEmail.get(email.toLowerCase()));
    }

    public void addUser(UserAccount u) {
        usersByEmail.put(u.getEmail(), u);
    }

    public void removeUserByEmail(String email) {
        if (email != null) usersByEmail.remove(email.toLowerCase());
    }

    public Optional<Course> findCourseByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(coursesByCode.get(code.trim().toUpperCase()));
    }

    public void addCourse(Course c) {
        coursesByCode.put(c.getCode(), c);
    }

    public void removeCourseByCode(String code) {
        if (code != null) coursesByCode.remove(code.trim().toUpperCase());
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
