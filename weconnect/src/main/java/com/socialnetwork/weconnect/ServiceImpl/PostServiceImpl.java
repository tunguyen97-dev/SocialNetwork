package com.socialnetwork.weconnect.ServiceImpl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.Service.FilesStorageService;
import com.socialnetwork.weconnect.Service.PostService;
import com.socialnetwork.weconnect.dto.request.UpdatePostRequest;
import com.socialnetwork.weconnect.entity.Post;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.exception.AppException;
import com.socialnetwork.weconnect.exception.ErrorCode;
import com.socialnetwork.weconnect.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
	
	PostRepository postRepository;
	FilesStorageService storageService;

	@Override
	public void savePostToDB(String content, List<String> files, User user) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Post post = Post.builder()
				.user(user)
				.content(content)
				.postImages(files)
				.createdAt(sdf.format(new Date()))
				.isDeleted(false).build();
		postRepository.save(post);
	}

	@Override
	public List<Post> getAllPostsByUserId(Integer userId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!userId.equals(user.getId())) {
			throw new AppException(ErrorCode.UNAUTHORIZED);
		}
		// kiem tra user có ton tai trong db

		return postRepository.findAllPostsByUserId(userId);
	}

	@Override
	public Post getPostById(Integer postId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// thêm code kiêm tra tồn tại user trong db? => k cần vì có ktra and với post
		// bên dưới
		if (!postRepository.existsByIdAndUserId(postId, user.getId())) {
			throw new AppException(ErrorCode.POST_NOT_EXISTED_OR_USER_NOT_EXISTED);
		}
		return postRepository.findPostById(postId);
	}

	@Override
	public Boolean delPostById(Integer postId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!postRepository.existsByIdAndUserId(postId, user.getId())) {
			throw new AppException(ErrorCode.POST_NOT_EXISTED_OR_USER_NOT_EXISTED);
		}
		postRepository.deleteById(postId);
		return null;
	}

	@Override
	public Post updatePostById(UpdatePostRequest updatePostRequest) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!postRepository.existsByIdAndUserId(updatePostRequest.getPostId(), user.getId())) {
			throw new AppException(ErrorCode.POST_NOT_EXISTED_OR_USER_NOT_EXISTED);
		}

		Post post = postRepository.findPostById(updatePostRequest.getPostId());

		List<String> newImages = new ArrayList<>(); // danh sách url image request
		List<String> addedImages = new ArrayList<>(); // Danh sách file mới được thêm
		List<String> deletedImages = new ArrayList<>(); // Danh sách file bị xóa
		List<String> unchangedImages = new ArrayList<>(); // Danh sách file giữ nguyên
		List<String> newDB = new ArrayList<>();

		// lây về list tên ảnh cũ của post theo id
		Post existingImages = postRepository.findPostImagesById(user.getId());

		for (String file : updatePostRequest.getImageFiles()) {
			newImages.add(file);

			if (!existingImages.getPostImages().contains(file)) {
				String result = storageService.saveToFileServer(file, user.getName());
				addedImages.add(result);
				newDB.add(result); // luu anh them moi vao sv
				// anh them mơi
			} else {
				unchangedImages.add(file); // anh giu nguyen
			}
		}

		// xu ly xoa voi list xoa
		for (String existingImage : existingImages.getPostImages()) {
			if (!unchangedImages.contains(existingImage)) {
				boolean result = storageService.delTosServer(existingImage, user.getName()); // xu lý xoa
				if (result) {
					deletedImages.add(existingImage); // File bị xóa
				}
			} else {
				newDB.add(existingImage);
			}
		}

		if (updatePostRequest.getContent().equals(post.getContent()) && addedImages.size() == 0
				&& deletedImages.size() == 0) {
			throw new AppException(ErrorCode.INVALID_DATA);
		}

		// xử lý post để save db
		if (!updatePostRequest.getContent().equals(post.getContent())) {
			post.setContent(updatePostRequest.getContent());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		post.setPostImages(newDB);
		post.setUpdatedAt(sdf.format(new Date()));
		return postRepository.save(post);
	}

}
