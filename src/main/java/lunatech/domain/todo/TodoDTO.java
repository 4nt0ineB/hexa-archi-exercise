package lunatech.domain.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.util.List;

@Schema(
        description = "Required fields to create a new Todo",
        example = """
        {
            "title": "Do the shopping",
            "description": "Buy some milk, eggs and bread",
            "tags": ["shopping", "food"]
        }
        """
)
public record TodoDTO(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        String description,
        @NotNull
        List<String> tags
) {
}
