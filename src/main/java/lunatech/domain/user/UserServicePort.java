package lunatech.domain.user;

import io.vavr.control.Either;

public interface UserServicePort {
    UserOutput find(String origin, String target);
    Either<String, UserOutput> create(User u);
}
