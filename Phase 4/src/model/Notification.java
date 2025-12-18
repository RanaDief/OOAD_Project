package model;

public class Notification {
    private final String message;

    public Notification(String message) {
        this.message = message == null ? "" : message;
    }

    public String getMessage() { return message; }

    @Override
    public String toString() { return message; }
}
