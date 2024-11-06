package lunatech.application.interceptors;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import lunatech.domain.permission.Context;
import lunatech.domain.permission.PermissionManager;
import lunatech.infra.security.SecurityService;

import java.util.Optional;

// https://docs.redhat.com/en/documentation/red_hat_fuse/7.4/html/apache_cxf_development_guide/jaxrs20filters#JAXRS20Filters-ServerRequestFilter
@Provider
@Priority(1)
/**
 * This filter is responsible to create the domain-context of the request.
 * This take place after the authentication and before the resource is called.
 * When the context is build (thus validated by the perm manager) it is stored in the request context.
 */
public class ContextAccess implements ContainerRequestFilter {

    @Inject
    SecurityService securityService;
    @Inject
    PermissionManager permissionManager;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var usurpedUser = Optional.ofNullable(requestContext.getUriInfo().getQueryParameters().getFirst("user"));
        requestContext.setProperty("context", accessFor(usurpedUser));
    }

    private Context accessFor(Optional<String> username) {
        var userTarget = username.orElse(securityService.userName());
        return permissionManager
                .as(securityService.userName())
                .impersonate(userTarget)
                // check permissions and returns the context
                .getAccess();
    }
}
