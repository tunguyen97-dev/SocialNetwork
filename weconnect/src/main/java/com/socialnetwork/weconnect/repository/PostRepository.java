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
			       "u.firstname as firstName, " +
                   "p.content as content, " +
                   "p.created_at as createdAt, " +
                   "p.update__at as updateAt, " +
                   "IFNULL(" +
                   "CONCAT('[', GROUP_CONCAT(JSON_QUOTE(pi.images) SEPARATOR ', '), ']')" +
                   ", '[]') AS postImages, " +
                   "CASE WHEN COUNT(c.id) > 0 THEN CONCAT('[', GROUP_CONCAT(DISTINCT " +
                   "JSON_OBJECT(" +
                   "'comment_id', c.id, " +
                   "'comment_content', c.content, " +
                   "'comment_user_id', c.user_id, " +
                   "'comment_created_at', c.created_at, " +
                   "'comment_is_deleted', c.is_deleted" +
                   ")" +
                   "SEPARATOR ', '" +
                   " ), ']')" +
                   "ELSE '[]'" +
                   "END " +
                   "AS postComments " +
                   "from post p " +
			       "inner join _user u " +
                   "on p.user_id = u.id " +
			       "left join post_images pi " +
			       "on pi.post_id = p.id " +
			       "left join comment c " +
			       "on c.post_id = p.id " +
                   "where p.user_id = :userId " +
			       "group by p.id",
		nativeQuery = true)
	List<PostInfoDto> getAllPostsByUserId(Integer userId);

	@Query( value = "select * from post", nativeQuery = true)
	Post getPostsByUserId(@Param("userId") Integer userId);
}