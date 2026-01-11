package com.example.cookingrecipes.repository;

import com.example.cookingrecipes.model.Recipe;
import com.example.cookingrecipes.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findByUser(User user, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR EXISTS (SELECT t FROM r.tags t WHERE LOWER(t) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Recipe> searchRecipes(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r WHERE r.user = :user AND " +
           "(LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR EXISTS (SELECT t FROM r.tags t WHERE LOWER(t) LIKE LOWER(CONCAT('%', :query, '%'))))")
    Page<Recipe> searchUserRecipes(@Param("user") User user, @Param("query") String query, Pageable pageable);
    
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.tags t WHERE t IN :tags")
    Page<Recipe> findByTags(@Param("tags") List<String> tags, Pageable pageable);
    
    @Query("SELECT r FROM Recipe r ORDER BY r.createdAt DESC")
    Page<Recipe> findRecentRecipes(Pageable pageable);
}
