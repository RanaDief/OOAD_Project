package ocrs.service;

import ocrs.data.DataStore;
import ocrs.model.UserAccount;

import java.util.Optional;

public class AuthService {
    private final DataStore store;

    public AuthService(DataStore store) {
        this.store = store;
    }

    public Optional<UserAccount> login(String email, String password) {
        return store.findUserByEmail(email)
                .filter(u -> u.checkPassword(password));
    }
}
