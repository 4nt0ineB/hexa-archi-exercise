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
    public Optional<User> getByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public Optional<User> save(User user) {
        users.put(user.username(), user);
        return Optional.of(user);
    }

    @Override
    public Either<String, Todo> addTodoToUser(User user, Todo todo) {
        return Optional.ofNullable(users.get(user.username())).map(u -> u.todos().stream()
                .filter(t -> t.equals(todo))
                .findFirst()
                .map(t -> Either.<String,Todo>left("Todo already exists"))
                .orElseGet(() -> {
                    user.todos().add(todo);
                    return Either.right(todo);
                })).orElse(Either.left("User not found"));
    }

    @Override
    public Either<String, Todo> updateTodo(User user, Todo todo) {
        return users.get(user.username()).todos().stream()
                .filter(t -> t.id().equals(todo.id()))
                .findFirst()
                .map(t -> {
                    user.todos().remove(t);
                    user.todos().add(todo);
                    return Either.<String,Todo>right(todo);
                }).orElseGet(() -> Either.left("Todo not found"));
    }

    @Override
    public Either<String, UUID> deleteTodo(User user, UUID id) {
        return users.get(user.username()).todos().stream()
                .filter(t -> t.id().equals(id))
                .findFirst()
                .map(t -> {
                    user.todos().remove(t);
                    return Either.<String,UUID>right(t.id());
                }).orElseGet(() -> Either.left("Todo not found"));
    }
}
