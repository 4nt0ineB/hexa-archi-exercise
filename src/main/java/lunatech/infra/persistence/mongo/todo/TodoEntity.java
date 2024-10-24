package lunatech.infra.persistence.mongo.todo;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.List;

@MongoEntity(collection = "todos")
public class TodoEntity extends PanacheMongoEntityBase {
    @BsonId
    public String id;
    public String username;
    @NotBlank
    public String title;
    @NotNull
    public String description;
    @NotNull
    public List<String> tags;
    public boolean done;
}
