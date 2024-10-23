package lunatech.domain.port;

import lunatech.domain.dto.UserInfo;

import java.util.Optional;

public interface AuthServicePort {
    Optional<UserInfo> authenticate(String username, String password);
}
