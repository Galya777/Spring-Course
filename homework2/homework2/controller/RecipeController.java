package homework2.controller;

import homework2.dto.request.RecipeRequest;
import homework2.dto.response.RecipeResponse;
import homework2.exception.ResourceNotFoundException;
import homework2.model.Recipe;
import homework2.model.User;
import homework2.repository.RecipeRepository;
import homework2.repository.UserRepository;
import homework2.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RecipeResponse getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));
        return convertToDto(recipe);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RecipeResponse createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        User currentUser = getCurrentUser();
        
        Recipe recipe = new Recipe();
        recipe.setAuthor(currentUser);
        recipe.setName(recipeRequest.getName());
        recipe.setShortDescription(recipeRequest.getShortDescription());
        recipe.setPreparationTime(recipeRequest.getPreparationTime());
        recipe.setIngredients(recipeRequest.getIngredients());
        recipe.setImageUrl(recipeRequest.getImageUrl());
        recipe.setDetailedDescription(recipeRequest.getDetailedDescription());
        recipe.setTags(recipeRequest.getTags());
        
        Recipe savedRecipe = recipeRepository.save(recipe);
        return convertToDto(savedRecipe);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public RecipeResponse updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeRequest recipeRequest) {
        User currentUser = getCurrentUser();
        
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));
        
        // Check if the current user is the author or an admin
        if (!recipe.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().name().equals("ADMIN")) {
            throw new ResourceNotFoundException("Recipe", "id", id);
        }
        
        recipe.setName(recipeRequest.getName());
        recipe.setShortDescription(recipeRequest.getShortDescription());
        recipe.setPreparationTime(recipeRequest.getPreparationTime());
        recipe.setIngredients(recipeRequest.getIngredients());
        recipe.setImageUrl(recipeRequest.getImageUrl());
        recipe.setDetailedDescription(recipeRequest.getDetailedDescription());
        recipe.setTags(recipeRequest.getTags());
        
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return convertToDto(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));
        
        // Check if the current user is the author or an admin
        if (!recipe.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().name().equals("ADMIN")) {
            throw new ResourceNotFoundException("Recipe", "id", id);
        }
        
        recipeRepository.delete(recipe);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public List<RecipeResponse> getRecipesByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return recipeRepository.findByAuthor(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));
    }

    private RecipeResponse convertToDto(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getAuthor(),
                recipe.getName(),
                recipe.getShortDescription(),
                recipe.getPreparationTime(),
                recipe.getIngredients(),
                recipe.getImageUrl(),
                recipe.getDetailedDescription(),
                recipe.getTags(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt()
        );
    }
}
