package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository annotation is not required as Implementation of JpaRepository have that annotation as
// well as @Transactional annotation
public interface PostRepository extends JpaRepository<Post, Long> {
}
