package com.socialnetwork.weconnect.Service;

import org.springframework.stereotype.Service;

@Service
public interface LikeService {
	String likePostByPostId(Integer postId);
}
