package com.socialnetwork.weconnect.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
	INVALID_KEY(1001, "Sai key enum"),
	USER_EXITED(1002, "User existed"),
	UPLOAD_IMAGE_INVALID(1007, "One or more files are too large!"),
	USERNAME_INVALID(1003, "Username must be at least 3 characters"),
	PASSWORD_INVALID(1004, "Password must be at least 8 characters"), 
	USER_NOT_EXISTED(1005, "User not existed"),
	UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"), 
	IMAGE_NOT_EXISTED(1008,"Image not existed"),
	URL_INVALID(1009,"Invalid URL"),
	UNAUTHENTICATED(1006, "Unauthenticated");

	int code;
	String message;

	private ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
