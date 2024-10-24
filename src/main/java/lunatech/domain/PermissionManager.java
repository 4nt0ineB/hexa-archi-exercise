package lunatech.domain;

import io.vavr.control.Either;
import lunatech.domain.user.Role;
import lunatech.domain.user.UserInfo;
import lunatech.domain.user.User;
import lunatech.domain.user.UserRepositoryPort;

public class PermissionManager {

    private final UserRepositoryPort userRepository;

    public PermissionManager(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Checks if the origin user is authorized to access or modify the target user.
     *
     * @param origin the username of the initiator of the action
     * @param target the username of the target user
     * @return Either a string with an error message or the target user info
     */
    public Either<String, UserInfo> canSee(String origin, String target) {
        return userRepository.get(origin)
                .map(originUser -> userRepository.get(target)
                        .map(targetUser ->
                                hasRightsOver(originUser, targetUser)
                                        .flatMap(__ -> Either.right(UserInfo.from(targetUser)))
                        )
                        .orElse(Either.left("User '" + target + "' not found")))
                .orElse(Either.left("User '" + origin + "' not found"));
    }

    private Either<String, User> hasRightsOver(User origin, User target) {
        if (!origin.username().equals(target.username())
                && !origin.role().equals(Role.ADMIN)
        ) {
            return Either.left("Unauthorized access to user '" + target + "'");
        }
        return Either.right(target);
    }

}
