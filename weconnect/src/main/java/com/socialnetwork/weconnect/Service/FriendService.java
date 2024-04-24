package com.socialnetwork.weconnect.Service;

import org.springframework.stereotype.Service;

import com.socialnetwork.weconnect.dto.response.CntResponse;

@Service
public interface FriendService {
	
	CntResponse addFriendRequest(Integer ReceiverId);
	
	CntResponse cancelFriendRequest(Integer ReceiverId);
	
	CntResponse acceptFriendRequest(Integer senderId);
}
