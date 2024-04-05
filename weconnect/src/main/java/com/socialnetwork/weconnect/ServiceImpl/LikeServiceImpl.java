package com.socialnetwork.weconnect.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socialnetwork.weconnect.Service.LikeService;
import com.socialnetwork.weconnect.entity.PostLike;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.exception.AppException;
import com.socialnetwork.weconnect.exception.ErrorCode;
import com.socialnetwork.weconnect.repository.LikeRepository;
import com.socialnetwork.weconnect.repository.PostRepository;
import com.socialnetwork.weconnect.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeServiceImpl implements LikeService {

	PostRepository postRepository;
	UserRepository userRepository;
	LikeRepository likeRepository;

	@Transactional
	@Override
	public String likePostByPostId(Integer postId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (!userRepository.existsById(user.getId())) {
			throw new AppException(ErrorCode.USER_NOT_EXISTED);
		}
		if (!postRepository.existsById(postId)) {
			throw new AppException(ErrorCode.POST_NOT_EXISTED);
		}

		 // Kiểm tra xem bài viết đã được like bởi user hiện tại hay chưa
	    PostLike existingLike = likeRepository.findByPostIdAndUserId(postId, user.getId());
	    if (existingLike != null) {
	        // Đã like bài viết, thì xoá đi/unlike
	        likeRepository.deleteById(existingLike.getId());;
	        return "Đã unlike bài viết";
	    } else {
	        // Chưa like bài viết, thực hiện insert/like
	        PostLike postLike = PostLike.builder()
	            .createdAt(sdf.format(new Date()))
	            .post(postRepository.findPostById(postId))
	            .user(userRepository.findUserById(user.getId()))
	            .build();

	        PostLike postLikeResult = likeRepository.save(postLike);
	        if (postLikeResult == null) {
	        	throw new AppException(ErrorCode.LIKE_FAILED);
	        }
	        return "Đã like thành công";
	    }
}
}
