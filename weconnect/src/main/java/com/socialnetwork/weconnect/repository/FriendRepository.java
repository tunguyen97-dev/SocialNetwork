package com.socialnetwork.weconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socialnetwork.weconnect.entity.Friend;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
	
	//Boolean existsByUser1IdAndUser2IdOrUser2IdAndUser1Id(Integer userId, Integer user2Id, Integer user3Id, Integer user4Id);

}
