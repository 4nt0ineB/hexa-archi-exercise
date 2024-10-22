package lunatech.infra.api;

import io.vavr.control.Either;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lunatech.application.service.AuthService;
import lunatech.application.service.UserServiceAdapter;
import lunatech.application.service.UserServicePort;
import lunatech.domain.Role;
import lunatech.domain.Todo;
import lunatech.domain.User;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.Optional;

@Path("/api/todos")
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResourceAdapter {

    private static final Logger logger = Logger.getLogger(lunatech.infra.api.TodoResourceAdapter.class);

    @Inject
    Validator validator;
    @Inject
    AuthService authService;

    @Inject
    UserServicePort userService;

    @GET
    @RolesAllowed({ Role.Names.ADMIN, Role.Names.REGULAR })
    @Transactional
    public Response todos(
            @QueryParam("tags") Optional<String> tagsFilter,
            @QueryParam("user") Optional<String> userName
    ) {
        var userTarget =  userName.orElse(authService.userName());
        System.out.println("userTarget: " + userTarget);

        var todos = userService.findTodos(authService.userName(), userTarget);

        return todos
                .map(Response::ok)
                .getOrElseGet(error -> Response.status(Response.Status.BAD_REQUEST).entity(error))
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

        var userTarget =  userName.orElse(authService.userName());
        logger.log(Logger.Level.INFO, "userTarget: " + userTarget);
        return userService.find(authService.userName(), userTarget)
                .flatMap(eitherUser -> {
                    // Extract UserEntity
                    return eitherUser
                            .map(Either::<String, User>right)
                            .orElse(Either.left("User not found"));
                })
                .flatMap(user -> {
                    // Adding TodoEntity to UserEntity
                    return userService.addTodo(user, todoToAdd);
                })
                .map(eitherTodo -> Response.created(URI.create(String.format("/api/todos/%s", eitherTodo.id()))).entity(eitherTodo))
                .getOrElseGet(error -> Response.status(Response.Status.BAD_REQUEST).entity(error))
                .build();
    }

}
