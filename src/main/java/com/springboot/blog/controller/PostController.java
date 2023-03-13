package com.springboot.blog.controller;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostPageableResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // only admin user can access this api
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {

        PostDto newPost = postService.createPost(postDto);
        ResponseEntity<PostDto> responseEntity = new ResponseEntity<>(newPost, HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity<PostPageableResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_ORDER, required = false) String sortDir) {

        PostPageableResponse postPageableResponse = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        ResponseEntity<PostPageableResponse> responseEntity = new ResponseEntity<>(postPageableResponse, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Long postId) {

        PostDto postDto = postService.getPostById(postId);
        ResponseEntity<PostDto> responseEntity = new ResponseEntity<>(postDto, HttpStatus.OK);
        return responseEntity;
    }

    // only admin user can access this api
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePostById(@PathVariable(name = "postId") Long postId,
                                                  @Valid @RequestBody PostDto postDto) {

        PostDto updatedPost = postService.updatePostById(postId, postDto);
        ResponseEntity<PostDto> responseEntity = new ResponseEntity<>(updatedPost, HttpStatus.OK);
        return responseEntity;
    }

    // only admin user can access this api
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable(name = "postId") Long postId) {

        postService.deletePostById(postId);

        return new ResponseEntity<>("Post deleted Successfully", HttpStatus.OK);
    }
}
