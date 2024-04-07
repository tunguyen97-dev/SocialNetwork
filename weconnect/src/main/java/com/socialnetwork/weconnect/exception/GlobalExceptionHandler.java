package com.socialnetwork.weconnect.exception;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.socialnetwork.weconnect.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
		apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

		return ResponseEntity.badRequest().body(apiResponse);
	}

	@ExceptionHandler(value = BadCredentialsException.class)
	ResponseEntity<ApiResponse> handlingBadCredentialsException(BadCredentialsException exception) {
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(ErrorCode.USER_NOT_EXISTED.getCode());
		apiResponse.setMessage(ErrorCode.USER_NOT_EXISTED.getMessage());

		return ResponseEntity.badRequest().body(apiResponse);
	}

	@ExceptionHandler(value = EmptyResultDataAccessException.class)
	ResponseEntity<ApiResponse> handlingEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(ErrorCode.RECORD_NOT_EXISTED.getCode());
		apiResponse.setMessage(ErrorCode.RECORD_NOT_EXISTED.getMessage());

		return ResponseEntity.badRequest().body(apiResponse);
	}

	@ExceptionHandler(value = MaxUploadSizeExceededException.class)
	ResponseEntity<ApiResponse> handlingMaxSizeException(MaxUploadSizeExceededException exception) {
		ApiResponse apiResponse = new ApiResponse();

		apiResponse.setCode(ErrorCode.UPLOAD_IMAGE_INVALID.getCode());
		apiResponse.setMessage(ErrorCode.UPLOAD_IMAGE_INVALID.getMessage());

		return ResponseEntity.badRequest().body(apiResponse);
	}

	@ExceptionHandler(value = AppException.class)
	ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());
		return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

		return ResponseEntity.status(errorCode.getStatusCode())
				.body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<?> handlingValication(MethodArgumentNotValidException exception) {
		List<ApiResponse> listApiResponses = new ArrayList<>();
		ErrorCode errorCode = ErrorCode.INVALID_KEY;
		List<ObjectError> constraintViolation = exception.getBindingResult().getAllErrors();

		switch (constraintViolation.size()) {
		case 1:
			ObjectError objectError = constraintViolation.get(0);
			try {
				errorCode = ErrorCode.valueOf(objectError.getDefaultMessage());
				listApiResponses
						.add(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
			} catch (IllegalArgumentException e) {
				errorCode = ErrorCode.INVALID_KEY;
				listApiResponses
						.add(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
			}
			break;
		default:
			for (ObjectError objectEr : constraintViolation) {
				try {
					errorCode = ErrorCode.valueOf(objectEr.getDefaultMessage());
					listApiResponses.add(
							ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
				} catch (IllegalArgumentException e) {
					errorCode = ErrorCode.INVALID_KEY;
					listApiResponses.clear();
					listApiResponses.add(
							ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());

					break;
				}
			}
			Collections.sort(listApiResponses, Comparator.comparingInt(ApiResponse::getCode));
			break;
		}
		return ResponseEntity.badRequest().body(listApiResponses);
	}
}
