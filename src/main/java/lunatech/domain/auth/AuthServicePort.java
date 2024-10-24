package lunatech.domain.auth;

import lunatech.domain.user.UserInfo;

import java.util.Optional;

public interface AuthServicePort {
    Optional<UserInfo> authenticate(String username, String password);
}
