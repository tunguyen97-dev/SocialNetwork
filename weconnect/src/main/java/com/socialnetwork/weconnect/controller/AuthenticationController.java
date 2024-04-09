package com.socialnetwork.weconnect.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.socialnetwork.weconnect.Service.AuthenticationService;
import com.socialnetwork.weconnect.dto.request.ChangePasswordRequest;
import com.socialnetwork.weconnect.dto.request.EmailRequest;
import com.socialnetwork.weconnect.dto.request.LoginRequest;
import com.socialnetwork.weconnect.dto.request.RegisterRequest;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.AuthenticationResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

	AuthenticationService authenticationService;

	@PostMapping("/register")
	public ApiResponse<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
		ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();
		apiResponse.setResult(authenticationService.register(request));
		return apiResponse;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authenticationService.authenticate(request));

	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		authenticationService.refreshToken(request, response);
	}

	@PostMapping("/user/fogot-password")
	public ApiResponse<AuthenticationResponse> forgotPassword(@RequestBody @Valid EmailRequest emailRequest) {
		return ApiResponse.<AuthenticationResponse>builder()
				.result(authenticationService.forgotPassword(emailRequest))
				.message("Reset-password success")
				.build();
	}
	
	@PatchMapping("/user/change-password")
	public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
		return ApiResponse.<String>builder()
				.result(authenticationService.changePassword(request))
				.build();
	}
	
}
