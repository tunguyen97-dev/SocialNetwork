package com.socialnetwork.weconnect.dto.response;

import org.springframework.beans.factory.annotation.Value;

public interface PostInfoDto {
	long getPostId();
	
    String getContent();
    
    //@Value("#{target.createdAt}")
    String getCreatedAt();
    
    String getUpdateAt();
    
    @Value("#{target.postComments}")
    String getPostComments();
    
    @Value("#{target.postImages}")
    String getPostImages();
    
    @Value("#{target.postLikes}")
    String getPostLikes();
   
}
