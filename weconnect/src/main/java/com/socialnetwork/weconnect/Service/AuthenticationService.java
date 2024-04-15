package com.socialnetwork.weconnect.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.weconnect.dto.request.AuthenticationRequest;
import com.socialnetwork.weconnect.dto.request.ChangePasswordRequest;
import com.socialnetwork.weconnect.dto.request.EmailRequest;
import com.socialnetwork.weconnect.dto.request.RegisterRequest;
import com.socialnetwork.weconnect.dto.request.VerifyRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

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

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
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

	public int login(AuthenticationRequest authenRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenRequest.getEmail(), authenRequest.getPassword()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		var user = userRepository.findByEmail(authenRequest.getEmail()).orElseThrow();
		int otpNumber = generateOtp();
		Otp otp = Otp.builder().otpNumber(otpNumber).user(user).createdAt(sdf.format(new Date())).build();
		otp = otpRepository.save(otp);
		if (otp == null) {
			throw new AppException(ErrorCode.LOGIN_FAILED);
		}
		return otpNumber;

	}

	public AuthenticationResponse verifyOtp(VerifyRequest verifyRequest) {
		Optional<User> optinalUser = userRepository.findByEmail(verifyRequest.getEmail());
		if (!optinalUser.isPresent()) {
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}
		Optional<Otp> optionalOtp = otpRepository.findByUserId(optinalUser.get().getId());
		if (!optionalOtp.isPresent()) {
			throw new AppException(ErrorCode.OTP_NOT_FOUND);
		}
		if (verifyRequest.getOtpNumber() == optionalOtp.get().getOtpNumber()) {
			var jwtToken = jwtService.generateToken(optinalUser.get());
			var refreshToken = jwtService.generateRefreshToken(optinalUser.get());
			revokeAllUserTokens(optinalUser.get());
			saveUserToken(optinalUser.get(), jwtToken);
			return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
		}
		throw new AppException(ErrorCode.OTP_INVALID);
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
		Optional<Token> token = tokenRepository.findByToken(changePasswordRequest.getToken()).map(t -> {
			if (t.isExpired() || t.isRevoked()) {
				return null;
			} else {
 				return t;
			}
		});

		if (!token.isPresent()) {
			throw new AppException(ErrorCode.TOKEN_INVALID);
		}

		User user = token.get().getUser();
//		if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
//			throw new IllegalStateException("Wrong password");
//		}
		if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmationPassword())) {
			throw new IllegalStateException("Password are not the same");
		}
		user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
		user = userRepository.save(user);
		if (user == null) {
			throw new AppException(ErrorCode.PASSWORD_CHANGE_FAILED);
		}
		return "Password changed successfully";
	}

	public int generateOtp() {
		UUID uuid = UUID.randomUUID();
		String otpString = uuid.toString().replaceAll("[^0-9]", "");
		int otp = Integer.parseInt(otpString.substring(0, 5));
		return otp;
	}

}
