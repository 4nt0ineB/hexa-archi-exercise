package lunatech.domain;

import io.vavr.control.Either;
import lunatech.domain.dto.UserInfo;
import lunatech.domain.model.Role;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;

public class PermissionManager {

    /**
     * Checks if the origin user is authorized to access or modify the target user.
     *
     * @param origin the username of the initiator of the action
     * @param target the username of the target user
     * @return Either a string with an error message or the target user info
     */
    public Either<String, UserInfo> canSee(UserInfo origin, UserInfo target) {
        if (!origin.username().equals(target.username())
                && !origin.role().equals(Role.ADMIN)
        ) {
            return Either.left("Unauthorized access to user '" + target + "'");
        }
        return Either.right(target);
    }

}
