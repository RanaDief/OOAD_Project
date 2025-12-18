package service;

import data.DataStore;
import model.*;

import java.util.Optional;

public class AdminService {
    private final DataStore store;

    public AdminService(DataStore store) { this.store = store; }

    public Notification addUser(UserAccount u) {
        store.addUser(u);
        return new Notification("User added: " + u);
    }

    public Notification removeUserByEmail(String email) {
        Optional<UserAccount> u = store.findUserByEmail(email);
        if (u.isEmpty()) return new Notification("No user found with email: " + email);
        store.removeUserByEmail(email);
        return new Notification("User removed: " + u.get());
    }

    public Notification removeCourse(String code) {
        Optional<Course> c = store.findCourseByCode(code);
        if (c.isEmpty()) return new Notification("No course found with code: " + code);
        store.removeCourseByCode(code);
        return new Notification("Course removed: " + c.get().getCode());
    }

    public Notification setMaxLoad(int maxLoad) {
        store.getRules().setMaxLoadCourses(maxLoad);
        return new Notification("Updated max load to " + maxLoad);
    }

    public Notification setRegistrationOpen(boolean open) {
        store.getRules().setRegistrationOpen(open);
        return new Notification("Registration open set to " + open);
    }
}
