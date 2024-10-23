package lunatech.infra.persistence.mongo.entities;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@MongoEntity(collection = "todos")
public class TodoEntity {
    @BsonId
    public UUID todoId;
    @NotNull
    @NotBlank
    public String title;
    @NotNull
    public String description;
    @NotNull
    public List<String> tags;
    public boolean done;
}
