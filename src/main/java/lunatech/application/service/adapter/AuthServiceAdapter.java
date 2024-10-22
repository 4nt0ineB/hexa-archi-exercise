package lunatech.application.service.adapter;

import io.vavr.control.Either;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lunatech.application.service.dto.UserInfo;
import lunatech.application.service.port.AuthServicePort;
import lunatech.domain.model.User;
import lunatech.domain.UserRepositoryPort;
import org.jboss.logging.Logger;

import java.util.Optional;

@ApplicationScoped
public class AuthServiceAdapter implements AuthServicePort {

    private static final Logger logger = Logger.getLogger(AuthServiceAdapter.class);

    @Inject
    UserRepositoryPort userRepositoryAdapter;

    public Optional<UserInfo> authenticate(String username, String password) {
        return userRepositoryAdapter.getByUsername(username)
                .filter(user -> user.password().equals(password))
                .map(user -> new UserInfo(user.username(), user.role()));
    }

}
