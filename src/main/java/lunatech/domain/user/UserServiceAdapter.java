package lunatech.domain.user;

import io.vavr.control.Either;
import lunatech.domain.permission.ForbiddenActionException;
import lunatech.domain.permission.PermissionManager;
import org.jboss.logging.Logger;

public class UserServiceAdapter implements UserServicePort {

    private static final Logger logger = Logger.getLogger(UserServiceAdapter.class);

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
    public UserOutput find(String origin, String target) {
        logAction("Finding user", origin, target);
        var originUser = getUserInfo(origin);
        var targetUser = getUserInfo(target);
        if(!permissionManager.hasRightsOver(originUser, targetUser)) {
            throw new ForbiddenActionException("");
        }
        return targetUser;
    }

    @Override
    public Either<String, UserOutput> create(User u) {
        logAction("Creating user", u.username(), u.username());
        return userRepository.get(u.username())
                .map(user -> Either.<String, UserOutput>left("User already exists"))
                .orElse(userRepository.save(u)
                        .map(user -> Either.<String, UserOutput>right(UserOutput.from(user)))
                        .orElse(Either.left("User could not be saved"))
                );
    }

    private UserOutput getUserInfo(String username) {
        return userRepository.get(username)
                .map(UserOutput::from)
                .orElseThrow(() -> new UnknownUserException(username));
    }

    private void logAction(String msg, String origin, String target, Object... args) {
        var prefix = "(%s" + (origin.equals(target) ? "" : " as %s") + ") ";
        logger.infof(prefix + msg, origin, target, args);
    }
}


