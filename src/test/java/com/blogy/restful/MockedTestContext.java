package com.blogy.restful;

import com.blogy.restful.service.CommentService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

public class MockedTestContext {

   
    @Bean
    public CommentService commentService() {
        return Mockito.mock(CommentService.class);
    }
}
