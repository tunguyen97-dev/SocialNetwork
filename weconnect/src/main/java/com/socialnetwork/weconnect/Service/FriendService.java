package com.socialnetwork.weconnect.Service;

import org.springframework.stereotype.Service;

@Service
public interface FriendService {
	
	String addFriendRequest(Integer ReceiverId);
	
	String cancelFriendRequest(Integer ReceiverId);
	
	String acceptFriendRequest(Integer senderId);
}
