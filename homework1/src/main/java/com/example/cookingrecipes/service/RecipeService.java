package com.example.cookingrecipes.service;

import com.example.cookingrecipes.exception.ResourceNotFoundException;
import com.example.cookingrecipes.model.Recipe;
import com.example.cookingrecipes.model.User;
import com.example.cookingrecipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final Path fileStorageLocation;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        this.fileStorageLocation = Paths.get("./uploads/recipes").toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the upload directory", ex);
        }
    }

    public Recipe createRecipe(Recipe recipe, MultipartFile imageFile, User user) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Recipe image is required");
        }

        String fileName = storeFile(imageFile);
        recipe.setImagePath(fileName);
        recipe.setUser(user);

        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long id, Recipe recipeDetails, MultipartFile imageFile) {
        Recipe recipe = getRecipeById(id);
        
        recipe.setName(recipeDetails.getName());
        recipe.setDescription(recipeDetails.getDescription());
        recipe.setPreparationTime(recipeDetails.getPreparationTime());
        recipe.setInstructions(recipeDetails.getInstructions());
        
        // Update ingredients
        recipe.getIngredients().clear();
        recipe.getIngredients().addAll(recipeDetails.getIngredients());
        
        // Update tags
        recipe.getTags().clear();
        recipe.getTags().addAll(recipeDetails.getTags());
        
        // Update image if new one is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image
            deleteFile(recipe.getImagePath());
            String fileName = storeFile(imageFile);
            recipe.setImagePath(fileName);
        }

        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        // Delete recipe image
        deleteFile(recipe.getImagePath());
        recipeRepository.delete(recipe);
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));
    }

    public Page<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public Page<Recipe> getUserRecipes(User user, Pageable pageable) {
        return recipeRepository.findByUser(user, pageable);
    }

    public Page<Recipe> searchRecipes(String query, Pageable pageable) {
        return recipeRepository.searchRecipes(query, pageable);
    }

    public Page<Recipe> searchUserRecipes(User user, String query, Pageable pageable) {
        return recipeRepository.searchUserRecipes(user, query, pageable);
    }

    public Page<Recipe> findRecipesByTags(List<String> tags, Pageable pageable) {
        return recipeRepository.findByTags(tags, pageable);
    }

    public Page<Recipe> findRecentRecipes(Pageable pageable) {
        return recipeRepository.findRecentRecipes(pageable);
    }

    public boolean isRecipeOwner(Long recipeId, Long userId) {
        return recipeRepository.existsByIdAndUserId(recipeId, userId);
    }

    private String storeFile(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = "recipe_" + UUID.randomUUID().toString() + fileExtension;
            
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return "/uploads/recipes/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }
    
    private void deleteFile(String filePath) {
        try {
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            Path fileToDelete = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(fileToDelete);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + filePath, ex);
        }
    }
}
