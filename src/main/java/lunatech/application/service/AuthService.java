package lunatech.application.service;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import lunatech.domain.User;
import lunatech.infra.persistence.InMemoryUserRepositoryAdapter;
import org.jboss.logging.Logger;

import java.util.Optional;

@ApplicationScoped
public class AuthService {

    private static final Logger logger = Logger.getLogger(lunatech.application.service.AuthService.class);


    @Context
    SecurityIdentity identity;

    @Inject
    InMemoryUserRepositoryAdapter userRepositoryAdapter;

    public String userName() {
        return identity.getPrincipal().getName();
    }

    public Optional<User> authenticate(String username, String password) {
        logger.log(Logger.Level.INFO, "Authenticating user: " + username);
        Optional<User> user = userRepositoryAdapter.getByUsername(username);
        System.out.println("user: " + user);
        return user;
    }


}
