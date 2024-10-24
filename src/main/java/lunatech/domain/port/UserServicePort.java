package lunatech.domain.port;

import io.vavr.control.Either;
import lunatech.domain.dto.UserInfo;

public interface UserServicePort {
    Either<String, UserInfo> find(String origin, String target);
}
