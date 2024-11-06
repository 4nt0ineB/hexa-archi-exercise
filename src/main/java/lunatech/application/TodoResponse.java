package lunatech.application;

import lunatech.domain.todo.Todo;

import java.util.List;

public record TodoResponse(List<Todo> todos) {
}