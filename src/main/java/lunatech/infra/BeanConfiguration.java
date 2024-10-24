package lunatech.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lunatech.domain.PermissionManager;
import lunatech.domain.adapter.AuthServiceAdapter;
import lunatech.domain.adapter.TodoServiceAdapter;
import lunatech.domain.adapter.UserServiceAdapter;
import lunatech.domain.port.*;

@ApplicationScoped
public class BeanConfiguration {

    @Inject
    UserRepositoryPort userRepository;
    @Inject
    TodoRepositoryPort todoRepository;
    @Inject
    UserServicePort userService;
    private final PermissionManager permissionManager = new PermissionManager();


    @Produces
    @ApplicationScoped
    public UserServicePort orderService() {
        return new UserServiceAdapter(userRepository, permissionManager);
    }

    @Produces
    @ApplicationScoped
    public TodoServicePort todoService() {
        return new TodoServiceAdapter(todoRepository, userService);
    }

    @Produces
    @ApplicationScoped
    public AuthServicePort authService() {
        return new AuthServiceAdapter(userRepository);
    }


}