package lunatech.infra.persistence;

import io.quarkus.arc.profile.IfBuildProfile;
import io.vavr.control.Either;
import jakarta.enterprise.context.ApplicationScoped;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.UserRepositoryPort;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
@IfBuildProfile("dev")
public class InMemoryUserRepositoryAdapter implements UserRepositoryPort {

    private static final Logger logger = Logger.getLogger(InMemoryUserRepositoryAdapter.class);

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
    public void delete(User user) {
        users.remove(user.username());
    }

    @Override
    public Either<String, Todo> addTodoToUser(User user, Todo todo) {
        return Optional.ofNullable(users.get(user.username())).map(u -> u.todos().stream()
                .filter(t -> t.equals(todo))
                .findFirst()
                .map(t -> Either.<String,Todo>left("Todo already exists"))
                .orElseGet(() -> {
                    // ugly
                    var newtodo = new Todo(ThreadLocalRandom.current().nextLong(), todo.title(), todo.description(), todo.tags(), todo.done());
                    user.todos().add(newtodo);
                    return Either.right(newtodo);
                })).orElse(Either.left("User not found"));
    }
}