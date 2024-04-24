package com.socialnetwork.weconnect.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.dto.request.UpdatePostRequest;
import com.socialnetwork.weconnect.dto.response.CntResponse;
import com.socialnetwork.weconnect.entity.Post;

@Service
public interface PostService {

	public void savePostToDB(String content, List<String> file);

	public List<Post> getAllPostsByUserId();
	
	public Post getPostById(Integer postId);

	public CntResponse delPostById(Integer postId);

	public Post updatePostById(UpdatePostRequest updatePostRequest);
	
	List<Post> getListTimeLine();

}
