package com.socialnetwork.weconnect.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.socialnetwork.weconnect.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	User findUserById(Integer userId);
		
	boolean existsById(Integer userId);
	
}