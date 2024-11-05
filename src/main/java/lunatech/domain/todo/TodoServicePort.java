package lunatech.domain.todo;

import lunatech.domain.permission.Context;

import java.util.List;
import java.util.UUID;

public interface TodoServicePort {
    List<Todo> find(Context context) ;

    List<Todo> findWithTags(Context context, List<String> tags);

    /**
     * Find a todo by its ID
     * @param context the context of the request
     * @param id the ID of the todo
     * @return The todo
     * @throws UnknownTodoException if the todo is not found
     */
    Todo findById(Context context, UUID id);

    /**
     * Update a todo
     * @param context the context of the request
     * @param todo the todo to update
     * @return the updated todo
     * @throws UnknownTodoException if the todo is not found
     */
    Todo update(Context context, Todo todo);

    /**
     * Add a todo
     * @param context the context of the request
     * @param todo the todo to add
     * @return the added todo
     */
    Todo add(Context context, TodoInput todo);

    /**
     * Delete a todo
     * @param context the context of the request
     * @param id the ID of the todo to delete
     * @return the ID of the deleted todo
     * @throws UnknownTodoException if the todo is not found
     */
    UUID delete(Context context, UUID id);
}
