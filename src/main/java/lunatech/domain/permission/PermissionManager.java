package lunatech.domain.permission;

import lunatech.domain.user.Role;
import lunatech.domain.user.UserOutput;

public class PermissionManager {

    public boolean hasRightsOver(UserOutput origin, UserOutput target) {
        return origin.username().equals(target.username())
                || origin.role().equals(Role.ADMIN);
    }

}
