package com.springboot.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignUpDto {

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    private String email;

}
