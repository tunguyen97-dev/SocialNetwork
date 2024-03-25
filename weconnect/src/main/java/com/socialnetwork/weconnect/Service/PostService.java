package com.socialnetwork.weconnect.Service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.socialnetwork.weconnect.dto.response.PostDto;
import com.socialnetwork.weconnect.dto.response.PostInfoDto;
import com.socialnetwork.weconnect.entity.Post;
import com.socialnetwork.weconnect.entity.User;

@Service
public interface PostService {
	
	public void savePostToDB(String content, List<String> file, User userId);
	public List<PostInfoDto> getAllPostsByUserId(Integer userId, Principal connectedUser);

}
