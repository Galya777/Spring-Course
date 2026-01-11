package com.example.cookingrecipes.payload.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreateRecipeRequest {
    @NotBlank
    @Size(max = 80)
    private String name;

    @NotBlank
    @Size(max = 256)
    private String description;

    @NotNull
    @Min(1)
    private Integer preparationTime; // in minutes

    @Size(min = 1, message = "At least one ingredient is required")
    private List<@NotBlank String> ingredients;

    @NotBlank
    @Size(max = 2048)
    private String instructions;

    private List<@NotBlank String> tags;
}
