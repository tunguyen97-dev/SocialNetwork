package com.socialnetwork.weconnect.Service;

import org.springframework.stereotype.Service;

import com.socialnetwork.weconnect.dto.response.CntResponse;

@Service
public interface LikeService {
	CntResponse likePostByPostId(Integer postId);
}
