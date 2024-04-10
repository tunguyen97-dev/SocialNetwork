package com.socialnetwork.weconnect.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.socialnetwork.weconnect.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query(value = "select * from post p where p.user_id = :userId", nativeQuery = true)
	List<Post> findAllPostsByUserId(@Param("userId") Integer userId);

	Post findPostById(Integer id);

	boolean existsByIdAndUserId(Integer postId, Integer id);

	Post findPostImagesById(Integer postId);

	@Query(value = "select p.user_id from post p where p.id = :postId", nativeQuery = true)
	Integer findUserIdById(Integer postId);

	@Query(value = "SELECT p.* FROM post p JOIN user u ON p.user_id = u.id WHERE u.id = :userId OR u.id IN (SELECT CASE WHEN user1_id = :userId THEN user2_id ELSE user1_id END AS friendId FROM friend WHERE user1_id = :userId OR user2_id = :userId) ORDER BY p.created_at DESC; ", nativeQuery = true)
	List<Post> getTimeLineByUserId(@Param("userId") Integer userId);

}