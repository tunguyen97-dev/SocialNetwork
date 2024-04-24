package com.socialnetwork.weconnect.dto.request;

import com.socialnetwork.weconnect.Validator.DobConstraint;
import com.socialnetwork.weconnect.config.Role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {

	@Size(min = 3, message = "USERNAME_INVALID")
	String userName;
	
	@NotEmpty(message = "EMAIL_NOT_EMPTY")
	String email;
	
	@Size(min = 8, message = "PASSWORD_INVALID")
	String password;
	
	@DobConstraint(min = 18, message = "LOCK_COMMENT_FAILED")
	String birthday;
}
