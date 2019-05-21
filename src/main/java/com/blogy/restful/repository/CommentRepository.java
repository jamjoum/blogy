package com.blogy.restful.repository;

import java.util.List;

import com.blogy.restful.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByPostId(Long postId);
}
