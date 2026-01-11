package com.example.cookingrecipes.payload.response;

import com.example.cookingrecipes.model.Recipe;
import com.example.cookingrecipes.model.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RecipeResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final Integer preparationTime;
    private final List<String> ingredients;
    private final String imagePath;
    private final String instructions;
    private final List<String> tags;
    private final UserProfileResponse author;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public RecipeResponse(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.preparationTime = recipe.getPreparationTime();
        this.ingredients = recipe.getIngredients();
        this.imagePath = recipe.getImagePath();
        this.instructions = recipe.getInstructions();
        this.tags = recipe.getTags();
        this.author = new UserProfileResponse(recipe.getUser());
        this.createdAt = recipe.getCreatedAt();
        this.updatedAt = recipe.getUpdatedAt();
    }
}
