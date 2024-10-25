package lunatech.domain.user;

import io.vavr.control.Either;

public interface UserServicePort {
    UserInfo find(String origin, String target);
    Either<String, UserInfo> create(User u);
}
