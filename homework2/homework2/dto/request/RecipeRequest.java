package homework2.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class RecipeRequest {
    @NotBlank
    @Size(max = 80)
    private String name;

    @Size(max = 256)
    private String shortDescription;

    @Min(1)
    @NotNull
    private Integer preparationTime; // in minutes

    @NotEmpty
    private List<String> ingredients;

    @NotBlank
    private String imageUrl;

    @Size(max = 2048)
    private String detailedDescription;

    private List<String> tags;
}
