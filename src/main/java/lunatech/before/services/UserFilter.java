package lunatech.before.services;

import lunatech.before.entities.UserEntity;

@FunctionalInterface
public interface UserFilter {
    UserEntity filter(UserEntity user);
}
