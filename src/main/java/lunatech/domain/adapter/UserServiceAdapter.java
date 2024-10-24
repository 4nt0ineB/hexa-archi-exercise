package lunatech.domain.adapter;

import io.vavr.control.Either;
import lunatech.domain.PermissionManager;
import lunatech.domain.dto.UserInfo;
import lunatech.domain.port.UserRepositoryPort;
import lunatech.domain.port.UserServicePort;

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
        var infoOrigin = userRepository.get(origin)
                .map(user -> new UserInfo(user.username(), user.role()));
        if (infoOrigin.isEmpty()) {
            return Either.left(origin + " not found");
        }
        var infoTarget = userRepository.get(target)
                .map(user -> new UserInfo(user.username(), user.role()));
        if (infoTarget.isEmpty()) {
            return Either.left(target + " not found");
        }
        return permissionManager.canSee(infoOrigin.get(), infoTarget.get());
    }
}


