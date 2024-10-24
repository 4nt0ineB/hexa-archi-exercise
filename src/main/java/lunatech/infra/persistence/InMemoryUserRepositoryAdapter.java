package lunatech.infra.persistence;

import io.quarkus.arc.profile.IfBuildProfile;
import io.vavr.control.Either;
import jakarta.enterprise.context.ApplicationScoped;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
@IfBuildProfile("test")
public class InMemoryUserRepositoryAdapter implements UserRepositoryPort {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> get(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public Optional<User> save(User user) {
        users.put(user.username(), user);
        return Optional.of(user);
    }
}
