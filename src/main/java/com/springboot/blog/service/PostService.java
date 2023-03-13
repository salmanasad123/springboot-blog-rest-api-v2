package com.springboot.blog.service;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostPageableResponse;

import java.util.List;

public interface PostService {

    public PostDto createPost(PostDto postDto);

    public PostPageableResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    public PostDto getPostById(Long postId);

    public PostDto updatePostById(Long postId, PostDto postDto);

    public void deletePostById(Long postId);
}
