package ocrs.service;

import ocrs.data.DataStore;
import ocrs.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseService {
    private final DataStore store;

    public CourseService(DataStore store) {
        this.store = store;
    }

    public List<Course> listAllCourses() {
        return new ArrayList<>(store.allCourses());
    }

    public Optional<Course> findByCode(String code) {
        return store.findCourseByCode(code);
    }

    public void addCourse(Course c) { store.addCourse(c); }
    public void removeCourse(String code) { store.removeCourseByCode(code); }
}
