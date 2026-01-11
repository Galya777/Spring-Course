package com.example.homework2.model;

import com.example.homework2.model.enums.AccountStatus;
import com.example.homework2.model.enums.Gender;
import com.example.homework2.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "displayName")
       })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Display name is required")
    @Size(max = 50, message = "Display name must be at most 50 characters")
    @Column(nullable = false, length = 50)
    private String displayName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers and underscores")
    @Column(nullable = false, unique = true, length = 15)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).*$",
             message = "Password must contain at least one digit and one special character")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(columnDefinition = "TEXT")
    private String profilePictureUrl;

    @Size(max = 512, message = "Bio must be at most 512 characters")
    @Column(length = 512)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Default constructor for JPA
    public User() {
    }

    // Business constructor
    public User(String displayName, String username, String email, String password, Gender gender) {
        this.displayName = displayName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }
}
