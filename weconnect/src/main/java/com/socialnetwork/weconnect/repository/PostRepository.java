package com.socialnetwork.weconnect.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.socialnetwork.weconnect.dto.response.PostInfoDto;
import com.socialnetwork.weconnect.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query(value = "select " +
                   "p.id as postId, " +
                   "p.content as content, " +
                   "p.created_at as createdAt, " +
                   "p.update__at as updateAt, " +
                   "(SELECT IFNULL(" +
                   "  JSON_OBJECTAGG(cmt.id, JSON_OBJECT(" +
                   "    'postId', cmt.post_id, " +
                   "    'content', cmt.content, " +
                   "    'createdAt', cmt.created_at, " +
                   "    'updatedAt', cmt.update__at" +
                   "  )), JSON_OBJECT()) " +
                   "FROM comment cmt WHERE cmt.post_id = p.id) AS postComments, " +
//                   "IFNULL((SELECT JSON_OBJECTAGG(pl.id,pl.post_id, pl.user_id) " +
//                   "FROM post_like pl WHERE pl.post_id = p.id), JSON_OBJECT()) AS postLikes " +
                   "(SELECT IFNULL(JSON_OBJECTAGG(pi.post_id, pi.images), JSON_OBJECT()) " +
                   "FROM post_images pi WHERE pi.post_id = p.id) AS postImages " +
                   "from post p " +
			       "inner join _user u " +
                   "on p.user_id = u.id " +
                   "where p.user_id = :userId ",
		nativeQuery = true)
	List<PostInfoDto> getAllPostsByUserId(Integer userId);

	@Query( value = "select * from post", nativeQuery = true)
	Post getPostsByUserId(@Param("userId") Integer userId);
}