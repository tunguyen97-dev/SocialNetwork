package com.socialnetwork.weconnect.controller;

import com.socialnetwork.weconnect.Service.FilesStorageService;
import com.socialnetwork.weconnect.Service.PostService;
import com.socialnetwork.weconnect.dto.request.FileInfo;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.PostInfoDto;
import com.socialnetwork.weconnect.entity.Post;
import com.socialnetwork.weconnect.entity.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class PostController {
	private final FilesStorageService storageService;
	private final PostService postService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<List<String>> uploadPost(@RequestPart("content") String content,
			@RequestPart("files") MultipartFile[] files, Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		List<String> urlList = new ArrayList<>();
		Arrays.asList(files).stream().forEach(file -> {
			urlList.add(storageService.saveTosServer(file, user.getFirstname()));
		});
		postService.savePostToDB(content, urlList, user);
		return ApiResponse.<List<String>>builder().message("Uploaded the files successfully: ").result(urlList).build();
	}

	@GetMapping("/files")
	public ApiResponse<List<FileInfo>> getListImageByUser(Principal connectedUser) {
		List<FileInfo> fileInfos = storageService.loadAllByUserName(connectedUser).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(PostController.class, "getImage", filename, connectedUser).build().toString();
			return new FileInfo(filename, url);
		}).collect(Collectors.toList());
		return ApiResponse.<List<FileInfo>>builder().result(fileInfos).build();
	}
	
	@GetMapping("/posts/{userId:.+}")
	public ApiResponse<List<PostInfoDto>> getPostsByUserId(@PathVariable Integer userId, Principal connectedUser) {
		return ApiResponse.<List<PostInfoDto>>builder().result(postService.getAllPostsByUserId(userId, connectedUser)).build();
	}

	@GetMapping("/files/{filename:.+}")
	public ApiResponse<String> getImage(@PathVariable String filename, Principal connectedUser) {
		return ApiResponse.<String>builder().result(storageService.load(filename, connectedUser)).build();
	}
}
