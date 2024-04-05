package com.socialnetwork.weconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.socialnetwork.weconnect.entity.PostLike;

@Repository
public interface LikeRepository extends JpaRepository<PostLike, Integer> {
	
	PostLike findByPostIdAndUserId(Integer postId, Integer userId);

}
