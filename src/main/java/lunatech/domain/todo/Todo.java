package lunatech.domain.todo;

import java.util.List;
import java.util.UUID;

public record Todo(
        UUID id,
        String title,
        String description,
        List<String> tags,
        boolean done) {
}
