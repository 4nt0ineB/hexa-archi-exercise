package lunatech.domain.todo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lunatech.domain.permission.Context;
import lunatech.domain.permission.ForbiddenActionException;
import org.jboss.logging.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TodoServiceAdapter implements TodoServicePort {

    private static final Logger logger = Logger.getLogger(TodoServiceAdapter.class);
    private final Validator validator;
    private final TodoRepositoryPort todoRepository;

    private final Set<UUID> deletedTodos = new HashSet<>();

    public TodoServiceAdapter(
            TodoRepositoryPort todoRepository,
            Validator validator
            ) {
        this.validator = validator;
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> find(Context context) {
        logAction("Finding all todos", context);
         return todoRepository.find(context.target().username());
    }

    @Override
    public List<Todo> findWithTags(Context context, List<String> tags) {
        logAction("Finding todos with tags %s", context, tags);
        return todoRepository.findWithTags(context.target().username(), tags);
    }

    @Override
    public Todo findById(Context context, UUID id) {
        logAction("Finding todo ID: %s", context, id);
        return todoRepository.findById(context.target().username(), id)
                .orElseThrow(() -> new UnknownTodoException(id));
    }

    @Override
    public Todo update(Context context, Todo todo) {
        logAction("Updating todo: %s", context, todo);
        validate(todo);
        todoRepository.findById(context.target().username(), todo.id())
                .map(__ -> todoRepository.upsert(context.target().username(), todo))
                .orElseThrow(() -> new ForbiddenActionException(""));
        return todo;
    }

    @Override
    public Todo add(Context context, TodoInput todoInput) {
        logAction("Adding new todo:", context, todoInput);
        validate(todoInput);
        var todo = new Todo(
                UUID.randomUUID(),
                todoInput.title(),
                todoInput.description(),
                todoInput.tags(),
                false);
        return todoRepository.upsert(context.target().username(), todo);
    }

    @Override
    public UUID delete(Context context, UUID id) {
        logAction("Deleting todo ID: %s", context, id);
        if(deletedTodos.contains(id)) { // idempotence
            return id;
        }
        var maybeDeletedId = todoRepository.delete(context.target().username(), id);
        if(maybeDeletedId.isPresent()) {
            deletedTodos.add(id);
        }
        return maybeDeletedId
                .orElseThrow(() -> new UnknownTodoException(id)); // it never existed
    }

    private <T> void validate(T todo) {
        var violations = validator.validate(todo);
        if (!violations.isEmpty()) {
            var messages = String.join(", ", violations.stream().map(ConstraintViolation::getMessage).toList());
            logger.warnf("Validation errors: %s", messages);
            throw new IllegalArgumentException(messages);
        }
    }

    private void logAction(String msg, Context context, Object... args) {
        var prefix = "(%s" + (context.isImpersonating() ? "" : " as %s") + ") ";
        logger.infof(prefix + msg, context.origin(), context.target(), args);
    }
}
