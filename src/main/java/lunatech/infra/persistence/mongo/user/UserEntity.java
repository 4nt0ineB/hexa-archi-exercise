package lunatech.infra.persistence.mongo.user;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@MongoEntity(collection = "users")
public class UserEntity extends PanacheMongoEntity {

    public String username;
    public String password;
    public String role;

    public UserEntity() {}

    public UserEntity(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}