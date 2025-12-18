package ocrs.ui;

import ocrs.data.DataStore;
import ocrs.model.*;
import ocrs.service.*;

import java.time.DayOfWeek;
import java.util.*;

public class ConsoleUI {
    private final DataStore store;
    private final AuthService auth;
    private final CourseService courses;
    private final RegistrationService regs;
    private final AdminService admin;

    private final Scanner in = new Scanner(System.in);

    public ConsoleUI(DataStore store) {
        this.store = store;
        this.auth = new AuthService(store);
        this.courses = new CourseService(store);
        this.regs = new RegistrationService(store);
        this.admin = new AdminService(store);
    }

    public void run() {
        System.out.println("=== Online Course Registration System (OCRS) ===");
        while (true) {
            UserAccount user = loginScreen();
            if (user == null) break;

            switch (user.role()) {
                case "STUDENT" -> studentMenu((Student) user);
                case "INSTRUCTOR" -> instructorMenu((Instructor) user);
                case "ADMIN" -> adminMenu((Administrator) user);
                default -> System.out.println("Unknown role.");
            }
        }
        System.out.println("Bye!");
    }

    private UserAccount loginScreen() {
        System.out.println("\n1) Login");
        System.out.println("0) Exit");
        System.out.print("> ");
        String c = in.nextLine().trim();
        if (c.equals("0")) return null;

        System.out.print("Email: ");
        String email = in.nextLine().trim();
        System.out.print("Password: ");
        String pass = in.nextLine().trim();

        Optional<UserAccount> u = auth.login(email, pass);
        if (u.isEmpty()) {
            System.out.println("Login failed.");
            return null;
        }
        System.out.println("Logged in as: " + u.get());
        return u.get();
    }

    // -------------------- Student --------------------
    private void studentMenu(Student s) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("Rules: " + store.getRules());
            System.out.println("1) Browse courses");
            System.out.println("2) Register for a course (request)");
            System.out.println("3) View my registered courses (approved)");
            System.out.println("4) Drop a course");
            System.out.println("0) Logout");
            System.out.print("> ");
            String c = in.nextLine().trim();

