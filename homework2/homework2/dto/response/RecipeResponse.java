package homework2.dto.response;

import homework2.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecipeResponse {
    private Long id;
    private User author;
    private String name;
    private String shortDescription;
    private Integer preparationTime;
    private List<String> ingredients;
    private String imageUrl;
    private String detailedDescription;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecipeResponse(
            Long id, User author, String name, String shortDescription,
            Integer preparationTime, List<String> ingredients, String imageUrl,
            String detailedDescription, List<String> tags,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.shortDescription = shortDescription;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
        this.detailedDescription = detailedDescription;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
