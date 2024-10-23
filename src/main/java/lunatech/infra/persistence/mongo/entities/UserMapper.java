package lunatech.infra.persistence.mongo.entities;

import lunatech.domain.model.Role;
import lunatech.domain.model.User;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.username = user.username();
        userEntity.password = user.password();
        userEntity.role = user.role().name();
        user.todos().forEach(todo -> userEntity.todos.add(TodoMapper.toEntity(todo)));
        return userEntity;
    }

    public static User toDomain(UserEntity userEntity) {
        User user = new User(userEntity.username, userEntity.password, Role.valueOf(userEntity.role));
        userEntity.todos.forEach(todoEntity -> user.addTodoToUser(TodoMapper.toDomain(todoEntity)));
        return user;
    }
}
