package com.socialnetwork.weconnect.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.socialnetwork.weconnect.dto.response.PostInfoDto;
import com.socialnetwork.weconnect.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query(value = "select " +
                   "p.id as id, " +
			       "p.content as content, " +
                   "u.firstname as firstName, " +
                   "(SELECT GROUP_CONCAT(pi.images SEPARATOR ', ') " +
                   "FROM post_images pi WHERE pi.post_id = p.id) AS postComments " +
                   "from post p " +
			       "inner join _user u " +
                   "on p.user_id = u.id ",
		nativeQuery = true)
	List<PostInfoDto> getAllPosts();

	@Query( value = "select * from post", nativeQuery = true)
	Post getPostByUserIdAndPostId(Integer postId, Integer userId);
}