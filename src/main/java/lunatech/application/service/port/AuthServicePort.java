package lunatech.application.service.port;

import io.vavr.control.Either;
import lunatech.application.service.dto.UserInfo;

import java.util.Optional;

public interface AuthServicePort {
    Optional<UserInfo> authenticate(String username, String password);
}
