package lunatech.domain.auth;

import lunatech.domain.user.UserOutput;
import lunatech.domain.user.UserRepositoryPort;

import java.util.Objects;
import java.util.Optional;

public class AuthServiceAdapter implements AuthServicePort {

    private final UserRepositoryPort userRepositoryAdapter;

    public AuthServiceAdapter(UserRepositoryPort userRepositoryAdapter) {
        this.userRepositoryAdapter = userRepositoryAdapter;
    }

    public Optional<UserOutput> authenticate(String username, String password) {
        return userRepositoryAdapter.get(username)
                .filter(user -> Objects.equals(user.password(),password))
                .map(UserOutput::from);
    }

}
