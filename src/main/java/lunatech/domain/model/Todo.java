package lunatech.domain.model;

import java.util.List;
import java.util.Objects;

public record Todo(String id, String title, String description, List<String> tags, boolean done) {

    public Todo(String id, String title, List<String> tags) {
        this(id, title, "", tags, false);
    }

    public Todo {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(tags);
        tags = List.copyOf(tags);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Todo other && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