            switch (c) {
                case "1" -> browseCourses();
                case "2" -> studentRegister(s);
                case "3" -> viewMySchedule(s);
                case "4" -> studentDrop(s);
                case "0" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private void browseCourses() {
        System.out.println("\nAvailable Courses:");
        for (Course c : courses.listAllCourses()) {
            System.out.println(" - " + c);
            System.out.println("   Times: " + c.getTimeSlots());
            System.out.println("   Prereqs: " + c.getPrerequisites());
        }
    }

    private void studentRegister(Student s) {
        System.out.print("Enter course code (e.g., CS101): ");
        String code = in.nextLine().trim();
        Optional<Course> c = courses.findByCode(code);
        if (c.isEmpty()) {
            System.out.println("Course not found.");
            return;
        }
        Notification n = regs.requestRegistration(s, c.get());
        System.out.println(n.getMessage());
    }

    private void viewMySchedule(Student s) {
        System.out.println("\nMy Approved Courses:");
        if (s.getSchedule().getCourses().isEmpty()) {
            System.out.println(" - (none)");
            return;
        }
        for (Course c : s.getSchedule().getCourses()) {
            System.out.println(" - " + c.getCode() + ": " + c.getTitle() + " | " + c.getTimeSlots());
        }
    }

    private void studentDrop(Student s) {
        System.out.print("Enter course code to drop: ");
        String code = in.nextLine().trim();
        Optional<Course> c = courses.findByCode(code);
        if (c.isEmpty()) {
            System.out.println("Course not found.");
            return;
        }
        Notification n = regs.dropCourse(s, c.get());
        System.out.println(n.getMessage());
    }

    // -------------------- Instructor --------------------
    private void instructorMenu(Instructor i) {
        while (true) {
            System.out.println("\n--- Instructor Menu ---");
            System.out.println("1) View my courses");
            System.out.println("2) View pending requests for a course");
            System.out.println("3) Approve request");
            System.out.println("4) Reject request");
            System.out.println("5) View enrolled students for a course");
            System.out.println("0) Logout");
            System.out.print("> ");
            String c = in.nextLine().trim();

            switch (c) {
                case "1" -> viewInstructorCourses(i);
                case "2" -> viewPending(i);
                case "3" -> approve(i);
                case "4" -> reject(i);
                case "5" -> viewEnrolled(i);
                case "0" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private List<Course> instructorCourses(Instructor i) {
        List<Course> out = new ArrayList<>();
        for (Course c : store.allCourses()) {
            if (c.getInstructor() != null && c.getInstructor().getEmail().equals(i.getEmail())) out.add(c);
        }
        return out;
    }

    private void viewInstructorCourses(Instructor i) {
        System.out.println("\nMy Courses:");
        for (Course c : instructorCourses(i)) {
            System.out.println(" - " + c + " | Times: " + c.getTimeSlots());
        }
    }

    private Optional<Course> pickInstructorCourse(Instructor i) {
        System.out.print("Course code: ");
        String code = in.nextLine().trim();
        Optional<Course> c = courses.findByCode(code);
        if (c.isEmpty()) {
            System.out.println("Course not found.");
            return Optional.empty();
        }
        if (c.get().getInstructor() == null || !c.get().getInstructor().getEmail().equals(i.getEmail())) {
            System.out.println("Denied: Not your course.");
            return Optional.empty();
        }
        return c;
    }

    private void viewPending(Instructor i) {
        Optional<Course> c = pickInstructorCourse(i);
        if (c.isEmpty()) return;

        List<Registration> pending = regs.pendingRequestsForCourse(c.get());
        System.out.println("\nPending Requests for " + c.get().getCode() + ":");
        if (pending.isEmpty()) {
            System.out.println(" - (none)");
            return;
        }
        for (Registration r : pending) {
            System.out.println(" - " + r.getStudent().getUserId() + " | " + r.getStudent().getName()
                    + " | " + r.getStudent().getEmail() + " | " + r.getStatus());
        }
    }

    private void approve(Instructor i) {
        Optional<Course> c = pickInstructorCourse(i);
        if (c.isEmpty()) return;

        System.out.print("Student ID to approve: ");
        String sid = in.nextLine().trim();
        Notification n = regs.approveRegistration(i, c.get(), sid);
        System.out.println(n.getMessage());
    }

    private void reject(Instructor i) {
        Optional<Course> c = pickInstructorCourse(i);
        if (c.isEmpty()) return;

        System.out.print("Student ID to reject: ");
        String sid = in.nextLine().trim();
        Notification n = regs.rejectRegistration(i, c.get(), sid);
        System.out.println(n.getMessage());
    }

    private void viewEnrolled(Instructor i) {
        Optional<Course> c = pickInstructorCourse(i);
        if (c.isEmpty()) return;

        List<Student> enrolled = regs.approvedStudentsForCourse(c.get());
        System.out.println("\nEnrolled Students in " + c.get().getCode() + ":");
        if (enrolled.isEmpty()) {
            System.out.println(" - (none)");
            return;
        }
        for (Student s : enrolled) {
            System.out.println(" - " + s.getUserId() + " | " + s.getName() + " | " + s.getEmail());
        }
    }

    // -------------------- Admin --------------------
    private void adminMenu(Administrator a) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("Rules: " + store.getRules());
            System.out.println("1) List all users");
            System.out.println("2) Add user");
            System.out.println("3) Remove user");
            System.out.println("4) List all courses");
            System.out.println("5) Add course");
            System.out.println("6) Remove course");
            System.out.println("7) Set max load");
            System.out.println("8) Toggle registration open/closed");
            System.out.println("0) Logout");
            System.out.print("> ");
            String c = in.nextLine().trim();

            switch (c) {
                case "1" -> listUsers();
                case "2" -> addUser();
                case "3" -> removeUser();
                case "4" -> browseCourses();
                case "5" -> addCourse();
                case "6" -> removeCourse();
                case "7" -> setMaxLoad();
                case "8" -> toggleRegistration();
                case "0" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private void listUsers() {
        System.out.println("\nUsers:");
        for (UserAccount u : store.allUsers()) System.out.println(" - " + u);
    }

    private void addUser() {
        System.out.println("Choose role: 1=Student, 2=Instructor, 3=Admin");
        System.out.print("> ");
        String r = in.nextLine().trim();

        System.out.print("User ID: ");
        String id = in.nextLine().trim();
        System.out.print("Name: ");
        String name = in.nextLine().trim();
        System.out.print("Email: ");
        String email = in.nextLine().trim();
        System.out.print("Password: ");
        String pass = in.nextLine().trim();

        UserAccount u;
        switch (r) {
            case "1" -> u = new Student(id, name, email, pass);
            case "2" -> u = new Instructor(id, name, email, pass);
            case "3" -> u = new Administrator(id, name, email, pass);
            default -> {
                System.out.println("Invalid role.");
                return;
            }
        }
        System.out.println(admin.addUser(u).getMessage());
    }

    private void removeUser() {
        System.out.print("Email to remove: ");
        String email = in.nextLine().trim();
        System.out.println(admin.removeUserByEmail(email).getMessage());
    }

    private void addCourse() {
        System.out.print("Course code (e.g., CS201): ");
        String code = in.nextLine().trim();

        System.out.print("Title: ");
        String title = in.nextLine().trim();

        System.out.print("Department: ");
        String dept = in.nextLine().trim();

        System.out.print("Level (int): ");
        int level = readInt();

        System.out.print("Capacity (int): ");
        int cap = readInt();

        // Pick instructor by email (must exist)
        System.out.print("Instructor email (must exist): ");
        String instrEmail = in.nextLine().trim().toLowerCase();
        Optional<UserAccount> u = store.findUserByEmail(instrEmail);
        if (u.isEmpty() || !(u.get() instanceof Instructor)) {
            System.out.println("Invalid instructor email.");
            return;
        }
        Instructor instr = (Instructor) u.get();

        System.out.print("Semester name (e.g., Fall 2025): ");
        String semName = in.nextLine().trim();
        Semester sem = new Semester(semName);

        Course c = new Course(code, title, dept, level, cap, instr, sem);

        // Add 1 time slot (minimum) for the prototype
        System.out.println("Add a TimeSlot:");
        DayOfWeek day = readDayOfWeek();
        System.out.print("Start hour (0-23): ");
        int sh = readInt();
        System.out.print("Start minute (0-59): ");
        int sm = readInt();
        System.out.print("End hour (0-23): ");
        int eh = readInt();
        System.out.print("End minute (0-59): ");
        int em = readInt();
        c.addTimeSlot(new TimeSlot(day, sh * 60 + sm, eh * 60 + em));

        // Optional prerequisite
        System.out.print("Prerequisite course code (or blank): ");
        String pre = in.nextLine().trim();
        if (!pre.isEmpty()) c.addPrerequisite(new Prerequisite(pre));

        store.addCourse(c);
        System.out.println("Course added: " + c);
    }

    private void removeCourse() {
        System.out.print("Course code to remove: ");
        String code = in.nextLine().trim();
        System.out.println(admin.removeCourse(code).getMessage());
    }

    private void setMaxLoad() {
        System.out.print("New max load (courses): ");
        int ml = readInt();
        System.out.println(admin.setMaxLoad(ml).getMessage());
    }

    private void toggleRegistration() {
        boolean open = store.getRules().isRegistrationOpen();
        System.out.println(admin.setRegistrationOpen(!open).getMessage());
    }

    // -------------------- Helpers --------------------
    private int readInt() {
        while (true) {
            try {
                System.out.print("> ");
                return Integer.parseInt(in.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private DayOfWeek readDayOfWeek() {
        while (true) {
            System.out.println("Day: 1=MON 2=TUE 3=WED 4=THU 5=FRI 6=SAT 7=SUN");
            System.out.print("> ");
            String s = in.nextLine().trim();
            switch (s) {
                case "1" -> { return DayOfWeek.MONDAY; }
                case "2" -> { return DayOfWeek.TUESDAY; }
                case "3" -> { return DayOfWeek.WEDNESDAY; }
                case "4" -> { return DayOfWeek.THURSDAY; }
                case "5" -> { return DayOfWeek.FRIDAY; }
                case "6" -> { return DayOfWeek.SATURDAY; }
                case "7" -> { return DayOfWeek.SUNDAY; }
                default -> System.out.println("Invalid day.");
            }
        }
    }
}
