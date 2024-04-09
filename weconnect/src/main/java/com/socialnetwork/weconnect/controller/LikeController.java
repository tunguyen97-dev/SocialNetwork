package com.socialnetwork.weconnect.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.socialnetwork.weconnect.Service.LikeService;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

	LikeService likeService;
	
	@PostMapping("/{postId}/like")
	public ApiResponse<String> likePostByPostId(@PathVariable @NotNull Integer postId) {
		return ApiResponse.<String>builder().result(likeService.likePostByPostId(postId)).build();
	}
	
}
