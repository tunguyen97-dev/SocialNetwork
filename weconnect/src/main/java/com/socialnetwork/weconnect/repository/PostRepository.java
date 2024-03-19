package com.socialnetwork.weconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socialnetwork.weconnect.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

}
