package lunatech.domain.adapter;

import io.vavr.control.Either;
import lunatech.domain.PermissionManager;
import lunatech.domain.model.Role;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.TodoRepositoryPort;
import lunatech.domain.port.TodoServicePort;
import lunatech.domain.port.UserRepositoryPort;
import lunatech.domain.port.UserServicePort;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class TodoServiceAdapter implements TodoServicePort {

    private final TodoRepositoryPort todoRepository;
    private final UserServicePort userService;

    public TodoServiceAdapter(
            TodoRepositoryPort todoRepository,
            UserServicePort userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    @Override
    public Either<String, List<Todo>> find(String origin, String target) {
        return userService.find(origin, target)
                .map(__ -> todoRepository.find(target));
    }

    @Override
    public Either<String, List<Todo>> findWithTags(String origin, String target, List<String> tags) {
        return userService.find(origin, target)
                .map(__ -> todoRepository.findWithTags(target, tags));
    }

    @Override
    public Either<String, Todo> findById(String origin, String username, UUID id) {
        return userService.find(origin, username)
                .flatMap(__ -> todoRepository.findById(username, id)
                        .map(Either::<String, Todo>right)
                        .orElse(Either.left("Todo not found"))
                );
    }

    @Override
    public Either<String, Todo> update(String origin, String username, Todo todo) {
        return userService.find(origin, username)
                .flatMap(__ ->
                        todoRepository.update(username, todo)
                                .map(Either::<String, Todo>right)
                                .orElse(Either.left("Todo not found")));
    }

    @Override
    public Either<String, Todo> add(String origin, String target, Todo todo) {
        return userService.find(origin, target)
                .flatMap(__ -> Either.right(todoRepository.add(target, todo.id() == null ? todo.withId(UUID.randomUUID()) : todo)));
    }

    @Override
    public Either<String, UUID> delete(String origin, String target, UUID id) {
        return userService.find(origin, target)
                .flatMap(__ -> todoRepository.delete(target, id)
                                .map(Either::<String, UUID>right)
                                .orElse(Either.left("Todo not found")));
    }
}
