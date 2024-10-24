package lunatech.domain.todo;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import lunatech.domain.PermissionManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TodoServiceAdapter implements TodoServicePort {

    @Inject Validator validator;

    private final TodoRepositoryPort todoRepository;
    private final PermissionManager permissionManager;

    public TodoServiceAdapter(
            TodoRepositoryPort todoRepository,
            PermissionManager permissionManager) {
        this.todoRepository = todoRepository;
        this.permissionManager = permissionManager;
    }

    @Override
    public Either<String, List<Todo>> find(String origin, String target) {
        return permissionManager.canSee(origin, target)
                .map(__ -> todoRepository.find(target));
    }

    @Override
    public Either<String, List<Todo>> findWithTags(String origin, String target, List<String> tags) {
        return permissionManager.canSee(origin, target)
                .map(__ -> todoRepository.findWithTags(target, tags));
    }

    @Override
    public Either<String, Todo> findById(String origin, String username, UUID id) {
        return permissionManager.canSee(origin, username)
                .flatMap(__ -> todoRepository.findById(username, id)
                        .map(Either::<String, Todo>right)
                        .orElse(Either.left("Todo not found"))
                );
    }

    @Override
    public Either<String, Todo> update(String origin, String username, Todo todo) {
        var errors = validate(todo);
        return errors.<Either<String, Todo>>map(Either::left).orElseGet(() -> permissionManager.canSee(origin, username)
                .flatMap(__ ->
                        todoRepository.update(username, todo)
                                .map(Either::<String, Todo>right)
                                .orElse(Either.left("Todo not found"))));
    }

    @Override
    public Either<String, Todo> add(String origin, String target, Todo todo) {
        var errors = validate(todo);
        return errors.<Either<String, Todo>>map(Either::left).orElseGet(() -> permissionManager.canSee(origin, target)
                .flatMap(__ -> Either.right(todoRepository.add(target, todo.id() == null ? todo.withId(UUID.randomUUID()) : todo))));
    }

    @Override
    public Either<String, UUID> delete(String origin, String target, UUID id) {
        return permissionManager.canSee(origin, target)
                .flatMap(__ -> todoRepository.delete(target, id)
                                .map(Either::<String, UUID>right)
                                .orElse(Either.left("Todo not found")));
    }

    private Optional<String> validate(Todo todo) {
        var violations = validator.validate(todo);
        if (!violations.isEmpty()) {
            var messages = violations.stream().map(ConstraintViolation::getMessage);
            return Optional.of(String.join(", ", messages.toList()));
        }
        return Optional.empty();
    }
}
