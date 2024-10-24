package lunatech.domain.auth;

import lunatech.domain.user.UserInfo;
import lunatech.domain.user.UserRepositoryPort;

import java.util.Optional;

public class AuthServiceAdapter implements AuthServicePort {

    private final UserRepositoryPort userRepositoryAdapter;

    public AuthServiceAdapter(UserRepositoryPort userRepositoryAdapter) {
        this.userRepositoryAdapter = userRepositoryAdapter;
    }

    public Optional<UserInfo> authenticate(String username, String password) {
        return userRepositoryAdapter.get(username)
                .filter(user -> user.password().equals(password))
                .map(user -> new UserInfo(user.username(), user.role()));
    }

}
