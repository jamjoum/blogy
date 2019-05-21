package com.blogy.restful.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class CommentRestTest extends AbstractTest {

   @MockBean
   protected CommentService commentService;
   
   @Override
   @Before
   public void setUp() {
      super.setUp();
      Mockito.reset(commentService);
      }

   @Test
   public void getCommentsList() throws Exception {
      // given
      List<Comment> comments = new ArrayList<>();
      Comment comment = new Comment();
      comment.setId(1L);
      comment.setAuthor("rami");
      comment.setContent("test");
      comment.setPostId(1L); // my post default id
      comments.add(comment);

      // when
      when(commentService.findAllByPostID(1L)).thenReturn(comments);

      // then
      mvc.perform(get("/post/1/comments")
         .accept(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$", hasSize(1)))
         .andExpect(jsonPath("$[0].id", is(1)))
         .andExpect(jsonPath("$[0].content", is("test")))
         .andExpect(jsonPath("$[0].author", is("rami")));

   }

   @Test
   public void createComment() throws Exception {

      // given
      NewCommentDto newComment = new NewCommentDto();
      newComment.setAuthor("rami");
      newComment.setContent("init content");
      newComment.setPostId(1L); // my post default id
      String inputJson = super.mapToJson(newComment);
		// when
		when(commentService.addComment(newComment)).thenReturn(1L);

      // then
      mvc.perform(post("/post/1/comment")
				.content(inputJson)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
   }

   
   @Test
   public void deleteComment() throws Exception {
     
      String uri = "/post/1/comments";
      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
      int status = mvcResult.getResponse().getStatus();
      assertEquals(200, status);

   }
}