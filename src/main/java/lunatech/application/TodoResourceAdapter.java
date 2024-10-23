package lunatech.application;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lunatech.domain.port.UserServicePort;
import lunatech.domain.model.Role;
import lunatech.domain.model.Todo;
import lunatech.infra.security.SecurityService;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Path("/api/todos")
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResourceAdapter {

    @Inject
    SecurityService securityService;

    private final UserServicePort userService;

    @Inject
    public TodoResourceAdapter(UserServicePort userService) {
        this.userService = userService;
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
                    return userService.findTodosWithTags(securityService.userName(), userTarget, tagList);
                })
                .orElse(userService.findTodos(securityService.userName(), userTarget));

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

        var todo = userService.findTodoById(securityService.userName(), userTarget, id);
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
        var userTarget = userName.orElse(securityService.userName());
        return userService.addTodo(securityService.userName(), userTarget, todoToAdd)
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
        return userService.updateTodo(securityService.userName(), userTarget, todoToUpdate)
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
        return userService.deleteTodo(securityService.userName(), userTarget, id)
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.FORBIDDEN).entity(error))
                .build();
    }

}
