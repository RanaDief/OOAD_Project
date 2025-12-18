package ocrs.model;

import java.util.Objects;

/** A prerequisite requirement for a course (simple: required course code). */
public class Prerequisite {
    private final String requiredCourseCode;

    public Prerequisite(String requiredCourseCode) {
        this.requiredCourseCode = Objects.requireNonNull(requiredCourseCode).trim();
        if (this.requiredCourseCode.isEmpty()) throw new IllegalArgumentException("Empty course code");
    }

    public String getRequiredCourseCode() { return requiredCourseCode; }

    @Override
    public String toString() {
        return requiredCourseCode;
    }
}
