package lunatech.domain.todo;

import java.util.List;
import java.util.UUID;

public record Todo(
        UUID id,
        String title,
        String description,
        List<String> tags,
        boolean done) {

    public Todo {
        tags = List.copyOf(tags);
    }

    public static Todo from(TodoInput input) {
        return new Todo(UUID.randomUUID(), input.title(), input.description(), input.tags(), false);
    }
}
