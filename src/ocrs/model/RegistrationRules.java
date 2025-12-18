package ocrs.model;

public class RegistrationRules {
    private boolean registrationOpen = true;
    private int maxLoadCourses = 5;

    public boolean isRegistrationOpen() { return registrationOpen; }
    public void setRegistrationOpen(boolean registrationOpen) { this.registrationOpen = registrationOpen; }

    public int getMaxLoadCourses() { return maxLoadCourses; }
    public void setMaxLoadCourses(int maxLoadCourses) {
        if (maxLoadCourses < 1) throw new IllegalArgumentException("maxLoad must be >= 1");
        this.maxLoadCourses = maxLoadCourses;
    }

    @Override
    public String toString() {
        return "Rules{open=" + registrationOpen + ", maxLoad=" + maxLoadCourses + "}";
    }
}
