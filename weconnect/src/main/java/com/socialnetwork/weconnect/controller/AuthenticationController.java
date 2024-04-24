package com.socialnetwork.weconnect.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.socialnetwork.weconnect.Service.AuthenticationService;
import com.socialnetwork.weconnect.dto.request.AuthenticationRequest;
import com.socialnetwork.weconnect.dto.request.ChangePasswordRequest;
import com.socialnetwork.weconnect.dto.request.EmailRequest;
import com.socialnetwork.weconnect.dto.request.RegisterRequest;
import com.socialnetwork.weconnect.dto.request.VerifyRequest;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.AuthenticationResponse;
import com.socialnetwork.weconnect.dto.response.CntResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

	AuthenticationService authenticationService;

	@PostMapping("/register")
	public ApiResponse<CntResponse> register(@RequestBody @Valid RegisterRequest request) {
		return ApiResponse.<CntResponse>builder()
				.result(authenticationService.register(request))
				.build();
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(authenticationService.authenticate(request));

	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		authenticationService.refreshToken(request, response);
	}

	@PostMapping("/login")
	public ApiResponse<CntResponse> login(@RequestBody AuthenticationRequest request) {
		return ApiResponse.<CntResponse>builder()
				.result(authenticationService.login(request))
				.build();
	}
	
	@PostMapping("/verifyOtp")
	public ApiResponse<AuthenticationResponse> verifyOtp(@RequestBody @Valid VerifyRequest verifyRequest) {
		return ApiResponse.<AuthenticationResponse>builder()
				.result(authenticationService.verifyOtp(verifyRequest))
				.build();
	}
	@PostMapping("/forgotPassword")
	public ApiResponse<AuthenticationResponse> forgotPassword(@RequestBody @Valid EmailRequest emailRequest) {
		return ApiResponse.<AuthenticationResponse>builder()
				.result(authenticationService.forgotPassword(emailRequest))
				.build();
	}
	
	@PutMapping("/user/change-password")
	public ApiResponse<CntResponse> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
		return ApiResponse.<CntResponse>builder()
				.result(authenticationService.changePassword(changePasswordRequest))
				.build();
	}
}
