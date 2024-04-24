package com.socialnetwork.weconnect.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socialnetwork.weconnect.Service.CommentService;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.CntResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	private final CommentService commentService;

	@GetMapping
	@PreAuthorize("hasAuthority('admin:read')")
	public String get() {
		return "GET:: admin controller";
	}

	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
	@Hidden
	public String post() {
		return "POST:: admin controller";
	}

	@PutMapping
	@PreAuthorize("hasAuthority('admin:update')")
	// @Hidden
	public String put() {
		return "PUT:: admin controller";
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('admin:delete')")
	// @Hidden
	public String delete() {
		return "DELETE:: admin controller";
	}
	
	@PutMapping("comment/{commentId}/lock")
	public ApiResponse<CntResponse> lockCommentByCommentId(@PathVariable @NotNull Integer commentId) {
		return ApiResponse.<CntResponse>builder()
				.result(commentService.lockCommentById(commentId, true))
				.build();
	}
	
	@PutMapping("comment/{commentId}/unlock")
	public ApiResponse<CntResponse> unLockCommentByCommentId(@PathVariable @NotNull Integer commentId) {
		return ApiResponse.<CntResponse>builder()
				.result(commentService.lockCommentById(commentId, false))
				.build();
	}
	
}
