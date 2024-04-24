package com.socialnetwork.weconnect.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.dto.request.CommentRequest;
import com.socialnetwork.weconnect.dto.request.UpdateCommentRequest;
import com.socialnetwork.weconnect.dto.response.CntResponse;
import com.socialnetwork.weconnect.entity.Comment;

@Service
public interface CommentService {

	public Comment getCommentById(Integer commentId);
	
	public List<Comment> getAllCommentsByPostId(Integer postId);

	public Comment addComment(CommentRequest newcomment);

	public Comment updateCommentById(UpdateCommentRequest updateCommentRequest);

	public CntResponse delCommentById(Integer commentId);

	public CntResponse lockCommentById(Integer commentId, boolean isLock);
	
}
