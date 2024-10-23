package lunatech.domain.adapter;

import io.vavr.control.Either;
import lunatech.domain.dto.UserInfo;
import lunatech.domain.model.Role;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import lunatech.domain.port.UserServicePort;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class UserServiceAdapter implements UserServicePort {

    private final UserRepositoryPort userRepository;

    public UserServiceAdapter(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Either<String, UserInfo> find(String origin, String target) {
        return findTarget(origin, target).map(user -> new UserInfo(user.username(), user.role()));
    }

    @Override
    public Either<String, List<Todo>> findTodos(String origin, String target) {
        return findTarget(origin, target).map(User::todos);
    }

    @Override
    public Either<String, List<Todo>> findTodosWithTags(String origin, String target, List<String> tags) {
        return findTodos(origin, target)
                .map(todos -> todos.stream()
                        .filter(todo -> new HashSet<>(todo.tags()).containsAll(tags))
                        .toList());
    }

    @Override
    public Either<String, Optional<Todo>> findTodoById(String origin, String username, String id) {
        return findTarget(origin, username)
                .map(user -> user.todos().stream()
                        .filter(todo -> todo.id().equals(String.valueOf(id)))
                        .findFirst());
    }

    @Override
    public Either<String, Todo> updateTodo(String origin, String target, Todo todo) {
        return findTarget(origin, target)
                .flatMap(user -> userRepository.updateTodo(user, todo));
    }

    @Override
    public Either<String, Todo> addTodo(String origin, String target, Todo todo) {
        return findTarget(origin, target)
                .flatMap(user -> {
                    var id = String.valueOf(ThreadLocalRandom.current().nextLong());
                    return userRepository.addTodoToUser(user, todo.withId(id));
                });
    }

    @Override
    public Either<String, String> deleteTodo(String origin, String target, String id) {
        return findTarget(origin, target)
                .flatMap(user -> userRepository.deleteTodo(user, id));
    }

    /**
     *
     * Retrieve the target user.
     * Checks authorization of the user performing the action on the target user
     *
     * @param origin username of the initiator of the action
     * @param target username of the target of the action
     * @return Either a string with an error message is the origin user unauthorized
     * or an optional of the target user
     */
    private Either<String, User> findTarget(String origin, String target) {
        return userRepository.getByUsername(origin)
                .map(originUser -> {
                    if (origin.equals(target)) {
                        return Either.<String,User>right(originUser);
                    }
                    // Only admin can perform actions on other users
                    if(!originUser.role().equals(Role.ADMIN)) {
                        return Either.<String,User>left("User not authorized");
                    }
                    return userRepository.getByUsername(target)
                            .map(Either::<String, User>right)
                            .orElse(Either.left(target + " user not found"));
                })
                .orElse(Either.left(origin + " not found"));
    }

}
