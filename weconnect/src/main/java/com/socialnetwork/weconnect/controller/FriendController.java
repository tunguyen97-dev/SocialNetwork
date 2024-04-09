package com.socialnetwork.weconnect.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.weconnect.Service.FriendService;
import com.socialnetwork.weconnect.dto.response.ApiResponse;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {

	FriendService friendService;

	@PostMapping("/friend/add-friend/{receiverId}")
	public ApiResponse<String> addFriend(@PathVariable @NotNull Integer receiverId) {
		return ApiResponse.<String>builder().result(friendService.addFriendRequest(receiverId)).build();
	}

	@DeleteMapping("/friend/cancel-friend/{receiverId}")
	public ApiResponse<String> cancelFriend(@PathVariable @NotNull Integer receiverId) {
		return ApiResponse.<String>builder().result(friendService.cancelFriendRequest(receiverId)).build();
	}

	@PostMapping("/friend/accept-friend/{senderId}")
	public ApiResponse<String> acceptFriend(@PathVariable @NotNull Integer senderId) {
		return ApiResponse.<String>builder().result(friendService.acceptFriendRequest(senderId)).build();
	}
}
