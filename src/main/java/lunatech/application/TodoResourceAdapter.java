package lunatech.application;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.netty.handler.codec.http.HttpStatusClass;
import io.vertx.core.spi.observability.HttpResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lunatech.domain.todo.Todo;
import lunatech.domain.todo.TodoInput;
import lunatech.domain.todo.TodoServicePort;
import lunatech.domain.user.Role;
import lunatech.infra.security.SecurityService;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CRUD for TodoEntity
 * NB: Regular users are allowed to get/modify/delete their own todos
 * NB: Admin users are allowed to get/modify/delete every todos
 */
@Path("/api/todos")
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({Role.Names.ADMIN, Role.Names.REGULAR})
public class TodoResourceAdapter {

    @Inject SecurityService securityService;
    MeterRegistry metrics = Metrics.globalRegistry;

    private final TodoServicePort todoService;

    @Inject
    public TodoResourceAdapter(TodoServicePort todoService) {
        this.todoService = todoService;
    }

    @GET
    public List<Todo> todos(
            @QueryParam("tags") Optional<String> tagsFilter,
            @QueryParam("user") Optional<String> userName
    ) {
        var userTarget =  userName.orElse(securityService.userName());
        return tagsFilter
                .map(tags -> {
                    var tagList = Arrays.asList(tags.split(","));
                    return todoService.findWithTags(securityService.userName(), userTarget, tagList);
                })
                .orElse(todoService.find(securityService.userName(), userTarget));
    }

    @GET
    @Path("/{id}")
    public Todo todo(
            @QueryParam("user") Optional<String> username,
            @PathParam("id") UUID id
    ) {
        var userTarget = username.orElse(securityService.userName());
        return todoService.findById(securityService.userName(), userTarget, id);
    }

    @POST
    public Response addTodo(
            @QueryParam("user") Optional<String> userName,
            @Valid TodoInput todoToAdd
    ) {
        var userTarget = userName.orElse(securityService.userName());
        var todo = todoService.add(securityService.userName(), userTarget, todoToAdd);
        metrics.counter("todos.created").increment();
        return Response.created(URI.create(String.format("/api/todos/%s", todo.id())))
                .entity(todo)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Todo updateTodo(
            @QueryParam("user") Optional<String> userName,
            Todo todoToUpdate

    ) {
        var userTarget = userName.orElse(securityService.userName());
        return todoService.update(securityService.userName(), userTarget, todoToUpdate);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(
            @QueryParam("user") Optional<String> userName,
            @PathParam("id") UUID id
    ) {
        var userTarget = userName.orElse(securityService.userName());
        todoService.delete(securityService.userName(), userTarget, id);
        return Response.noContent().build();
    }
}
