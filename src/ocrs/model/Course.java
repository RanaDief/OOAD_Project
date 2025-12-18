package ocrs.model;

import java.util.*;

public class Course {
    private final String code; // unique, e.g., "CS101"
    private String title;
    private String department;
    private int level;
    private int capacity;

    private Instructor instructor;
    private Semester semester;

    // aggregation: course has 1..* time slots
    private final List<TimeSlot> timeSlots = new ArrayList<>();

    // prerequisites
    private final List<Prerequisite> prerequisites = new ArrayList<>();

    // registrations (pending/approved/rejected)
    private final List<Registration> registrations = new ArrayList<>();

    public Course(String code, String title, String department, int level, int capacity,
                  Instructor instructor, Semester semester) {
        this.code = Objects.requireNonNull(code).trim().toUpperCase();
        this.title = Objects.requireNonNull(title);
        this.department = Objects.requireNonNullElse(department, "");
        this.level = level;
        this.capacity = capacity;
        this.instructor = instructor;
        this.semester = semester;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public String getDepartment() { return department; }
    public int getLevel() { return level; }
    public int getCapacity() { return capacity; }
    public Instructor getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }

    public void setTitle(String title) { this.title = Objects.requireNonNull(title); }
    public void setDepartment(String department) { this.department = Objects.requireNonNullElse(department, ""); }
    public void setLevel(int level) { this.level = level; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
    public void setSemester(Semester semester) { this.semester = semester; }

    public List<TimeSlot> getTimeSlots() { return Collections.unmodifiableList(timeSlots); }
    public List<Prerequisite> getPrerequisites() { return Collections.unmodifiableList(prerequisites); }
    public List<Registration> getRegistrations() { return Collections.unmodifiableList(registrations); }

    public void addTimeSlot(TimeSlot ts) { if (ts != null) timeSlots.add(ts); }
    public void addPrerequisite(Prerequisite p) { if (p != null) prerequisites.add(p); }

    public int approvedCount() {
        int c = 0;
        for (Registration r : registrations) if (r.getStatus() == RegistrationStatus.APPROVED) c++;
        return c;
    }

    public boolean isFull() {
        return approvedCount() >= capacity;
    }

    public boolean conflictsWith(Course other) {
        if (other == null) return false;
        for (TimeSlot a : this.timeSlots) {
            for (TimeSlot b : other.timeSlots) {
                if (a.conflictsWith(b)) return true;
            }
        }
        return false;
    }

    public Optional<Registration> findRegistrationFor(Student s) {
        for (Registration r : registrations) {
            if (r.getStudent().equals(s)) return Optional.of(r);
        }
        return Optional.empty();
    }

    public void addRegistration(Registration r) {
        if (r == null) return;
        registrations.add(r);
    }

    public void removeRegistration(Registration r) {
        registrations.remove(r);
    }

    @Override
    public String toString() {
        return code + " - " + title + " (" + (semester != null ? semester : "N/A") + "), "
                + "Cap: " + approvedCount() + "/" + capacity
                + (instructor != null ? ", Instr: " + instructor.getName() : "");
    }
}
