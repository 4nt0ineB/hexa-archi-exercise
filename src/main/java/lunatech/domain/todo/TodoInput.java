package lunatech.domain.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

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
public record TodoInput(
        @NotNull
        @NotBlank
        String title,
        @NotNull
        String description,
        @NotNull
        List<String> tags
) {
}
