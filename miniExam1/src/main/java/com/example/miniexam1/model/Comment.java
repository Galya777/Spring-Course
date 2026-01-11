package com.example.miniexam1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 80, message = "Title must be between 2 and 80 characters")
    @Column(nullable = false, length = 80)
    private String title;
    
    @NotBlank(message = "Content is required")
    @Size(max = 512, message = "Content cannot exceed 512 characters")
    @Column(nullable = false, length = 512)
    private String content;
    
    @Column(name = "image_path")
    private String imagePath;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status = CommentStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum CommentStatus {
        ACTIVE, SUSPENDED
    }
}
