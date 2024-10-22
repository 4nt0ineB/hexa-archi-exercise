package lunatech.application.service;

import io.quarkus.arc.profile.IfBuildProfile;
import io.vavr.control.Either;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lunatech.domain.Role;
import lunatech.domain.Todo;
import lunatech.domain.User;
import lunatech.domain.UserRepositoryPort;
import lunatech.infra.persistence.InMemoryUserRepositoryAdapter;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@IfBuildProfile("dev")
public class UserServiceAdapter implements UserServicePort {

    private final UserRepositoryPort userRepository;

    @Inject
    public UserServiceAdapter(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Either<String, Optional<User>> find(String origin, String target) {
        return findTarget(origin, target);
    }

    @Override
    public Either<String, Optional<List<Todo>>> findTodos(String origin, String target) {
        return findTarget(origin, target)
                .map(user -> user.flatMap(u -> Optional.of(u.todos())));
    }

    @Override
    public Optional<List<Todo>> findTodosWithTags(String origin, String target, List<String> tags) {
        return Optional.empty();
    }

    @Override
    public Optional<Todo> findTodoById(String username, long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> createUser(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> updateTodo(User user, Todo todo) {
        return Optional.empty();
    }

    @Override
    public Either<String, Todo> addTodo(User user, Todo todo) {
        return userRepository.addTodoToUser(user, todo);
    }

    private Either<String, Optional<User>> findTarget(String origin, String target) {
        System.out.println("origin: " + origin);
        System.out.println("target: " + target);
        var originUser = userRepository.getByUsername(origin);
        if(origin.equals(target)) {
            return Either.right(originUser);
        }
        var targetUser = userRepository.getByUsername(target);
        if (originUser.isEmpty()) {
            return Either.left("Origin user not found");
        }
        if (targetUser.isEmpty()) {
            return Either.right(Optional.empty());
        }
        if(!originUser.get().role().equals(Role.ADMIN)) {
            return Either.left("User not authorized");
        }
        return Either.right(targetUser);
    }
}
