package com.blogy.restful.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;
import java.util.List;

import com.blogy.restful.dto.CommentDto;
import com.blogy.restful.dto.NewCommentDto;
import com.blogy.restful.model.Comment;
import com.blogy.restful.service.CommentService;


@RestController
@RequestMapping("/post")
public class CommentRest {
    private final CommentService commentService;

    @Autowired
    public CommentRest(CommentService commentService){
        this.commentService=commentService;
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Long> createNewComment(@PathVariable Long id,@Valid @RequestBody NewCommentDto newCommentDto) {
        newCommentDto.setPostId(1L); // my post default id
        newCommentDto.setCreationDate(new Date());
        Long idResult =  commentService.addComment(newCommentDto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(idResult);
            
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> findCommentsByPostID(@PathVariable Long id) {
     
        List<Comment> result = commentService.findAllByPostID(id);

        return result.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(result);
       
    }

    @PutMapping("/{id}/comments")
    public ResponseEntity<Long> updateComment(@PathVariable Long id, @Valid @RequestBody CommentDto commentDto) {
        return commentService.findById(id)
      .map(oldComment -> {
        oldComment.setUpdateDate(new Date());
        oldComment.setContent(commentDto.getContent());
        return ResponseEntity.ok(commentService.save(oldComment).getId());
      })
      .orElseGet(() -> {
        return ResponseEntity.badRequest().build();
      });
       
    }

    @DeleteMapping("/{id}/comments")
    public ResponseEntity deleteComment(@PathVariable Long id) {
        if (!commentService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }

        commentService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
