package lunatech.domain.adapter;

import lunatech.domain.dto.UserInfo;
import lunatech.domain.port.AuthServicePort;
import lunatech.domain.port.UserRepositoryPort;
import org.jboss.logging.Logger;

import java.util.Optional;

public class AuthServiceAdapter implements AuthServicePort {

    private final UserRepositoryPort userRepositoryAdapter;

    public AuthServiceAdapter(UserRepositoryPort userRepositoryAdapter) {
        this.userRepositoryAdapter = userRepositoryAdapter;
    }

    public Optional<UserInfo> authenticate(String username, String password) {
        return userRepositoryAdapter.getByUsername(username)
                .filter(user -> user.password().equals(password))
                .map(user -> new UserInfo(user.username(), user.role()));
    }

}
