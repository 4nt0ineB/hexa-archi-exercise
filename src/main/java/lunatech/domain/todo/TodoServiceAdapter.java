package lunatech.domain.todo;

import io.vavr.control.Either;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lunatech.domain.PermissionManager;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TodoServiceAdapter implements TodoServicePort {

    private static final Logger logger = Logger.getLogger(TodoServiceAdapter.class);
    private final Validator validator;
    private final TodoRepositoryPort todoRepository;
    private final PermissionManager permissionManager;

    public TodoServiceAdapter(
            TodoRepositoryPort todoRepository,
            PermissionManager permissionManager,
            Validator validator
            ) {
        this.validator = validator;
        this.todoRepository = todoRepository;
        this.permissionManager = permissionManager;
    }

    @Override
    public Either<String, List<Todo>> find(String origin, String target) {
        logAction("Finding all todos", target, origin);
        return permissionManager.canSee(origin, target)
                .map(__ -> todoRepository.find(target));
    }

    @Override
    public Either<String, List<Todo>> findWithTags(String origin, String target, List<String> tags) {
        logAction("Finding todos with tags %s", target, origin, tags);
        return permissionManager.canSee(origin, target)
                .map(__ -> todoRepository.findWithTags(target, tags));
    }

    @Override
    public Either<String, Todo> findById(String origin, String target, UUID id) {
        logAction("Finding todo ID: %s", origin, target, id);
        return permissionManager.canSee(origin, target)
                .flatMap(__ -> todoRepository.findById(target, id)
                        .map(Either::<String, Todo>right)
                        .orElse(Either.left("Todo not found"))
                );
    }

    @Override
    public Either<String, Todo> update(String origin, String username, Todo todo) {
        logAction("Updating todo: %s", origin, username, todo);
        var errors = validate(todo);
        return errors.<Either<String, Todo>>map(Either::left)
                .orElseGet(() -> permissionManager.canSee(origin, username)
                        .flatMap(__ -> todoRepository.update(username, todo)
                                .map(Either::<String, Todo>right)
                                .orElse(Either.left("Todo not found"))));
    }

    @Override
    public Either<String, Todo> add(String origin, String target, TodoDTO todoDto) {
        logAction("Adding new todo:", origin, target, todoDto);
        var errors = validate(todoDto);
        return errors.<Either<String, Todo>>map(Either::left)
                .orElseGet(() -> permissionManager.canSee(origin, target)
                        .flatMap(__ -> {
                            var todo = new Todo(
                            UUID.randomUUID(),
                            todoDto.title(),
                            todoDto.description(),
                            todoDto.tags(),
                            false
                    );
                    return Either.right(todoRepository.add(target, todo));
                }));
    }

    @Override
    public Either<String, UUID> delete(String origin, String target, UUID id) {
        logAction("Deleting todo ID: %s", origin, target, id);
        return permissionManager.canSee(origin, target)
                .flatMap(__ -> todoRepository.delete(target, id)
                                .map(Either::<String, UUID>right)
                                .orElse(Either.left("Todo not found")));
    }

    private <T> Optional<String> validate(T todo) {
        var violations = validator.validate(todo);
        if (!violations.isEmpty()) {
            var messages = String.join(", ", violations.stream().map(ConstraintViolation::getMessage).toList());
            logger.warnf("Validation errors: %s", messages);
            return Optional.of(messages);
        }
        return Optional.empty();
    }

    private void logAction(String msg, String origin, String target, Object... args) {
        var prefix = "(%s" + (origin.equals(target) ? "" : " as %s") + ") ";
        logger.infof(prefix + msg, origin, target, args);
    }
}
