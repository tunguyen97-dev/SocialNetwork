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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeServiceImpl implements LikeService {

	PostRepository postRepository;
	LikeRepository likeRepository;

	@Transactional
	@Override
	public String likePostByPostId(Integer postId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!postRepository.existsById(postId)) {
			throw new AppException(ErrorCode.POST_NOT_EXISTED);
		}

	    PostLike existingLike = likeRepository.findByPostIdAndUserId(postId, user.getId());
	    if (existingLike != null) {
	        likeRepository.deleteById(existingLike.getId());;
	        return "Đã unlike bài viết";
	    } else {
	        PostLike postLike = PostLike.builder()
	            .createdAt(sdf.format(new Date()))
	            .post(postRepository.findPostById(postId))
	            .user(user)
	            .build();
	        PostLike postLikeResult = likeRepository.save(postLike);
	        if (postLikeResult == null) {
	        	throw new AppException(ErrorCode.LIKE_FAILED);
	        }
	        return "Đã like thành công";
	    }
}
}
