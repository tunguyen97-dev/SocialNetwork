package com.socialnetwork.weconnect.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.socialnetwork.weconnect.dto.request.ChangePasswordRequest;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.repository.UserRepository;
import com.socialnetwork.weconnect.token.TokenRepository;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

	PasswordEncoder passwordEncoder;
	JwtService jwtService;
	UserRepository repository;
	TokenRepository tokenRepository;

	public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

		// check if the current password is correct
		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new IllegalStateException("Wrong password");
		}
		// check if the two new passwords are the same
		if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
			throw new IllegalStateException("Password are not the same");
		}

		// update the password
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		repository.save(user);
	}

	public String forgotPassword(Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

		String userEmail = user.getEmail(); // jwtService.extractUsername(jwt);
		// Todo xử lý gửi link qua email của user
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty()) {
			// Xem xu ly tao token moi
			return null;
		}
		String resetPasswordUrl = "http://localhost:8080/api/v1/users/reset-password?token="
				+ validUserTokens.get(0).token.toString();
		return resetPasswordUrl;
	}
}
