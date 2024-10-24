package lunatech.infra.security;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lunatech.domain.auth.AuthServicePort;


// https://quarkus.io/guides/security-basic-authentication-howto
// https://quarkus.io/guides/security-identity-providers
// https://stackoverflow.com/questions/74621459/using-a-custom-identity-provider-in-quarkus
@ApplicationScoped
public class UserIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {

    @Inject
    AuthServicePort authService;

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(
            UsernamePasswordAuthenticationRequest request,
            AuthenticationRequestContext context) {
        return authService.authenticate(request.getUsername(), String.valueOf(request.getPassword().getPassword()))
                .map(userInfo -> Uni.createFrom().item((SecurityIdentity) QuarkusSecurityIdentity.builder()
                        .setPrincipal(new QuarkusPrincipal(userInfo.username()))
                        .addCredential(request.getPassword())
                        .setAnonymous(false)
                        .addRole(userInfo.role().name())
                        .build()))
                .orElseThrow(() -> new AuthenticationFailedException("Couldn't authenticate"));
    }
}
