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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CommentRestIntegrationTest {

    private MockMvc mvc;

    @Mock
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(new CommentRest(commentService)).build();
    }

    @Test
    void getCommentsList_shouldReturnComments() throws Exception {
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
    void createComment_shouldReturnCreated() throws Exception {
        NewCommentDto newComment = new NewCommentDto();
        newComment.setAuthor("rami");
        newComment.setContent("init content");
        newComment.setPostId(1L);

        when(commentService.addComment(org.mockito.Mockito.any(NewCommentDto.class))).thenReturn(1L);

        mvc.perform(post("/post/1/comment")
                .content(mapToJson(newComment))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteComment_shouldReturnOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/post/1/comments")).andExpect(status().isOk());
    }

    private String mapToJson(Object obj) throws com.fasterxml.jackson.core.JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
