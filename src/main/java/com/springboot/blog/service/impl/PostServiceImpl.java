package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostPageableResponse;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// this annotation makes this class available for auto-detection while component scanning
@Service
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post newPost = postRepository.save(post);
        // convert entity back to Dto
        PostDto returnValue = mapToDto(newPost);

        return returnValue;
    }

    @Override
    public PostPageableResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Pageable pageable = PageRequest.of(pageNo, pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<Post> postPage = postRepository.findAll(pageable);

        // get content from page object
        List<Post> postList = postPage.getContent();

        List<PostDto> postDtoList = postList.stream()
                .map((Post post) -> {
                    return mapToDto(post);
                })
                .collect(Collectors.toList());

        // create pageable response
        PostPageableResponse postPageableResponse = new PostPageableResponse();
        postPageableResponse.setPostDtoList(postDtoList);
        postPageableResponse.setPageNo(postPage.getNumber());
        postPageableResponse.setPageSize(postPage.getSize());
        postPageableResponse.setTotalPages(postPage.getTotalPages());
        postPageableResponse.setTotalElements(postPage.getTotalElements());
        postPageableResponse.setLast(postPage.isLast());

        return postPageableResponse;
    }

    @Override
    public PostDto getPostById(Long postId) {

        PostDto postDto = null;
        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {
            postDto = mapToDto(post.get());
        } else {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        return postDto;
    }

    @Override
    public PostDto updatePostById(Long postId, PostDto postDto) {

        Post updatedPost = null;
        PostDto returnValue = null;

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            // update post
            Post post = postOptional.get();
            post.setTitle(postDto.getTitle());
            post.setDescription(postDto.getDescription());
            post.setContent(postDto.getContent());

            updatedPost = postRepository.save(post);

            returnValue = mapToDto(updatedPost);

        } else {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        return returnValue;
    }

    @Override
    public void deletePostById(Long postId) {

        // find post to delete
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Post", "id", postId);
        });

        postRepository.delete(post);
    }

    private PostDto mapToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());

        if (post.getCommentSet().size() > 0) {
            Set<CommentDto> commentDtoSet = post.getCommentSet().stream().map((Comment comment) -> {
                return mapToDto(comment);
            }).collect(Collectors.toSet());

            postDto.setComments(commentDtoSet);
        }

        return postDto;
    }

    private CommentDto mapToDto(Comment comment) {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());

        return commentDto;
    }
}
