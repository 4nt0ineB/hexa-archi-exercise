package lunatech.domain.user;

import io.vavr.control.Either;
import lunatech.domain.PermissionManager;

public class UserServiceAdapter implements UserServicePort {

    private final PermissionManager permissionManager;
    private final UserRepositoryPort userRepository;

    public UserServiceAdapter(
            UserRepositoryPort userRepository,
            PermissionManager permissionManager)
    {
        this.userRepository = userRepository;
        this.permissionManager = permissionManager;
    }

    @Override
    public Either<String, UserInfo> find(String origin, String target) {
        return permissionManager.canSee(origin, target);
    }

    @Override
    public Either<String, UserInfo> create(User u) {
        return userRepository.get(u.username())
                .map(user -> Either.<String, UserInfo>left("User already exists"))
                .orElse(userRepository.save(u)
                        .map(user -> Either.<String, UserInfo>right(UserInfo.from(user)))
                        .orElse(Either.left("User could not be saved"))
                );
    }
}


