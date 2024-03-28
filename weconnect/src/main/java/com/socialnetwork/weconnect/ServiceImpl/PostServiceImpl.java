package com.socialnetwork.weconnect.ServiceImpl;

import java.security.Principal;
import java.util.List;
import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.Service.PostService;
import com.socialnetwork.weconnect.dto.response.PostDto;
import com.socialnetwork.weconnect.dto.response.PostInfoDto;
import com.socialnetwork.weconnect.entity.Post;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.repository.PostRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
	PostRepository postRepository;

	@Override
	public void savePostToDB(String content, List<String> files, User user) {
		Post post = new Post();
		post.setContent(content);
		post.setUser(user);
		post.setImages(files);
		postRepository.save(post);
	}

	@Override
	public List<PostInfoDto> getAllPostsByUserId(Integer userId, Principal connectedUser) {
		List<PostInfoDto> infoDto = postRepository.getAllPostsByUserId(userId);
		for (PostInfoDto postInfoDto : infoDto) {
			System.out.println(postInfoDto.getPostId());
			System.out.println(postInfoDto.getContent());
			System.out.println(postInfoDto.getCreatedAt());
			System.out.println(postInfoDto.getUpdateAt());
			System.out.println(postInfoDto.getPostImages());
			System.out.println(postInfoDto.getPostComments());
		}
		return infoDto;
	}
}
