package ocrs.model;

public class Instructor extends UserAccount {
    public Instructor(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public String role() { return "INSTRUCTOR"; }
}
