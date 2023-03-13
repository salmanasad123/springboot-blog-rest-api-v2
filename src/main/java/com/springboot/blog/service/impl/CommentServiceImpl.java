package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exceptions.BlogApiException;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;

    PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        // find the post first
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Post", "id", postId);
        });

        Comment comment = mapToEntity(commentDto);
        // set the found post to comment
        comment.setPost(post);

        // save comment
        Comment newComment = commentRepository.save(comment);
        CommentDto newCommentDto = mapToDto(newComment);
        return newCommentDto;
    }

    @Override
    public List<CommentDto> getAllComments(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Post", "id", postId);
        });

        List<Comment> commentList = commentRepository.findByPostId(postId);

        List<CommentDto> commentDtoList = commentList.stream().map((Comment comment) -> {
            return mapToDto(comment);
        }).collect(Collectors.toList());

        return commentDtoList;
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        CommentDto commentDto = null;

        // fetch post
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Post", "id", postId);
        });

        // find the comment
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            // check if comment belongs to a post
            if (!commentOptional.get().getPost().getId().equals(post.getId())) {
                throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to a post");
            }
            commentDto = mapToDto(commentOptional.get());
        } else {
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }

        return commentDto;
    }

    @Override
    public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentDto) {

        CommentDto returnValue = null;

        // fetch post
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Post", "id", postId);
        });

        // find the comment
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            // check if comment belongs to a post
            if (!commentOptional.get().getPost().getId().equals(post.getId())) {
                throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to a post");
            }
            // update the comment
            Comment comment = commentOptional.get();
            comment.setName(commentDto.getName());
            comment.setEmail(commentDto.getEmail());
            comment.setBody(commentDto.getBody());

            Comment updatedComment = commentRepository.save(comment);
            returnValue = mapToDto(updatedComment);
        } else {
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }

        return returnValue;
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {

        // fetch post
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Post", "id", postId);
        });

        // find the comment
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            // check if comment belongs to a post
            if (!commentOptional.get().getPost().getId().equals(post.getId())) {
                throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to a post");
            }

            commentRepository.delete(commentOptional.get());

        } else {
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }
    }

    private CommentDto mapToDto(Comment comment) {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {

        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        return comment;
    }
}
