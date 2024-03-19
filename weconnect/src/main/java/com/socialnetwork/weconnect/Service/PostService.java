package com.socialnetwork.weconnect.Service;

import java.util.List;

import com.socialnetwork.weconnect.entity.User;

public interface PostService {
	
	public void savePostToDB(String content, List<String> file, User userId);

}
