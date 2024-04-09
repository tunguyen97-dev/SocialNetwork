package com.socialnetwork.weconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.socialnetwork.weconnect.entity.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer> {
	
	Optional<Otp> findByUserId(Integer userId);
}