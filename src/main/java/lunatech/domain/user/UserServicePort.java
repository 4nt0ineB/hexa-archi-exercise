package lunatech.domain.user;

import io.vavr.control.Either;
import lunatech.domain.permission.Context;

public interface UserServicePort {
    UserOutput find(Context context);
    Either<String, UserOutput> create(User u);
    User delete(Context context);
}
