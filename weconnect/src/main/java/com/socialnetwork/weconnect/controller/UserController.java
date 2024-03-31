package com.socialnetwork.weconnect.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.socialnetwork.weconnect.Service.FilesStorageService;
import com.socialnetwork.weconnect.Service.UserService;
import com.socialnetwork.weconnect.dto.request.ChangePasswordRequest;
import com.socialnetwork.weconnect.dto.request.FileInfo;
import com.socialnetwork.weconnect.dto.response.ApiResponse;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	private final FilesStorageService storageService;
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
	
	@GetMapping("/images")
	public ApiResponse<List<FileInfo>> getListImageByUser() {
		List<FileInfo> fileInfos = storageService.loadAllByUserName().map(path -> {
			String imageName = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(PostController.class, "getImage", imageName).build().toString();
			return new FileInfo(imageName, url);
		}).collect(Collectors.toList());
		return ApiResponse.<List<FileInfo>>builder().result(fileInfos).build();
	}
}
