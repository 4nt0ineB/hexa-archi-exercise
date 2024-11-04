package lunatech.domain.todo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lunatech.domain.user.UserServicePort;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;

public class TodoServiceAdapter implements TodoServicePort {

    private static final Logger logger = Logger.getLogger(TodoServiceAdapter.class);
    private final Validator validator;
    private final TodoRepositoryPort todoRepository;
    private final UserServicePort userService;

    public TodoServiceAdapter(
            TodoRepositoryPort todoRepository,
            UserServicePort userService,
            Validator validator
            ) {
        this.validator = validator;
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    @Override
    public List<Todo> find(String origin, String target) {
        logAction("Finding all todos", target, origin);
         userService.find(origin, target);
         return todoRepository.find(target);
    }

    @Override
    public List<Todo> findWithTags(String origin, String target, List<String> tags) {
        logAction("Finding todos with tags %s", target, origin, tags);
        userService.find(origin, target);
        return todoRepository.findWithTags(target, tags);
    }

    @Override
    public Todo findById(String origin, String target, UUID id) {
        logAction("Finding todo ID: %s", origin, target, id);
        userService.find(origin, target);
        return todoRepository.findById(target, id)
                .orElseThrow(() -> new UnknownTodoException(id));
    }

    @Override
    public Todo update(String origin, String target, Todo todo) {
        logAction("Updating todo: %s", origin, target, todo);
        validate(todo);
        userService.find(origin, target);
        return todoRepository.update(target, todo);
    }

    @Override
    public Todo add(String origin, String target, TodoInput todoInput) {
        logAction("Adding new todo:", origin, target, todoInput);
        validate(todoInput);
        userService.find(origin, target);
        var todo = new Todo(
                UUID.randomUUID(),
                todoInput.title(),
                todoInput.description(),
                todoInput.tags(),
                false);
        return todoRepository.add(target, todo);
    }

    @Override
    public UUID delete(String origin, String target, UUID id) {
        logAction("Deleting todo ID: %s", origin, target, id);
        userService.find(origin, target);
        return todoRepository.delete(target, id)
                .orElseThrow(() -> new UnknownTodoException(id));
    }

    private <T> void validate(T todo) {
        var violations = validator.validate(todo);
        if (!violations.isEmpty()) {
            var messages = String.join(", ", violations.stream().map(ConstraintViolation::getMessage).toList());
            logger.warnf("Validation errors: %s", messages);
            throw new IllegalArgumentException(messages);
        }
    }

    private void logAction(String msg, String origin, String target, Object... args) {
        var prefix = "(%s" + (origin.equals(target) ? "" : " as %s") + ") ";
        logger.infof(prefix + msg, origin, target, args);
    }
}
