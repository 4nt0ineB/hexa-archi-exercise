package lunatech.infra.api;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lunatech.application.service.port.UserServicePort;
import lunatech.domain.model.Role;
import lunatech.domain.model.Todo;
import lunatech.infra.security.SecurityService;
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
        System.out.println("userTarget: " + userTarget);

        var todos = userService.findTodos(securityService.userName(), userTarget);

        return todos
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
        var userTarget =  userName.orElse(securityService.userName());
        return userService.addTodo(securityService.userName(), userTarget, todoToAdd)
                .map(eitherTodo -> Response.created(URI.create(String.format("/api/todos/%s", eitherTodo.id()))).entity(eitherTodo))
                .getOrElseGet(error -> Response.status(Response.Status.FORBIDDEN).entity(error))
                .build();
    }

}
