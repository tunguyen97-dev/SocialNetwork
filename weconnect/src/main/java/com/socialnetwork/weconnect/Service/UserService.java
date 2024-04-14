package com.socialnetwork.weconnect.Service;

import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.dto.request.UserInforRequest;
import com.socialnetwork.weconnect.dto.response.UserInforRestponse;

@Service
public interface UserService {
	
	UserInforRestponse getInformationUser();

	UserInforRestponse editInformationUser(UserInforRequest userInforRequest);
	
	String deleteUser();
	
}