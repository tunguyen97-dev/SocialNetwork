package com.socialnetwork.weconnect.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.socialnetwork.weconnect.Service.FriendService;
import com.socialnetwork.weconnect.entity.Friend;
import com.socialnetwork.weconnect.entity.FriendRequest;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.exception.AppException;
import com.socialnetwork.weconnect.exception.ErrorCode;
import com.socialnetwork.weconnect.repository.FriendRepository;
import com.socialnetwork.weconnect.repository.FriendRequestRepository;
import com.socialnetwork.weconnect.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendServiceImpl implements FriendService {

	FriendRequestRepository friendRequestRepository;
	FriendRepository friendRepository;
	UserRepository userRepository;
	
	@Transactional
	@Override
	public String addFriendRequest(Integer receiverId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (receiverId.equals(user.getId())) {
			throw new AppException(ErrorCode.DUPLICATE_ID);
		} else if (friendRequestRepository.existsBySenderIdAndReceiverIdOrReceiverIdAndSenderId(user.getId(), receiverId, user.getId(), receiverId)) {
			throw new AppException(ErrorCode.SEND_FRIEND_FAILED);
		}
		
        FriendRequest friendRequest = FriendRequest.builder()
        		.receiver(userRepository.findUserById(receiverId))
        		.sender(user)
        		.createdAt(null)
        		.build();
        friendRequest = friendRequestRepository.save(friendRequest);
		return "Friend request sent successfully";
	}
	
	@Transactional
	@Override
	public String cancelFriendRequest(Integer receiverId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (receiverId.equals(user.getId())) {
			throw new AppException(ErrorCode.DUPLICATE_ID);
		} else if (!friendRequestRepository.existsBySenderIdAndReceiverId(user.getId(), receiverId)) {
			throw new AppException(ErrorCode.CANCEL_FRIEND_FAILED);
		}
		
        int resultCancel = friendRequestRepository.deleteBySenderIdAndReceiverId(user.getId(), receiverId);
        if (resultCancel != 1) {
        	throw new AppException(ErrorCode.CANCEL_FRIEND_FAILED);
		}
		return "Friend request cancelled successfully";
	}
	
	@Transactional
	@Override
	public String acceptFriendRequest(Integer senderId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (senderId.equals(user.getId())) {
			throw new AppException(ErrorCode.DUPLICATE_ID);
		} else if (!friendRequestRepository.existsBySenderIdAndReceiverId(senderId, user.getId())) {
			throw new AppException(ErrorCode.ACCEPT_FRIEND_FAILED);
		} 

		Friend friend = Friend.builder()
				.user1(user)
				.user2(userRepository.findUserById(senderId))
				.createdAt(sdf.format(new Date()))
				.build();
		
		friend = friendRepository.save(friend);
		if (friend == null) {
			throw new AppException(ErrorCode.ACCEPT_FRIEND_FAILED);
		}
        int resultCancel = friendRequestRepository.deleteBySenderIdAndReceiverId(senderId, user.getId());
        if (resultCancel != 1) {
        	throw new AppException(ErrorCode.ACCEPT_FRIEND_FAILED);
		}
		return "Friend request accepted successfully";
	}
	
}
