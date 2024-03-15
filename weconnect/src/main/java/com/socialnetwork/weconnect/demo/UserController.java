package com.socialnetwork.weconnect.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.weconnect.user.ChangePasswordRequest;
import com.socialnetwork.weconnect.user.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	@PatchMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
		service.changePassword(request, connectedUser);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> forgotPassword(Principal connectedUser) {
		String changePasswordUrl = service.forgotPassword(connectedUser);
		return ResponseEntity.ok(changePasswordUrl);
	}
}
