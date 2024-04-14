package com.socialnetwork.weconnect.dto.request;

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
public class UserInforRequest {	
	String gender;
	
	String avatarUrl;
	
	String birthDay;
	
	String profession;
	
	String address;
}