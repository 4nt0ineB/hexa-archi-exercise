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
import java.util.UUID;

public class UserServiceAdapter implements UserServicePort {

    private final UserRepositoryPort userRepository;

    public UserServiceAdapter(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Either<String, UserInfo> find(String origin, String target) {
        return findAuthorizedTarget(origin, target).map(user -> new UserInfo(user.username(), user.role()));
    }

    @Override
    public Either<String, List<Todo>> findTodos(String origin, String target) {
        return findAuthorizedTarget(origin, target).map(User::todos);
    }

    @Override
    public Either<String, List<Todo>> findTodosWithTags(String origin, String target, List<String> tags) {
        return findTodos(origin, target)
                .map(todos -> todos.stream()
                        .filter(todo -> new HashSet<>(todo.tags()).containsAll(tags))
                        .toList());
    }

    @Override
    public Either<String, Todo> findTodoById(String origin, String username, UUID id) {
        return findAuthorizedTarget(origin, username)
                .flatMap(user -> user.todos().stream()
                        .filter(todo -> todo.id().equals(id))
                        .findFirst()
                        .map(Either::<String, Todo>right)
                        .orElse(Either.left("Todo not found"))
                );
    }

    @Override
    public Either<String, Todo> updateTodo(String origin, String target, Todo todo) {
        return findAuthorizedTarget(origin, target)
                .flatMap(user -> userRepository.updateTodo(user, todo));
    }

    @Override
    public Either<String, Todo> addTodo(String origin, String target, Todo todo) {
        return findAuthorizedTarget(origin, target)
                .flatMap(user -> userRepository.addTodoToUser(user, todo.withId(UUID.randomUUID())));
    }

    @Override
    public Either<String, UUID> deleteTodo(String origin, String target, UUID id) {
        return findAuthorizedTarget(origin, target)
                .flatMap(user -> userRepository.deleteTodo(user, id));
    }

    /**
     * Retrieves the target user and checks if the origin user is authorized to perform actions.
     *
     * @param origin the username of the initiator of the action
     * @param target the username of the target user
     * @return Either an error message or the target User
     */
    private Either<String, User> findAuthorizedTarget(String origin, String target) {
        return userRepository.getByUsername(origin)
                .map(originUser -> checkAuthorizationAndFindTarget(originUser, target))
                .orElse(Either.left(origin + " not found"));
    }

    /**
     * Checks if the origin user is authorized to access or modify the target user.
     *
     * @param originUser the user performing the action
     * @param target the target username
     * @return Either the target user or an error message
     */
    private Either<String, User> checkAuthorizationAndFindTarget(User originUser, String target) {
        if (originUser.username().equals(target)) {
            return Either.right(originUser);
        }
        if (!originUser.role().equals(Role.ADMIN)) {
            return Either.left("Unauthorized access to user '" + target + "'");
        }
        return userRepository.getByUsername(target)
                .map(Either::<String, User>right)
                .orElse(Either.left(target + " not found"));
    }

}
