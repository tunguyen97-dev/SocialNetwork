package com.socialnetwork.weconnect.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.Service.CommentService;
import com.socialnetwork.weconnect.dto.request.CommentRequest;
import com.socialnetwork.weconnect.dto.request.UpdateCommentRequest;
import com.socialnetwork.weconnect.entity.Comment;
import com.socialnetwork.weconnect.entity.Post;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.exception.AppException;
import com.socialnetwork.weconnect.exception.ErrorCode;
import com.socialnetwork.weconnect.repository.CommentRepositoty;
import com.socialnetwork.weconnect.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {

	CommentRepositoty commentRepository;
	PostRepository postRepository;

	@Override
	public Comment getCommentById(Integer commentId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<Comment> optionalComment = commentRepository.findById(commentId);
		if (optionalComment.isPresent()) {
			Comment comment = optionalComment.get();
			if (!comment.getUser().getId().equals(user.getId())) {
				throw new AppException(ErrorCode.UNAUTHORIZED);
			}
			return comment;
		} else {
			throw new AppException(ErrorCode.COMMENT_NOT_EXISTED);
		}
	}

	@Override
	public Comment addComment(CommentRequest newcomment) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!postRepository.existsById(newcomment.getPostId())) {
			throw new AppException(ErrorCode.POST_NOT_EXISTED);
		}

		Post post = postRepository.findPostById(newcomment.getPostId());
		Comment comment = Comment.builder()
				.content(newcomment.getContent())
				.post(post)
				.user(user)
				.createdAt(sdf.format(new Date()))
				.isDeleted(false)
				.build();
		Comment commentResult = commentRepository.save(comment);
		if (commentResult == null) {
			throw new AppException(ErrorCode.COMMENT_FAILED);
		}
		return commentResult;
	}

	@Override
	public Comment updateCommentById(UpdateCommentRequest updateCommentRequest) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Optional<Comment> optionalComment = commentRepository.findById(updateCommentRequest.getCommentId());
		if (optionalComment.isPresent()) {
			Comment comment = optionalComment.get();
			if (!comment.getUser().getId().equals(user.getId())) {
				throw new AppException(ErrorCode.UNAUTHORIZED);
			}
			comment.setContent(updateCommentRequest.getContent());
			comment.setUpdatedAt(sdf.format(new Date()));
			comment.setIsDeleted(false);
			Comment commentResult = commentRepository.save(comment);
			if (commentResult == null) {
				throw new AppException(ErrorCode.UPDATE_COMMENT_FAILED);
			}
			return commentResult;
			
		} else {
			throw new AppException(ErrorCode.COMMENT_NOT_EXISTED);
		}
	}

	@Override
	public String delCommentById(Integer commentId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<Comment> optionalComment = commentRepository.findById(commentId);
		if (optionalComment.isPresent()) {
			Comment comment = optionalComment.get();
			if (!comment.getUser().getId().equals(user.getId())) {
				throw new AppException(ErrorCode.UNAUTHORIZED);
			}
			commentRepository.deleteById(commentId);
			return "Đã xoá comment thành công";
		} else {
			throw new AppException(ErrorCode.COMMENT_NOT_EXISTED);
		}
	}

	@Override
	public String lockCommentById(Integer commentId) {
		Optional<Comment> optionalComment = commentRepository.findById(commentId);
		if (optionalComment.isPresent()) {
			Comment comment = optionalComment.get();
			comment.setIsDeleted(true);
			Comment commentResult = commentRepository.save(comment);
			if (commentResult == null) {
				throw new AppException(ErrorCode.LOCK_COMMENT_FAILED);
			}
			return "Đã khoá comment thành công";
		} else {
			throw new AppException(ErrorCode.COMMENT_NOT_EXISTED);
		}
	}

	@Override
	public List<Comment> getAllCommentsByPostId(Integer postId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<Post> optionalPost = postRepository.findById(postId);
		Post post = new Post();
		if (optionalPost.isPresent()) {
			post = optionalPost.get();
		    if (!post.getUser().getId().equals(user.getId())) {
				throw new AppException(ErrorCode.UNAUTHORIZED);
			}
		} else {
			throw new AppException(ErrorCode.POST_NOT_EXISTED);
		}
		return post.getPostComments();
	}
}
