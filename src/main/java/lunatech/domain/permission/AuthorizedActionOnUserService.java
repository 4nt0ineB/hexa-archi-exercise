package lunatech.domain.permission;

import lunatech.domain.user.UserServicePort;

@FunctionalInterface
public interface AuthorizedActionOnUserService<T> {
    T apply(Context context, UserServicePort userService);
}
