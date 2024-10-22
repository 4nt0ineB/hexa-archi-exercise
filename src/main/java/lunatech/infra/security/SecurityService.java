package lunatech.infra.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;

@ApplicationScoped
public class SecurityService {

    @Context
    SecurityIdentity identity;

    public String userName() {
        return identity.getPrincipal().getName();
    }

}
