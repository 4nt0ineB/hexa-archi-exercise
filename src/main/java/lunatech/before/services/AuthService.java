package lunatech.before.services;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import lunatech.before.entities.UserEntity;

import java.util.Optional;

@ApplicationScoped
public class AuthService {
//
//    @Context
//    SecurityIdentity identity;
//
//
//    public String userName() {
//        return identity.getPrincipal().getName();
//    }
//
//    public Optional<UserEntity> authenticate(String username, String password) {
//        Optional<UserEntity> user = UserEntity.find("username = ?1 and password = ?2", username, password).firstResultOptional();
//        return user;
//    }


}
