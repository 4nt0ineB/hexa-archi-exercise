package lunatech.domain.auth;

import lunatech.domain.user.UserOutput;

import java.util.Optional;

public interface AuthServicePort {
    Optional<UserOutput> authenticate(String username, String password);
}
