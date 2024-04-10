package com.socialnetwork.weconnect.ServiceImpl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.Service.UserService;
import com.socialnetwork.weconnect.dto.request.UserInforRequest;
import com.socialnetwork.weconnect.dto.response.UserInforRestponse;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.exception.AppException;
import com.socialnetwork.weconnect.exception.ErrorCode;
import com.socialnetwork.weconnect.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserInforImpl implements UserService {

	UserRepository userRepository;

	@Override
	public UserInforRestponse getInformationUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return UserInforRestponse.builder()
				.userName(user.getName())
				.avatarUrl(user.getAvataUrl())
				.profession(user.getProfession())
				.birthDay(user.getBirthday())
				.address(user.getAddress())
				.build();
	}

	@Override
	public UserInforRestponse editInformationUser(UserInforRequest userInforRequest) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userUpdate = User.builder()
				.avataUrl(userInforRequest.getAvatarUrl())
				.profession(userInforRequest.getProfession())
				.birthday(userInforRequest.getBirthDay())
				.address(userInforRequest.getAddress())
				.build();
		userUpdate = userRepository.save(userUpdate);
		if (userUpdate == null) {
			throw new AppException(ErrorCode.EDIT_INFO_FAILED);
		}
		return UserInforRestponse.builder()
				.userName(userUpdate.getName())
				.avatarUrl(userUpdate.getAvataUrl())
				.profession(userUpdate.getProfession())
				.birthDay(userUpdate.getBirthday())
				.address(userUpdate.getAddress())
				.build();
	}
	
	

}
