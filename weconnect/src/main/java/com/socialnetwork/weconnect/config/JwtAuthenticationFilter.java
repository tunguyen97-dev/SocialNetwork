package com.socialnetwork.weconnect.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.beans.Transient;
import java.io.IOException;
import java.security.Security;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.weconnect.Service.JwtService;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.exception.ErrorCode;
import com.socialnetwork.weconnect.token.TokenRepository;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		if (request.getServletPath().contains("/api/v1/auth")) {
			filterChain.doFilter(request, response);
			return;
		}
		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			handleNullJwt(request, response);
		}

		final String jwt;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		userEmail = jwtService.extractUsername(jwt, response);
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
			var isTokenValid = tokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked())
					.orElse(false);
			if (jwtService.isTokenValid(jwt, userDetails, response) && isTokenValid) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			response.setStatus(ErrorCode.TOKEN_NOT_FOUND.getCode());
		}
		filterChain.doFilter(request, response);
	}

	private void handleNullJwt(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Xử lý khi JWT là null, ví dụ trả về mã lỗi hoặc thông báo lỗi
		ErrorCode errorCode = ErrorCode.TOKEN_EMPTY;
		response.setStatus(errorCode.getCode());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ApiResponse<?> apiResponse = ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage())
				.build();

		ObjectMapper objectMapper = new ObjectMapper();

		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

	}
}
