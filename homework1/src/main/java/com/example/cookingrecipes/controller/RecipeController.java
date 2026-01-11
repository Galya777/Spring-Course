package com.example.cookingrecipes.controller;

import com.example.cookingrecipes.model.Recipe;
import com.example.cookingrecipes.model.User;
import com.example.cookingrecipes.payload.request.CreateRecipeRequest;
import com.example.cookingrecipes.payload.request.UpdateRecipeRequest;
import com.example.cookingrecipes.payload.response.RecipeResponse;
import com.example.cookingrecipes.security.CurrentUser;
import com.example.cookingrecipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public Page<RecipeResponse> getAllRecipes(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) List<String> tags,
            Pageable pageable) {
        
        if (query != null && !query.isEmpty()) {
            return recipeService.searchRecipes(query, pageable)
                    .map(RecipeResponse::new);
        } else if (tags != null && !tags.isEmpty()) {
            return recipeService.findRecipesByTags(tags, pageable)
                    .map(RecipeResponse::new);
        } else {
            return recipeService.findRecentRecipes(pageable)
                    .map(RecipeResponse::new);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RecipeResponse createRecipe(
            @Valid @ModelAttribute CreateRecipeRequest request,
            @RequestParam("image") MultipartFile imageFile,
            @CurrentUser User currentUser) {
        
        Recipe recipe = recipeService.createRecipe(request, imageFile, currentUser);
        return new RecipeResponse(recipe);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(new RecipeResponse(recipe));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateRecipeRequest request,
            @RequestParam(required = false) MultipartFile imageFile,
            @CurrentUser User currentUser) {
        
        if (!recipeService.isRecipeOwner(id, currentUser.getId()) && 
            currentUser.getRole() != User.UserRole.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        Recipe updatedRecipe = recipeService.updateRecipe(id, request, imageFile);
        return ResponseEntity.ok(new RecipeResponse(updatedRecipe));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRecipe(
            @PathVariable Long id,
            @CurrentUser User currentUser) {
        
        if (!recipeService.isRecipeOwner(id, currentUser.getId()) && 
            currentUser.getRole() != User.UserRole.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        recipeService.deleteRecipe(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{username}")
    public Page<RecipeResponse> getUserRecipes(
            @PathVariable String username,
            @RequestParam(required = false) String query,
            Pageable pageable) {
        
        if (query != null && !query.isEmpty()) {
            return recipeService.searchUserRecipes(username, query, pageable)
                    .map(RecipeResponse::new);
        } else {
            return recipeService.getUserRecipes(username, pageable)
                    .map(RecipeResponse::new);
        }
    }
}
