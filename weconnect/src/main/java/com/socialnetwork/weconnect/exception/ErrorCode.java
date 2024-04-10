package com.socialnetwork.weconnect.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
public enum ErrorCode {
	INVALID_KEY(1001, "Invalid enum key", HttpStatus.BAD_REQUEST),
	USER_EXITED(1002, "User existed", HttpStatus.BAD_REQUEST),
	UPLOAD_IMAGE_INVALID(1007, "One or more files are too large!", HttpStatus.BAD_REQUEST),
	EMAIL_NOT_EMPTY(10, "Email not empty", HttpStatus.BAD_REQUEST),
	DATE_FORMAT_INVALID(9, "Format date does not match YYYY-MM-DD", HttpStatus.BAD_REQUEST),
	USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
	PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST), 
	USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
	RECORD_NOT_EXISTED(1017, "Record not existed", HttpStatus.NOT_FOUND),
	UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.BAD_REQUEST), 
	IMAGE_NOT_EXISTED(1008,"Image not existed", HttpStatus.NOT_FOUND),
	URL_INVALID(1009,"Invalid URL", HttpStatus.BAD_REQUEST),
	BIRTHDAY_INVALID(1022,"Invalid birthday", HttpStatus.BAD_REQUEST),
	UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(1010, "You do not have permission", HttpStatus.FORBIDDEN),
	POST_NOT_EXISTED(1011,"Post not existed", HttpStatus.NOT_FOUND),
	LIKE_FAILED(1012,"Like failed", HttpStatus.BAD_REQUEST),
	COMMENT_FAILED(1016,"Comment failed", HttpStatus.BAD_REQUEST),
	UPDATE_COMMENT_FAILED(1018,"Update comment failed", HttpStatus.BAD_REQUEST),
	LOCK_COMMENT_FAILED(1017,"Lock comment failed", HttpStatus.BAD_REQUEST),
	SEND_FRIEND_FAILED(1019, "Send Friend Request failed", HttpStatus.BAD_REQUEST),
	CANCEL_FRIEND_FAILED(1020, "Cancel Friend Request failed", HttpStatus.BAD_REQUEST),
	ACCEPT_FRIEND_FAILED(1021, "Accept Friend Request failed", HttpStatus.BAD_REQUEST),
	INVALID_DATA(1013, "Updated data and original data have no difference", HttpStatus.BAD_REQUEST),
	COMMENT_NOT_EXISTED(1014,"Comment not existed", HttpStatus.NOT_FOUND),
	LIST_COMMENT_EMPTY(1016,"There are no comments for postId", HttpStatus.OK),
	TOKEN_NOT_FOUND(1015,"Token not found", HttpStatus.NOT_FOUND),
	TOKEN_INVALID(1028,"Token invalid", HttpStatus.BAD_REQUEST),
	INVALID_SIGNATURE(1024,"The JWT signature does not match the locally computed signature", HttpStatus.BAD_REQUEST),
	DUPLICATE_ID(1025,"Cannot friend yourself or already accepted/declined friend request from yourself.", HttpStatus.BAD_REQUEST),
	OTP_INVALID(1026, "OTP_INVALID", HttpStatus.BAD_REQUEST),
	OTP_NOT_FOUND(1027,"OTP not found", HttpStatus.NOT_FOUND),
	PASSWORD_CHANGE_FAILED(1028, "Password change unsuccessful", HttpStatus.BAD_REQUEST),
	EDIT_INFO_FAILED(1029, "Information user change unsuccessful", HttpStatus.BAD_REQUEST),
	TOKEN_EMPTY(1023,"Token is empty", HttpStatus.BAD_REQUEST);

	int code;
	String message;
	private HttpStatus statusCode;

	private ErrorCode(int code, String message, HttpStatus statusCode) {
		this.code = code;
		this.message = message;
		this.statusCode = statusCode;
	}

}
