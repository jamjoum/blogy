package com.blogy.restful.service;


import java.util.List;
import java.util.Optional;

import com.blogy.restful.dto.NewCommentDto;
import com.blogy.restful.model.Comment;


public interface  CommentService {
    
    List<Comment> findAll() ;
    List<Comment> findAllByPostID(Long id) ;
    Optional<Comment> findById(Long id);
    Comment save(Comment comment) ;
    Long addComment(NewCommentDto newComment);
    void deleteById(Long id) ;
}
