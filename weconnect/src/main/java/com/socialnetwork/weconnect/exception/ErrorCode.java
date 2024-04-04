package com.socialnetwork.weconnect.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {
	INVALID_KEY(1001, "Sai key enum", HttpStatus.BAD_REQUEST),
	USER_EXITED(1002, "User existed", HttpStatus.BAD_REQUEST),
	UPLOAD_IMAGE_INVALID(1007, "One or more files are too large!", HttpStatus.BAD_REQUEST),
	USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
	PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST), 
	USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
	UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.BAD_REQUEST), 
	IMAGE_NOT_EXISTED(1008,"Image not existed", HttpStatus.NOT_FOUND),
	URL_INVALID(1009,"Invalid URL", HttpStatus.BAD_REQUEST),
	UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(1010, "You do not have permission", HttpStatus.FORBIDDEN),
	POST_NOT_EXISTED(1011,"Post not existed", HttpStatus.NOT_FOUND),
	LIKE_FAILED(1012,"Like failed", HttpStatus.BAD_REQUEST),
	COMMENT_FAILED(1016,"Comment failed", HttpStatus.BAD_REQUEST),
	UPDATE_COMMENT_FAILED(1018,"Update comment failed", HttpStatus.BAD_REQUEST),
	LOCK_COMMENT_FAILED(1017,"Lock comment failed", HttpStatus.BAD_REQUEST),
	INVALID_DATA(1013, "Updated data and original data have no difference", HttpStatus.BAD_REQUEST),
	COMMENT_NOT_EXISTED(1014,"Comment not existed", HttpStatus.NOT_FOUND),
	LIST_COMMENT_EMPTY(1016,"There are no comments for postId", HttpStatus.OK),
	TOKEN_NOT_EXISTED(1015,"Token not existed", HttpStatus.NOT_FOUND);

	int code;
	String message;
	private HttpStatus statusCode;

	private ErrorCode(int code, String message, HttpStatus statusCode) {
		this.code = code;
		this.message = message;
		this.statusCode = statusCode;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
