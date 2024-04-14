package com.socialnetwork.weconnect.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

//@Data
//@Builder
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
public interface ReportResponse {
	@Value("#{target.postCount}")
	int getPostCount();
	@Value("#{target.likeCount}")
	int getLikeCount();
	@Value("#{target.commentCount}")
	int getCommentCount();
	@Value("#{target.newFriendCount}")
	int getNewFriendCount();
}
