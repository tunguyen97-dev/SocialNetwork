package com.socialnetwork.weconnect.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import com.socialnetwork.weconnect.Service.AuthenticationService;
import com.socialnetwork.weconnect.Service.FilesStorageService;
import com.socialnetwork.weconnect.Service.UserService;
import com.socialnetwork.weconnect.dto.request.FileInfo;
import com.socialnetwork.weconnect.dto.request.UserInforRequest;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.UserInforRestponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
	
	FilesStorageService storageService;
	AuthenticationService authenticationService;
	UserService userService;
	

	@GetMapping("/user/images")
	public ApiResponse<List<FileInfo>> getListImageByUser() {
		List<FileInfo> fileInfos = storageService.loadAllByUserName().map(path -> {
			String imageName = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(PostController.class, "getImage", imageName).build().toString();
			return new FileInfo(imageName, url);
		}).collect(Collectors.toList());
		return ApiResponse.<List<FileInfo>>builder()
				.result(fileInfos)
				.build();
	}
	
	@DeleteMapping("/user/delete-user")
	public ApiResponse<String> deleteUser() {
		return ApiResponse.<String>builder()
				.result(authenticationService.deleteUser())
				.build();
	}
	
	@GetMapping("/user/getInformation")
	public ApiResponse<UserInforRestponse> getInformationUser() {
		return ApiResponse.<UserInforRestponse>builder()
				.result(userService.getInformationUser())
				.build();
	}
	
	@PutMapping("/user/getInformation")
	public ApiResponse<UserInforRestponse> editInformationUser(@RequestBody UserInforRequest userInforRequest) {
		return ApiResponse.<UserInforRestponse>builder()
				.result(userService.editInformationUser(userInforRequest))
				.build();
	}
	
}
