package lunatech.domain.permission;

import lunatech.domain.user.Role;
import lunatech.domain.user.UserInfo;

public class PermissionManager {

    public boolean hasRightsOver(UserInfo origin, UserInfo target) {
        return origin.username().equals(target.username())
                || origin.role().equals(Role.ADMIN);
    }

}
