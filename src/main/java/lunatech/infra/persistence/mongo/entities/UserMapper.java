package lunatech.infra.persistence.mongo.entities;

import lunatech.domain.model.Role;
import lunatech.domain.model.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        return new UserEntity(user.username(),
                user.password(),
                user.role().name(),
                user.todos().stream().map(TodoMapper::toEntity).collect(Collectors.toList()));
    }

    public static User toDomain(UserEntity userEntity) {
        User user = new User(userEntity.username, userEntity.password, Role.valueOf(userEntity.role));
        userEntity.todos
                .stream()
                .map(TodoMapper::toDomain)
                .forEach(user::addTodo);
        return user;
    }
}
