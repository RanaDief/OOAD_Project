package model;

import java.util.*;

/** Student weekly plan (stores approved courses). */
public class Schedule {
    private final List<Course> courses = new ArrayList<>();

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public void addCourse(Course c) {
        if (c == null) return;
        if (!courses.contains(c)) courses.add(c);
    }

    public void removeCourse(Course c) {
        courses.remove(c);
    }

    public boolean hasConflict(Course candidate) {
        if (candidate == null) return false;
        for (Course c : courses) {
            if (c.conflictsWith(candidate)) return true;
        }
        return false;
    }

    public int load() {
        return courses.size();
    }
}
