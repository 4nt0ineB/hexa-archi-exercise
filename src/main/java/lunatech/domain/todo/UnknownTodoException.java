package lunatech.domain.todo;

import java.util.UUID;

public class UnknownTodoException extends RuntimeException {
    public final UUID id;
    public UnknownTodoException(UUID id) {
        this.id = id;
    }
}
