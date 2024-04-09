package com.socialnetwork.weconnect.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.weconnect.dto.request.ChangePasswordRequest;
import com.socialnetwork.weconnect.dto.request.EmailRequest;
import com.socialnetwork.weconnect.dto.request.LoginRequest;
import com.socialnetwork.weconnect.dto.request.RegisterRequest;
import com.socialnetwork.weconnect.dto.response.AuthenticationResponse;
import com.socialnetwork.weconnect.entity.Otp;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.exception.AppException;
import com.socialnetwork.weconnect.exception.ErrorCode;
import com.socialnetwork.weconnect.repository.OtpRepository;
import com.socialnetwork.weconnect.repository.UserRepository;
import com.socialnetwork.weconnect.token.Token;
import com.socialnetwork.weconnect.token.TokenRepository;
import com.socialnetwork.weconnect.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
	UserRepository userRepository;
	TokenRepository tokenRepository;
	PasswordEncoder passwordEncoder;
	JwtService jwtService;
	AuthenticationManager authenticationManager;
	OtpRepository otpRepository;

	public AuthenticationResponse register(RegisterRequest request) {
		Optional<User> userCheck = userRepository.findByEmail(request.getEmail());
		if (userCheck.isPresent()) {
			throw new AppException(ErrorCode.USER_EXITED);
		}
		var user = User.builder().name(request.getUserName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).role(request.getRole()).build();
		var savedUser = userRepository.save(user);
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		saveUserToken(savedUser, jwtToken);

		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	public AuthenticationResponse authenticate(LoginRequest request) {
		// xac thuc thong tin nguoi dung
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	public void saveUserToken(User user, String jwtToken) {
		var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false)
				.build();
		tokenRepository.save(token);
	}

	public void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken, response);
		if (userEmail != null) {
			var user = userRepository.findByEmail(userEmail).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user, response)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

	public AuthenticationResponse verifyOtp(int optRequest, String email) {
		Optional<User> optinalUser = userRepository.findByEmail(email);
		if (!optinalUser.isPresent()) {
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}
		Optional<Otp> optionalOtp = otpRepository.findByUserId(optinalUser.get().getId());
		if (optionalOtp.isPresent()) {
			if (optRequest == optionalOtp.get().getOtpNumber()) {
				var jwtToken = jwtService.generateToken(optinalUser.get());
				var refreshToken = jwtService.generateRefreshToken(optinalUser.get());
				revokeAllUserTokens(optinalUser.get());
				saveUserToken(optinalUser.get(), jwtToken);
				return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
			} else {
				throw new AppException(ErrorCode.OTP_INVALID);
			}
		}
		throw new AppException(ErrorCode.OTP_NOT_FOUND);
	}

	// trường hợp dag k login và dag login va quen mk
	public AuthenticationResponse forgotPassword(EmailRequest emailRequest) {
		Optional<User> optinalUser = userRepository.findByEmail(emailRequest.getEmail());
		if (!optinalUser.isPresent()) {
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}
		var jwtToken = jwtService.generateToken(optinalUser.get());
		var refreshToken = jwtService.generateRefreshToken(optinalUser.get());
		revokeAllUserTokens(optinalUser.get());
		saveUserToken(optinalUser.get(), jwtToken);
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	public String changePassword(ChangePasswordRequest changePasswordRequest) {
		// valid token
		var isTokenValid = tokenRepository.findByToken(changePasswordRequest.getToken())
				.map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
		if (!isTokenValid) {
			throw new AppException(ErrorCode.TOKEN_INVALID);
		}
		User user = tokenRepository.findUserByToken(changePasswordRequest.getToken());
		// check if the current password is correct
		if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
			throw new IllegalStateException("Wrong password");
		}
		// check if the two new passwords are the same
		if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmationPassword())) {
			throw new IllegalStateException("Password are not the same");
		}
		// update the password
		user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
		user = userRepository.save(user);
		if (user == null) {
			throw new AppException(ErrorCode.PASSWORD_CHANGE_FAILED);
		}
		return "Password changed successfully";
	}

	@Transactional
	public String deleteUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean existUser = userRepository.existsById(user.getId());
		if (!existUser) {
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}
		int resultDelete = userRepository.deleteUserById(user.getId());
		if (resultDelete != 1) {
			return "Xử lý xoá không thành công";
		}
		return "Xử lý xoá thành công";
	}

	public int login(LoginRequest loginRequest) {
		var user = userRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//		if (condition) {
//			
//		}
return 1;
	}

	public String generatorOtpNumber() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "").substring(0, 6);
	}

}
