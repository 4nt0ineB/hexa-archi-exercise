package lunatech.application;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lunatech.domain.todo.TodoServicePort;
import lunatech.domain.user.Role;
import lunatech.domain.todo.Todo;
import lunatech.infra.security.SecurityService;
import jakarta.validation.ConstraintViolation;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * CRUD for TodoEntity
 * NB: Regular users are allowed to get/modify/delete their own todos
 * NB: Admin users are allowed to get/modify/delete every todos
 */
@Path("/api/todos")
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResourceAdapter {

    @Inject Validator validator;
    @Inject SecurityService securityService;

    private final TodoServicePort todoService;

    @Inject
    public TodoResourceAdapter(TodoServicePort todoService) {
        this.todoService = todoService;
    }

    @GET
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response todos(
            @QueryParam("tags") Optional<String> tagsFilter,
            @QueryParam("user") Optional<String> userName
    ) {
        var userTarget =  userName.orElse(securityService.userName());

        var todos = tagsFilter
                .map(tags -> {
                    var tagList = Arrays.asList(tags.split(","));
                    return todoService.findWithTags(securityService.userName(), userTarget, tagList);
                })
                .orElse(todoService.find(securityService.userName(), userTarget));

        return todos
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.FORBIDDEN).entity(error))
                .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response todo(
            @QueryParam("user") Optional<String> username,
            @PathParam("id") UUID id
    ) {
        var userTarget = username.orElse(securityService.userName());

        var todo = todoService.findById(securityService.userName(), userTarget, id);
        return todo
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.FORBIDDEN).entity(error))
                .build();
    }

    @POST
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response addTodo(
            @QueryParam("user") Optional<String> userName,
            Todo todoToAdd
    ) {
        var violations = validator.validate(todoToAdd);
        if (!violations.isEmpty()) {
            var messages = violations.stream().map(ConstraintViolation::getMessage);
            return Response.status(Response.Status.BAD_REQUEST).entity(messages).build();
        }
        var userTarget = userName.orElse(securityService.userName());
        return todoService.add(securityService.userName(), userTarget, todoToAdd)
                .map(todo -> Response.created(URI.create(String.format("/api/todos/%s", todo.id()))).entity(todo))
                .getOrElseGet(error -> Response.status(Response.Status.FORBIDDEN).entity(error))
                .build();
    }

    @PUT
    @Path("/")
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response updateTodo(
            @QueryParam("user") Optional<String> userName,
            Todo todoToUpdate
    ) {
        var userTarget = userName.orElse(securityService.userName());
        return todoService.update(securityService.userName(), userTarget, todoToUpdate)
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.FORBIDDEN).entity(error))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    public Response delete(
            @QueryParam("user") Optional<String> userName,
            @PathParam("id") UUID id
    ) {
        var userTarget = userName.orElse(securityService.userName());
        return todoService.delete(securityService.userName(), userTarget, id)
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.FORBIDDEN).entity(error))
                .build();
    }

}
