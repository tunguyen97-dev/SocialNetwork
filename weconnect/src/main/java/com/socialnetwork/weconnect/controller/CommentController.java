package com.socialnetwork.weconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.socialnetwork.weconnect.Service.CommentService;
import com.socialnetwork.weconnect.dto.request.CommentRequest;
import com.socialnetwork.weconnect.dto.request.UpdateCommentRequest;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.entity.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

	private final CommentService commentService;

	@DeleteMapping("/comment/delete-comment/{commentId}")
	public ApiResponse<String> delCommentByCommentId(@PathVariable @NotNull Integer commentId) {
		return ApiResponse.<String>builder()
				.result(commentService.delCommentById(commentId))
				.build();
	}

	@GetMapping("/comment/{commentId}")
	public ApiResponse<Comment> getCommentByCommentId(@PathVariable @NotNull Integer commentId) {
		return ApiResponse.<Comment>builder()
				.result(commentService.getCommentById(commentId))
				.build();
	}

	@PostMapping("comment/add-comment")
	public ApiResponse<Comment> addComment(@RequestBody @NotNull CommentRequest commentRequest) {
		return ApiResponse.<Comment>builder()
				.result(commentService.addComment(commentRequest))
				.build();
	}

	@PutMapping("comment/update-comment")
	public ApiResponse<Comment> updateCommentByCommentId(@RequestBody @NotNull UpdateCommentRequest updateCommentRequest) {
		return ApiResponse.<Comment>builder()
				.result(commentService.updateCommentById(updateCommentRequest))
				.build();
	}

	@PutMapping("comment/{commentId}/lock") 
	public ApiResponse<String> lockCommentByCommentId(@PathVariable @NotNull Integer commentId) {
		return ApiResponse.<String>builder()
				.result(commentService.lockCommentById(commentId))
				.build();
	}

	@GetMapping("/{postId}")
	public ApiResponse<List<Comment>> getAllCommentsByPostId(@PathVariable Integer postId) {
		return ApiResponse.<List<Comment>>builder()
				.result(commentService.getAllCommentsByPostId(postId))
				.build();
	}

}
