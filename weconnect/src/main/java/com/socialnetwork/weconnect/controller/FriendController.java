package com.socialnetwork.weconnect.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.weconnect.Service.FriendService;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.CntResponse;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {

	private final FriendService friendService;

	@PostMapping("/friend/add-friend/{receiverId}")
	public ApiResponse<CntResponse> addFriend(@PathVariable @NotNull Integer receiverId) {
		return ApiResponse.<CntResponse>builder()
				.result(friendService.addFriendRequest(receiverId))
				.build();
	}

	@DeleteMapping("/friend/cancel-friend/{receiverId}")
	public ApiResponse<CntResponse> cancelFriend(@PathVariable @NotNull Integer receiverId) {
		return ApiResponse.<CntResponse>builder()
				.result(friendService.cancelFriendRequest(receiverId))
				.build();
	}

	@PostMapping("/friend/accept-friend/{senderId}")
	public ApiResponse<CntResponse> acceptFriend(@PathVariable @NotNull Integer senderId) {
		return ApiResponse.<CntResponse>builder()
				.result(friendService.acceptFriendRequest(senderId))
				.build();
	}
}
