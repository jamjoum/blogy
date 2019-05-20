package com.blogy.restful.dto;

import java.util.Date;

public class CommentDto {

    private Long id;

    private Long postId;

    private String content;

    private String author;

    private Date creationDate;
    
    private Date updateDate;


    public CommentDto()
    {
        
    }

    public CommentDto(Long id,Long postId, String content, String author, Date creationDate, Date updateDate) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }
    public Long getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

}
