package com.socialnetwork.weconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.socialnetwork.weconnect.entity.Comment;

@Repository
public interface CommentRepositoty extends JpaRepository<Comment, Integer> {

	List<Comment> findByPostId(Integer postId);
	
	Optional<Comment> findById(Integer commentId);

}
