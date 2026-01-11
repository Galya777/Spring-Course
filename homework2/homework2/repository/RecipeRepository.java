package com.example.homework2.repository;

import com.example.homework2.model.Recipe;
import com.example.homework2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthorId(Long authorId);
    
    @Query("SELECT r FROM Recipe r WHERE r.author = :author AND r.id = :recipeId")
    Optional<Recipe> findByIdAndAuthor(@Param("recipeId") Long recipeId, @Param("author") User author);
    
    boolean existsByIdAndAuthorId(Long id, Long authorId);
    
    @Query("SELECT r FROM Recipe r WHERE " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.shortDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "EXISTS (SELECT t FROM r.tags t WHERE LOWER(t) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Recipe> searchRecipes(@Param("query") String query);
}
