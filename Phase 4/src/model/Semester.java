package model;

import java.util.Objects;

public class Semester {
    private final String name; // e.g., "Fall 2025"

    public Semester(String name) {
        this.name = Objects.requireNonNull(name).trim();
        if (this.name.isEmpty()) throw new IllegalArgumentException("Empty semester name");
    }

    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}
