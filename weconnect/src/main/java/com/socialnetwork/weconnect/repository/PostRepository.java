package com.socialnetwork.weconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.socialnetwork.weconnect.dto.response.PostDto;
import com.socialnetwork.weconnect.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
	
	@Query( value = "select * from post", nativeQuery = true)
	Post getPostByUserIdAndPostId(Integer postId, Integer userId);
}