package com.blogy.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.blogy.restful.dto.NewCommentDto;
import com.blogy.restful.model.Comment;
import com.blogy.restful.repository.CommentRepository;

@Service
public class CommentServiceImpl  implements CommentService{
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository=commentRepository;
    }
    
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
    public List<Comment> findAllByPostID(Long id) {
        return commentRepository.findByPostId(id);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
    
    @Override
    public Long addComment(NewCommentDto newCommentDto) {
        Comment newComment=new Comment();
        newComment.setPostId(newCommentDto.getPostId());
        newComment.setAuthor(newCommentDto.getAuthor());
        newComment.setContent(newCommentDto.getContent());
        newComment.setCreationDate(new Date());
        Long newId = commentRepository.save(newComment).getId();
        return newId;
    }
    
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
