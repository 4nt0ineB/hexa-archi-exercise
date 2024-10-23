package lunatech.infra.persistence.mongo.entities;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.List;

@MongoEntity(collection = "todos")
public class TodoEntity {

    public ObjectId todoId;
    @NotNull
    @NotBlank
    public String title;
    @NotNull
    public String description;
    @NotNull
    public List<String> tags;
    public boolean done;
    @NotNull


    public TodoEntity() {
        this.todoId = new ObjectId();
    }
}