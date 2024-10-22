package lunatech.before.entities;

import com.fasterxml.classmate.ResolvedType;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.smallrye.common.constraint.NotNull;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.util.List;


@MongoEntity(collection = "todos")
public class TodoEntity {

    public ObjectId todoId;

    @NotNull
    public String description;
    @NotNull
    public List<String> tags;
    public boolean done;

    public TodoEntity() {
        this.todoId = new ObjectId();
    }
}
