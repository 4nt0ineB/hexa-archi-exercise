package lunatech.domain.user;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> get(String username);
    Optional<User> save(User user);
}
