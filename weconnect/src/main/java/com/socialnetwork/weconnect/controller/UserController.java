package com.socialnetwork.weconnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.weconnect.Service.UserService;
import com.socialnetwork.weconnect.dto.request.ChangePasswordRequest;
import com.socialnetwork.weconnect.dto.response.ApiResponse;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	@PatchMapping("/change-password")
	public ApiResponse<String> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
		service.changePassword(request, connectedUser);
		return ApiResponse.<String>builder().message("change password success").build();
	}

	@PostMapping("/reset-password")
	public ApiResponse<String> forgotPassword(Principal connectedUser) {
		return ApiResponse.<String>builder().message(service.forgotPassword(connectedUser)).build();
	}
}
