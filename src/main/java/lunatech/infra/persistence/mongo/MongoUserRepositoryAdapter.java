package lunatech.infra.persistence.mongo;

import io.quarkus.arc.profile.IfBuildProfile;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import lunatech.infra.persistence.mongo.entities.TodoMapper;
import lunatech.infra.persistence.mongo.entities.UserEntity;
import lunatech.infra.persistence.mongo.entities.UserMapper;

import java.util.Optional;
import java.util.UUID;

@Singleton
@IfBuildProfile("dev")
public class MongoUserRepositoryAdapter implements UserRepositoryPort {

    @Override
    public Optional<User> get(String username) {
        return UserEntity.<UserEntity>find("username", username)
                .firstResultOptional()
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> save(User user) {
        var entity = UserMapper.toEntity(user);
        UserEntity.persist(entity);
        return Optional.of(UserMapper.toDomain(entity));
    }
}
