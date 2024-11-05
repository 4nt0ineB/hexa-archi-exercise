package lunatech.domain.permission;

import lunatech.domain.todo.TodoServicePort;

@FunctionalInterface
public interface AuthorizedActionOnTodoService<T>{
    T apply(Context context, TodoServicePort todoService);
}
