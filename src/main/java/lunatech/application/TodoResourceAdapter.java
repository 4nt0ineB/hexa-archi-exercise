package lunatech.application;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lunatech.domain.permission.Context;
import lunatech.domain.todo.Todo;
import lunatech.domain.todo.TodoInput;
import lunatech.domain.todo.TodoServicePort;
import lunatech.domain.user.Role;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * CRUD for TodoEntity
 * NB: Admin users are allowed to get/modify/delete every todos
 */
@Path("/api/todos")
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({Role.Names.ADMIN, Role.Names.REGULAR})
public class TodoResourceAdapter {

    MeterRegistry metrics = Metrics.globalRegistry;
    private final TodoServicePort todoService;
    @Inject
    ContainerRequestContext requestContext;

    @Inject
    public TodoResourceAdapter(TodoServicePort todoService) {
        this.todoService = todoService;
    }

    @GET
    public TodoResponse todos(
            @QueryParam("tags") Optional<String> tagsFilter,
            @QueryParam("user") Optional<String> userName

    ) {
        var context = getContext();
        var todo = tagsFilter
                .map(tags -> {
                    var tagList = Arrays.asList(tags.split(","));
                    return todoService.findWithTags(context, tagList);
                })
                .orElse(todoService.find(context));
        return new TodoResponse(todo);
    }

    @GET
    @Path("/{id}")
    public Todo todo(
            @QueryParam("user") Optional<String> username,
            @PathParam("id") UUID id
    ) {
        return todoService.findById(getContext(), id);
    }


    @POST
    public Response addTodo(
            @QueryParam("user") Optional<String> userName,
            @Valid TodoInput todoToAdd
    ) {
        var todo = todoService.add(getContext(), todoToAdd);
        metrics.counter("todos.created").increment();
        return Response.created(URI.create(String.format("/api/todos/%s", todo.id())))
                .entity(todo)
                .build();
    }

    @PUT
    @Path("/")
    public Todo updateTodo(
            @QueryParam("user") Optional<String> userName,
            Todo todoToUpdate
    ) {
        return todoService.update(getContext(), todoToUpdate);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(
            @QueryParam("user") Optional<String> userName,
            @PathParam("id") UUID id
    ) {
        todoService.delete(getContext(), id);
        return Response.noContent().build();
    }

    public Context getContext() {
        return (Context) requestContext.getProperty("context");
    }
}
