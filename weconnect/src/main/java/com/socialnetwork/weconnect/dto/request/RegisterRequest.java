package com.socialnetwork.weconnect.dto.request;

import com.socialnetwork.weconnect.config.Role;

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
public class RegisterRequest {

	@Size(min = 3, message = "USERNAME INVALID")
	String userName;
	
	String email;
	
	@Size(min = 8, message = "PASSWORD INVALID")
	String password;
	
	Role role;
}
