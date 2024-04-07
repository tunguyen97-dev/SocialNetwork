package com.socialnetwork.weconnect.config;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.exception.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//	int code = 0;
//
//	public JwtAuthenticationEntryPoint(int code) {
//		super();
//		this.code = code;
//	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		int errorCode = response.getStatus();
		if (errorCode == ErrorCode.TOKEN_EMPTY.getCode()) {
			response.setStatus(ErrorCode.TOKEN_EMPTY.getStatusCode().value());
			response.flushBuffer();
			return;
		} else if (errorCode == ErrorCode.INVALID_SIGNATURE.getCode()) {
			response.setStatus(ErrorCode.INVALID_SIGNATURE.getStatusCode().value());
			ErrorCode errorCd = ErrorCode.INVALID_SIGNATURE;
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ApiResponse<?> apiResponse = ApiResponse.builder().code(errorCd.getCode()).message(errorCd.getMessage())
					.build();
			ObjectMapper objectMapper = new ObjectMapper();
			response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
			return;
		} else if (errorCode == ErrorCode.TOKEN_NOT_FOUND.getCode()) {
			ErrorCode errorCd = ErrorCode.TOKEN_NOT_FOUND;
			response.setStatus(errorCd.getStatusCode().value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ApiResponse<?> apiResponse = ApiResponse.builder().code(errorCd.getCode()).message(errorCd.getMessage())
					.build();
			ObjectMapper objectMapper = new ObjectMapper();
			response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
			return;
		}
		response.reset();
		ErrorCode errorCd = ErrorCode.UNAUTHENTICATED;

		response.setStatus(errorCd.getStatusCode().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ApiResponse<?> apiResponse = ApiResponse.builder().code(errorCd.getCode()).message(errorCd.getMessage())
				.build();

		ObjectMapper objectMapper = new ObjectMapper();

		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
		response.flushBuffer();
	}
}
