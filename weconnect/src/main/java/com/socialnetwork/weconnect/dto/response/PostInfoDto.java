package com.socialnetwork.weconnect.dto.response;

import org.springframework.beans.factory.annotation.Value;

public interface PostInfoDto {
	long getId();
	
    String getContent();
    
    @Value("#{target.firstname}")
    String getfirstName();
    
    @Value("#{target.postComments}")
    String getPostComments();
}
