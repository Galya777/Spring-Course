package com.example.cookingrecipes.payload.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateRecipeRequest {
    @Size(max = 80)
    private String name;

    @Size(max = 256)
    private String description;

    @Min(1)
    private Integer preparationTime; // in minutes

    private List<@Size(min = 1, max = 100) String> ingredients;

    @Size(max = 2048)
    private String instructions;

    private List<@Size(max = 30) String> tags;
}
