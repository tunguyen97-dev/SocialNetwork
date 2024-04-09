package com.socialnetwork.weconnect.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.beans.Transient;
import java.io.IOException;
import java.security.Security;
import java.util.List;

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

import ch.qos.logback.core.joran.conditional.IfAction;
import io.jsonwebtoken.lang.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		if (request.getServletPath().contains("/api/v1/auth")
				|| request.getServletPath().contains("/swagger-ui")
				|| request.getServletPath().contains("/configuration")
				|| request.getServletPath().contains("/webjars")
				|| request.getServletPath().contains("/swagger-resources")
				|| request.getServletPath().contains("/v2/api-docs")
				|| request.getServletPath().contains("/v3/api-docs")) {
			filterChain.doFilter(request, response);
			return;
		}
		final String authHeader = request.getHeader("Authorization");

		final String jwt;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatus(ErrorCode.TOKEN_EMPTY.getCode());
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

}
