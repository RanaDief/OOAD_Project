package model;

import java.time.DayOfWeek;
import java.util.Objects;

/** Represents a course meeting time (e.g., MONDAY 08:00-10:00). */
public class TimeSlot {
    private final DayOfWeek day;
    private final int startMinutes; // minutes from 00:00
    private final int endMinutes;   // exclusive

    public TimeSlot(DayOfWeek day, int startMinutes, int endMinutes) {
        if (endMinutes <= startMinutes) throw new IllegalArgumentException("End must be after start");
        this.day = Objects.requireNonNull(day);
        this.startMinutes = startMinutes;
        this.endMinutes = endMinutes;
    }

    public DayOfWeek getDay() { return day; }
    public int getStartMinutes() { return startMinutes; }
    public int getEndMinutes() { return endMinutes; }

    public boolean conflictsWith(TimeSlot other) {
        if (other == null) return false;
        if (this.day != other.day) return false;
        // overlap: [a,b) intersects [c,d)
        return this.startMinutes < other.endMinutes && other.startMinutes < this.endMinutes;
    }

    private static String fmt(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;
        return String.format("%02d:%02d", h, m);
    }

    @Override
    public String toString() {
        return day + " " + fmt(startMinutes) + "-" + fmt(endMinutes);
    }
}
