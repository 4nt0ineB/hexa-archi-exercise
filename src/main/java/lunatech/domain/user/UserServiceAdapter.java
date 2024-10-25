package lunatech.domain.user;

import io.vavr.control.Either;
import lunatech.domain.permission.ForbiddenActionException;
import lunatech.domain.permission.PermissionManager;

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
    public UserInfo find(String origin, String target) {
        var originUser = getUserInfo(origin);
        var targetUser = getUserInfo(target);
        if(!permissionManager.hasRightsOver(originUser, targetUser)) {
            throw new ForbiddenActionException("");
        }
        return targetUser;
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

    private UserInfo getUserInfo(String username) {
        return userRepository.get(username)
                .map(UserInfo::from)
                .orElseThrow(() -> new UnknownUserException(username));
    }
}


