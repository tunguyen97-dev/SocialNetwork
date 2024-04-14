package com.socialnetwork.weconnect.controller;

import com.socialnetwork.weconnect.Service.FilesStorageService;
import com.socialnetwork.weconnect.Service.PostService;
import com.socialnetwork.weconnect.dto.request.UpdatePostRequest;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.entity.Post;
import com.socialnetwork.weconnect.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

	private final FilesStorageService storageService;
	private final PostService postService;

	@PostMapping(value = "/add-post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<List<String>> uploadPost(@RequestPart("content") String content,
			@RequestPart("files") MultipartFile[] imageFiles, Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		List<String> urlList = new ArrayList<>();
		Arrays.asList(imageFiles).stream().forEach(file -> {
			urlList.add(storageService.saveTosServer(file, user.getName()));
		});
		postService.savePostToDB(content, urlList);
		return ApiResponse.<List<String>>builder().result(urlList).build();
	}

	// @Hidden
	@GetMapping("/images/{imageName:.+}")
	public ApiResponse<String> getImage(@PathVariable String imageName) {
		return ApiResponse.<String>builder().result(storageService.load(imageName)).build();
	}

	@GetMapping()
	public ApiResponse<List<Post>> getAllPostsByUserId() {
		return ApiResponse.<List<Post>>builder().result(postService.getAllPostsByUserId()).build();
	}

	@GetMapping("/post/{postId}")
	public ApiResponse<Post> getPostByPostId(@PathVariable Integer postId) {
		return ApiResponse.<Post>builder().result(postService.getPostById(postId)).build();
	}

	@DeleteMapping("/post/delete-post/{postId}")
	public ApiResponse<String> delPostByPostId(@PathVariable @NotNull Integer postId) {
		return ApiResponse.<String>builder().result(postService.delPostById(postId)).build();
	}

	@PutMapping("/post/update-post")
	public ApiResponse<Post> updatePostByPostId(@RequestBody @NotNull UpdatePostRequest updatePostRequest) {
		return ApiResponse.<Post>builder().result(postService.updatePostById(updatePostRequest)).build();
	}

	@GetMapping("/post/timeLines")
	public ApiResponse<List<Post>> getListTimeLine() {
		return ApiResponse.<List<Post>>builder().result(postService.getListTimeLine()).build();
	}
}
