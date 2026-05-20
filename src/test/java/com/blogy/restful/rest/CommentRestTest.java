package com.blogy.restful.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.blogy.restful.dto.NewCommentDto;
import com.blogy.restful.model.Comment;
import com.blogy.restful.service.CommentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CommentRestTest {

   private MockMvc mvc;

   @Mock
   protected CommentService commentService;
   
   @BeforeEach
   public void setUp() {
      MockitoAnnotations.openMocks(this);
      this.mvc = MockMvcBuilders.standaloneSetup(new CommentRest(commentService)).build();
   }

   @Test
   public void getCommentsList() throws Exception {
      List<Comment> comments = new ArrayList<>();
      Comment comment = new Comment();
      comment.setId(1L);
      comment.setAuthor("rami");
      comment.setContent("test");
      comment.setPostId(1L);
      comments.add(comment);

      when(commentService.findAllByPostID(1L)).thenReturn(comments);

      mvc.perform(get("/post/1/comments").accept(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
         .andExpect(jsonPath("$[0].id", is(1)))
         .andExpect(jsonPath("$[0].content", is("test")))
         .andExpect(jsonPath("$[0].author", is("rami")));
   }

   @Test
   public void createComment() throws Exception {
      NewCommentDto newComment = new NewCommentDto();
      newComment.setAuthor("rami");
      newComment.setContent("init content");
      newComment.setPostId(1L);
      String inputJson = mapToJson(newComment);

      when(commentService.addComment(newComment)).thenReturn(1L);

      mvc.perform(post("/post/1/comment").content(inputJson).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
   }

   @Test
   public void deleteComment() throws Exception {
      String uri = "/post/1/comments";
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
      int status = mvcResult.getResponse().getStatus();
      org.junit.jupiter.api.Assertions.assertEquals(200, status);
   }
   
   private String mapToJson(Object obj) throws com.fasterxml.jackson.core.JsonProcessingException {
      com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
      return objectMapper.writeValueAsString(obj);
   }
}