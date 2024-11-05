package lunatech.application;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lunatech.domain.permission.PermissionManager;
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

    private final PermissionManager permissionManager;

    @Inject
    public TodoResourceAdapter(TodoServicePort todoService, PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    @GET
    public List<Todo> todos(
            @QueryParam("tags") Optional<String> tagsFilter,
            @QueryParam("user") Optional<String> userName
    ) {
        return tagsFilter
                .map(tags -> {
                    var tagList = Arrays.asList(tags.split(","));
                    return access(userName).todoService((context, service) -> service.findWithTags(context, tagList));
                })
                .orElse(access(userName).todoService((context, service) -> service.find(context)));
    }

    @GET
    @Path("/{id}")
    public Todo todo(
            @QueryParam("user") Optional<String> username,
            @PathParam("id") UUID id
    ) {
        return access(username).todoService((context, service) -> service.findById(context, id));
    }


    @POST
    public Response addTodo(
            @QueryParam("user") Optional<String> userName,
            @Valid TodoInput todoToAdd
    ) {
        var todo = access(userName).todoService((context, service) -> service.add(context, todoToAdd));
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
        return access(userName)
                .todoService((context, service) -> service.update(context, todoToUpdate));
    }

    @DELETE
    @Path("/{id}")
    public Response delete(
            @QueryParam("user") Optional<String> userName,
            @PathParam("id") UUID id
    ) {
        access(userName).todoService((context, service) -> service.delete(context, id));
        return Response.noContent().build();
    }

    private PermissionManager.SafeConduct access(Optional<String> username) {
        var userTarget = username.orElse(securityService.userName());
        return permissionManager
                .as(securityService.userName())
                .impersonate(userTarget)
                .access();
    }

}
