package ocrs.model;

import java.util.*;

public class Student extends UserAccount {
    // completed courses (by code) for prerequisite validation
    private final Set<String> completedCourseCodes = new HashSet<>();
    // composition: student owns their schedule
    private final Schedule schedule = new Schedule();

    public Student(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public String role() { return "STUDENT"; }

    public Schedule getSchedule() { return schedule; }

    public Set<String> getCompletedCourseCodes() {
        return Collections.unmodifiableSet(completedCourseCodes);
    }

    public void markCompleted(String courseCode) {
        if (courseCode != null) completedCourseCodes.add(courseCode.trim().toUpperCase());
    }

    public boolean hasCompleted(String courseCode) {
        if (courseCode == null) return false;
        return completedCourseCodes.contains(courseCode.trim().toUpperCase());
    }
}
