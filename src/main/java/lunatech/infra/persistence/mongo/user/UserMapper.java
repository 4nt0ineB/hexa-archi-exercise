package lunatech.infra.persistence.mongo.user;

import lunatech.domain.user.Role;
import lunatech.domain.user.User;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        return new UserEntity(user.username(),
                user.password(),
                user.role().name());
    }

    public static User toDomain(UserEntity userEntity) {
        return new User(userEntity.username, userEntity.password, Role.valueOf(userEntity.role));
    }
}
