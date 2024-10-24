package lunatech.domain.dto;

import lunatech.domain.model.Role;
import lunatech.domain.model.User;

public record UserInfo(String username, Role role) {
    public static UserInfo from(User user) {
        return new UserInfo(user.username(), user.role());
    }
}
