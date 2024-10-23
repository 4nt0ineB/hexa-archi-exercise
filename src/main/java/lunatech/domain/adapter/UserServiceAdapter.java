package lunatech.domain.adapter;

import io.vavr.control.Either;
import lunatech.domain.model.Role;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import lunatech.domain.port.UserServicePort;

import java.util.List;
import java.util.Optional;

public class UserServiceAdapter implements UserServicePort {

    private final UserRepositoryPort userRepository;

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
    public Either<String, Todo> addTodo(String origin, String target, Todo todo) {
        return find(origin, target)
                .flatMap(eitherUser -> {
                    // Extract UserEntity
                    return eitherUser
                            .map(Either::<String, User>right)
                            .orElse(Either.left("User not found"));
                })
                .flatMap(user -> {
                    // Adding TodoEntity to UserEntity
                    return userRepository.addTodoToUser(user, todo);
                });
    }

    /**
     *
     * @param origin username of the initiator of the action
     * @param target username of the target of the action
     * @return Either a string with an error message is the origin user unauthorized
     * or an optional of the target user
     */
    private Either<String, Optional<User>> findTarget(String origin, String target) {
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
