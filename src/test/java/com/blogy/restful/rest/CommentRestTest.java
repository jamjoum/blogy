package com.blogy.restful.rest;

import com.blogy.restful.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.blogy.restful.MockedTestContext;
import com.blogy.restful.config.RestContext;
import com.blogy.restful.dto.CommentDto;
import com.blogy.restful.dto.NewCommentDto;
import com.blogy.restful.model.Comment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestContext.class, MockedTestContext.class})
@WebAppConfiguration
public class CommentRestTest {

    private MockMvc mockMvc;

    @Autowired
    private CommentService commentService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        Mockito.reset(commentService);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void shouldReturnFoundComments() throws Exception{

        // given
        List<CommentDto> comments = new ArrayList<CommentDto>();
        Date creationDate = new Date();
        comments.add(new CommentDto(2L,1L, "message test", "rami", creationDate,null));

        // when
        when(commentService.findAllByPostID(1L).stream()
        .map(comment -> new CommentDto(comment.getId(),comment.getPostId(),comment.getContent(),
        comment.getAuthor(),comment.getCreationDate(),null))
        .collect(Collectors.toList())).thenReturn(comments);

        // then
        mockMvc.perform(get("/post/1/comments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].content", is("message test")))
                .andExpect(jsonPath("$[0].author", is("rami")))
                .andExpect(jsonPath("$[0].creationDate", is(creationDate.getTime())));

    }
    @Test
    public void shouldAddComment() throws Exception{

        // given
        NewCommentDto newComment = createComment("new message", "rami");

        // when
        when(commentService.addComment(newComment)).thenReturn(1L);
 
        // then
        Comment comment=new Comment();
        comment.setAuthor(newComment.getAuthor());
        comment.setContent(newComment.getContent());
        comment.setPostId(1L); // my post default id
        comment.setCreationDate(new Date());
        
        mockMvc.perform(post("/post/1/comment")
                .content(TestUtil.convertObjectToJsonBytes(comment))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private NewCommentDto createComment(String content, String author) {
        NewCommentDto newComment = new NewCommentDto();
        newComment.setContent(content);
        newComment.setAuthor(author);
        return newComment;
    }

}
