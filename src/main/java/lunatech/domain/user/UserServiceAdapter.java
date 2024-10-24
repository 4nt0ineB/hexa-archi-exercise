package lunatech.domain.user;

import io.vavr.control.Either;
import lunatech.domain.PermissionManager;

public class UserServiceAdapter implements UserServicePort {

    private final PermissionManager permissionManager;
    public UserServiceAdapter(
            PermissionManager permissionManager)
    {
        this.permissionManager = permissionManager;
    }

    @Override
    public Either<String, UserInfo> find(String origin, String target) {
        return permissionManager.canSee(origin, target);
    }
}


