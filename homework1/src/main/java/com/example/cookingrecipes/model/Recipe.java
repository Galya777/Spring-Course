package com.example.cookingrecipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Recipe name is required")
    @Size(max = 80, message = "Recipe name cannot be longer than 80 characters")
    @Column(nullable = false, length = 80)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 256, message = "Description cannot be longer than 256 characters")
    @Column(nullable = false, length = 256)
    private String description;

    @NotNull(message = "Preparation time is required")
    @Min(value = 1, message = "Preparation time must be at least 1 minute")
    @Column(name = "prep_time", nullable = false)
    private Integer preparationTime; // in minutes

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient", nullable = false)
    @Size(min = 1, message = "At least one ingredient is required")
    private List<String> ingredients = new ArrayList<>();

    @NotBlank(message = "Image is required")
    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @NotBlank(message = "Instructions are required")
    @Size(max = 2048, message = "Instructions cannot be longer than 2048 characters")
    @Column(nullable = false, length = 2048)
    private String instructions;

    @ElementCollection
    @CollectionTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "tag", nullable = false)
    private List<String> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }
}
