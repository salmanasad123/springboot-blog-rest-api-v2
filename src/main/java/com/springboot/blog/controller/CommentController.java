package com.springboot.blog.controller;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable(name = "postId") Long postId) {

        List<CommentDto> commentDtoList = commentService.getAllComments(postId);
        ResponseEntity<List<CommentDto>> responseEntity = new ResponseEntity<>(commentDtoList, HttpStatus.OK);

        return responseEntity;
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createCommentForPost(@PathVariable(name = "postId") Long postId,
                                                           @Valid @RequestBody CommentDto commentDto) {

        CommentDto newCommentDto = commentService.createComment(postId, commentDto);
        ResponseEntity<CommentDto> responseEntity = new ResponseEntity<>(newCommentDto, HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentForPostByCommentId(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {


        CommentDto commentDto = commentService.getCommentById(postId, commentId);

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentByIdForAPost(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId,
            @Valid @RequestBody CommentDto commentDto) {

        CommentDto updatedComment = commentService.updateCommentById(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable(name = "postId") Long postId,
                                                    @PathVariable(name = "commentId") Long commentId) {


        commentService.deleteCommentById(postId, commentId);

        return new ResponseEntity<>("Comment Deleted Successfully", HttpStatus.OK);
    }
}
