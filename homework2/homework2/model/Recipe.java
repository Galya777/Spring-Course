package com.example.homework2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @NotBlank(message = "Recipe name is required")
    @Size(max = 80, message = "Recipe name must be at most 80 characters")
    @Column(nullable = false, length = 80)
    private String name;

    @Size(max = 256, message = "Short description must be at most 256 characters")
    @Column(length = 256)
    private String shortDescription;

    @Min(value = 1, message = "Preparation time must be at least 1 minute")
    @Column(nullable = false)
    private Integer preparationTime; // in minutes

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;

    @NotBlank(message = "Image URL is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Size(max = 2048, message = "Detailed description must be at most 2048 characters")
    @Column(columnDefinition = "TEXT")
    private String detailedDescription;

    @ElementCollection
    @CollectionTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "tag")
    private List<String> tags;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Default constructor for JPA
    public Recipe() {
    }

    // Business constructor
    public Recipe(User author, String name, String shortDescription, 
                 Integer preparationTime, List<String> ingredients, 
                 String imageUrl, String detailedDescription, List<String> tags) {
        this.author = author;
        this.name = name;
        this.shortDescription = shortDescription;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
        this.detailedDescription = detailedDescription;
        this.tags = tags;
    }
}
