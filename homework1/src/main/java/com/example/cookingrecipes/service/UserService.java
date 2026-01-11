package com.example.cookingrecipes.service;

import com.example.cookingrecipes.exception.ResourceNotFoundException;
import com.example.cookingrecipes.model.User;
import com.example.cookingrecipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Path fileStorageLocation;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageLocation = Paths.get("./uploads/users").toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the upload directory", ex);
        }
    }

    public User createUser(User user, MultipartFile imageFile) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByDisplayName(user.getDisplayName())) {
            throw new IllegalArgumentException("Display name is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = storeFile(imageFile);
            user.setImagePath(fileName);
        } else {
            // Set default avatar based on gender
            String defaultAvatar = user.getGender() == User.Gender.FEMALE ? 
                "/images/default-female-avatar.png" : "/images/default-male-avatar.png";
            user.setImagePath(defaultAvatar);
        }

        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails, MultipartFile imageFile) {
        User user = getUserById(id);
        
        if (!user.getUsername().equals(userDetails.getUsername()) && 
            userRepository.existsByUsername(userDetails.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        if (!user.getDisplayName().equals(userDetails.getDisplayName()) && 
            userRepository.existsByDisplayName(userDetails.getDisplayName())) {
            throw new IllegalArgumentException("Display name is already taken");
        }

        user.setUsername(userDetails.getUsername());
        user.setDisplayName(userDetails.getDisplayName());
        user.setBio(userDetails.getBio());
        user.setGender(userDetails.getGender());
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if it's not a default avatar
            if (user.getImagePath() != null && 
                !user.getImagePath().startsWith("/images/default-")) {
                deleteFile(user.getImagePath());
            }
            String fileName = storeFile(imageFile);
            user.setImagePath(fileName);
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        // Delete user's image if it's not a default avatar
        if (user.getImagePath() != null && 
            !user.getImagePath().startsWith("/images/default-")) {
            deleteFile(user.getImagePath());
        }
        userRepository.delete(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUserStatus(Long id, User.AccountStatus status) {
        User user = getUserById(id);
        user.setStatus(status);
        return userRepository.save(user);
    }

    private String storeFile(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = "user_" + UUID.randomUUID().toString() + fileExtension;
            
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return "/uploads/users/" + fileName;
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
