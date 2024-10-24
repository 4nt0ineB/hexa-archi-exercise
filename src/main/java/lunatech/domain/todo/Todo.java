package lunatech.domain.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Todo(
        UUID id,
        @NotNull
        @NotBlank
        String title,
        @NotNull
        String description,
        @NotNull
        List<String> tags,
        @NotNull
        boolean done) {

    public Todo(String title, String description, List<String> tags) {
        this(UUID.randomUUID(), title, description, tags, false);
    }

    public Todo(UUID id, String title, List<String> tags) {
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

    public Todo withId(UUID id) {
        return new Todo(id, title, description, tags, done);
    }
}
