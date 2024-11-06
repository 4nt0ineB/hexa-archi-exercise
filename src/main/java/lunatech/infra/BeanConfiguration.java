package lunatech.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import lunatech.domain.auth.AuthServicePort;
import lunatech.domain.permission.PermissionManager;
import lunatech.domain.auth.AuthServiceAdapter;
import lunatech.domain.todo.TodoRepositoryPort;
import lunatech.domain.todo.TodoServiceAdapter;
import lunatech.domain.user.UserRepositoryPort;
import lunatech.domain.user.UserServiceAdapter;
import lunatech.domain.todo.TodoServicePort;
import lunatech.domain.user.UserServicePort;


@ApplicationScoped
public class BeanConfiguration {

    @Inject
    Validator validator;

    @Inject
    UserRepositoryPort userRepository;
    @Inject
    TodoRepositoryPort todoRepository;
    @Inject
    PermissionManager permissionManager;
    @Inject
    UserServicePort userService;
    @Inject
    TodoServicePort todoService;

    @Produces
    @ApplicationScoped
    public PermissionManager permissionManager() {
        return new PermissionManager(userRepository);
    }

    @Produces
    @ApplicationScoped
    public UserServicePort orderService() {
        return new UserServiceAdapter(userRepository);
    }

    @Produces
    @ApplicationScoped
    public TodoServicePort todoService() {
        return new TodoServiceAdapter(todoRepository, validator);
    }

    @Produces
    @ApplicationScoped
    public AuthServicePort authService() {
        return new AuthServiceAdapter(userRepository);
    }

}