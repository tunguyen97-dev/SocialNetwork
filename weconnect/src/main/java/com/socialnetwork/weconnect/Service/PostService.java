package com.socialnetwork.weconnect.Service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.socialnetwork.weconnect.dto.request.UpdatePostRequest;
import com.socialnetwork.weconnect.entity.Post;
import com.socialnetwork.weconnect.entity.User;

@Service
public interface PostService {

	public void savePostToDB(String content, List<String> file, User userId);

	public List<Post> getAllPostsByUserId(Integer userId);
	
	public Post getPostById(Integer postId);

	public Boolean delPostById(Integer postId);

	public Post updatePostById(UpdatePostRequest updatePostRequest);

}
