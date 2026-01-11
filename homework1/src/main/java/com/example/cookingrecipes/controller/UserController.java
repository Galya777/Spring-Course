package com.example.cookingrecipes.controller;

import com.example.cookingrecipes.model.User;
import com.example.cookingrecipes.payload.request.UpdateUserRequest;
import com.example.cookingrecipes.payload.response.UserProfileResponse;
import com.example.cookingrecipes.security.CurrentUser;
import com.example.cookingrecipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@CurrentUser User currentUser) {
        User user = userService.getUserById(currentUser.getId());
        return ResponseEntity.ok(new UserProfileResponse(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserProfileResponse> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable)
                .map(UserProfileResponse::new);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new UserProfileResponse(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateUserRequest updateRequest,
            @RequestParam(required = false) MultipartFile imageFile,
            @CurrentUser User currentUser) {
        
        if (!currentUser.getId().equals(id) && currentUser.getRole() != User.UserRole.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        User updatedUser = userService.updateUser(id, updateRequest, imageFile);
        return ResponseEntity.ok(new UserProfileResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> updateUserStatus(
            @PathVariable Long id,
            @RequestParam User.AccountStatus status) {
        
        User updatedUser = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(new UserProfileResponse(updatedUser));
    }
}
