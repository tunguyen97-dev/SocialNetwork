package com.socialnetwork.weconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.socialnetwork.weconnect.entity.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

	Boolean existsBySenderIdAndReceiverIdOrReceiverIdAndSenderId(Integer sender1Id, Integer receiver1Id, Integer receiver2Id, Integer sender2Id);

	Boolean existsBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

	int deleteBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

}
