package lunatech.domain;

import java.util.List;
import java.util.Objects;

public record Todo(long id, String title, String description, List<String> tags, boolean done) {

    public Todo {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(tags);
        tags = List.copyOf(tags);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Todo other && other.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
