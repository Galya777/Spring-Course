package com.example.cookingrecipes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "displayName")
})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Display name is required")
    @Size(max = 30, message = "Display name cannot be longer than 30 characters")
    @Column(name = "display_name", nullable = false, length = 30)
    private String displayName;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    @Pattern(regexp = "^\\w+$", message = "Username can only contain letters, numbers, and underscores")
    @Column(nullable = false, unique = true, length = 15)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).*$", 
             message = "Password must contain at least one number and one special character")
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender = Gender.MALE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserRole role = UserRole.USER;
    
    @Column(name = "image_path")
    private String imagePath;
    
    @Size(max = 512, message = "Bio cannot be longer than 512 characters")
    @Column(length = 512)
    private String bio;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    public enum UserRole {
        USER, ADMIN
    }
    
    public enum AccountStatus {
        ACTIVE, SUSPENDED, DEACTIVATED
    }
}
