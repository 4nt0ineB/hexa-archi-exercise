package lunatech.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lunatech.domain.adapter.AuthServiceAdapter;
import lunatech.domain.adapter.UserServiceAdapter;
import lunatech.domain.port.AuthServicePort;
import lunatech.domain.port.UserRepositoryPort;
import lunatech.domain.port.UserServicePort;

@ApplicationScoped
public class BeanProducer {

    @Inject
    UserRepositoryPort orderRepository;

    @Produces
    @ApplicationScoped
    public UserServicePort orderService() {
        return new UserServiceAdapter(orderRepository);
    }

    @Produces
    @ApplicationScoped
    public AuthServicePort authService() {
        return new AuthServiceAdapter(orderRepository);
    }


}