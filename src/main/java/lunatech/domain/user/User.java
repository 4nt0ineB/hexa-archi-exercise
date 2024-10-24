package lunatech.domain.user;

import lunatech.domain.todo.Todo;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String username;
    private final String password;
    private final Role role;
    private final List<Todo> todos = new ArrayList<>();

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public Role role() {
        return role;
    }

    public List<Todo> todos() {
        return todos;
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
    }

}
