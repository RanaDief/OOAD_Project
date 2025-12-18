package model;

public class Administrator extends UserAccount {
    public Administrator(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public String role() { return "ADMIN"; }
}
