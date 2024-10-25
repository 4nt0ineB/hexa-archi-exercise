package lunatech.domain.todo;

import java.util.List;
import java.util.UUID;

public interface TodoServicePort {
    List<Todo> find(String origin, String target) ;
    List<Todo> findWithTags(String origin, String target, List<String> tags);
    /**
     * Find a todo by its ID
     * @param origin the origin user of the request
     * @param username the target user of the request
     * @param id the ID of the todo
     * @return The todo
     * @throws UnknownTodoException if the todo is not found
     */
    Todo findById(String origin, String username, UUID id);
    Todo update(String origin, String username, Todo todo);
    Todo add(String origin, String target, TodoDTO todo);
    UUID delete(String origin, String target, UUID id);
}
