package lunatech.infra.persistence;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import lunatech.domain.user.User;
import lunatech.domain.user.UserRepositoryPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
