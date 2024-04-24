package com.socialnetwork.weconnect.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.socialnetwork.weconnect.Service.LikeService;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.CntResponse;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

	private final LikeService likeService;
	
	@PostMapping("/{postId}/like")
	public ApiResponse<CntResponse> likePostByPostId(@PathVariable @NotNull Integer postId) {
		return ApiResponse.<CntResponse>builder().result(likeService.likePostByPostId(postId)).build();
	}
	
}
