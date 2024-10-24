package lunatech.infra.persistence.mongo.user;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.DecimalMin;
import lunatech.domain.user.User;
import lunatech.domain.user.UserRepositoryPort;

import java.util.Optional;

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
