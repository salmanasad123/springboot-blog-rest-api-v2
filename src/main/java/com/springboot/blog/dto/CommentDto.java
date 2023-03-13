package com.springboot.blog.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class CommentDto {

    private Long id;

    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email is not in valid format")
    private String email;

    @NotEmpty(message = "Body must not be empty")
    @Min(value = 10, message = "Body must be of at least 10 characters")
    private String body;
}
