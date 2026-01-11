package com.example.cookingrecipes.payload.response;

import com.example.cookingrecipes.model.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserProfileResponse {
    private final Long id;
    private final String username;
    private final String displayName;
    private final User.Gender gender;
    private final String imagePath;
    private final String bio;
    private final User.AccountStatus status;
    private final User.UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserProfileResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.gender = user.getGender();
        this.imagePath = user.getImagePath();
        this.bio = user.getBio();
        this.status = user.getStatus();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
