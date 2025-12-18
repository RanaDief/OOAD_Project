package ocrs.model;

import java.util.Objects;

public abstract class UserAccount {
    private final String userId;
    private final String name;
    private final String email;
    private String password;

    protected UserAccount(String userId, String name, String email, String password) {
        this.userId = Objects.requireNonNull(userId);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email).toLowerCase();
        this.password = Objects.requireNonNull(password);
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public boolean checkPassword(String p) { return password.equals(p); }
    public void setPassword(String newPassword) { this.password = Objects.requireNonNull(newPassword); }

    public abstract String role();

    @Override
    public String toString() {
        return role() + "{" + userId + ", " + name + ", " + email + "}";
    }
}
