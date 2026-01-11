package com.example.miniexam1.service;

import com.example.miniexam1.exception.ResourceNotFoundException;
import com.example.miniexam1.model.Comment;
import com.example.miniexam1.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final Path fileStorageLocation;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.fileStorageLocation = Paths.get("./uploads").toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the upload directory", ex);
        }
    }

    public List<Comment> getAllComments(String status) {
        if (status != null && !status.isEmpty()) {
            Comment.CommentStatus commentStatus = Comment.CommentStatus.valueOf(status.toUpperCase());
            return commentRepository.findByStatusOrderByUpdatedAtDesc(commentStatus);
        }
        return commentRepository.findAllByOrderByUpdatedAtDesc();
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }

    public Comment createComment(Comment comment, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = storeFile(imageFile);
            comment.setImagePath(fileName);
        }
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, Comment commentDetails, MultipartFile imageFile) {
        Comment comment = getCommentById(id);
        
        comment.setTitle(commentDetails.getTitle());
        comment.setContent(commentDetails.getContent());
        
        if (commentDetails.getStatus() != null) {
            comment.setStatus(commentDetails.getStatus());
        }
        
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (comment.getImagePath() != null) {
                deleteFile(comment.getImagePath());
            }
            String fileName = storeFile(imageFile);
            comment.setImagePath(fileName);
        }
        
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        Comment comment = getCommentById(id);
        if (comment.getImagePath() != null) {
            deleteFile(comment.getImagePath());
        }
        commentRepository.delete(comment);
    }

    public Comment updateCommentStatus(Long id, Comment.CommentStatus status) {
        Comment comment = getCommentById(id);
        comment.setStatus(status);
        return commentRepository.save(comment);
    }

    private String storeFile(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;
            
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }
    
    private void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + fileName, ex);
        }
    }
}
