package com.socialnetwork.weconnect.dto.request;

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
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

	@NotEmpty(message = "EMAIL_NOT_EMPTY")
	String email;

	@Size(min = 8, message = "PASSWORD_INVALID")
	String password;

}
