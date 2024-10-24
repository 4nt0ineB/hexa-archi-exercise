package lunatech.domain.port;

import io.vavr.control.Either;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    Optional<User> get(String username);
    Optional<User> save(User user);
}
