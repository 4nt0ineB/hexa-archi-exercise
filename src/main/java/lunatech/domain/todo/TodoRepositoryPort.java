package lunatech.domain.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepositoryPort {
     List<Todo> find(String username);
     List<Todo> findWithTags(String username, List<String> tags);
     Optional<Todo> findById(String username, UUID id);
     Optional<Todo> update(String username, Todo todo);
     Todo add(String username, Todo todo);
     Optional<UUID> delete(String username, UUID id);
}
