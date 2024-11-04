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

    /**
     * Update a todo
     * @param origin the origin user of the request
     * @param username the target user of the request
     * @param todo the todo to update
     * @return the updated todo
     * @throws UnknownTodoException if the todo is not found
     */
    Todo update(String origin, String username, Todo todo);

    /**
     * Add a todo
     * @param origin the origin user of the request
     * @param target the target user of the request
     * @param todo the todo to add
     * @return the added todo
     */
    Todo add(String origin, String target, TodoInput todo);

    /**
     * Delete a todo
     * @param origin the origin user of the request
     * @param target the target user of the request
     * @param id the ID of the todo to delete
     * @return the ID of the deleted todo
     * @throws UnknownTodoException if the todo is not found
     */
    UUID delete(String origin, String target, UUID id);
}
