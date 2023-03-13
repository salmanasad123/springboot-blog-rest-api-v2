package com.springboot.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDto {

    @NotEmpty(message = "username or email cannot be empty")
    private String usernameOrEmail;

    @NotEmpty(message = "password cannot be empty")
    private String password;

}
