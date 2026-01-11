package com.example.miniexam1.repository;

import com.example.miniexam1.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByUpdatedAtDesc();
    List<Comment> findByStatusOrderByUpdatedAtDesc(Comment.CommentStatus status);
}
