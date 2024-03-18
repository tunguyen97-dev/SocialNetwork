package com.socialnetwork.weconnect.entity;

import lombok.Data;

import java.security.Timestamp;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Data
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String content;
	private Timestamp created_at;
	private Timestamp update__at;
	@OneToMany(mappedBy = "post")
	private List<PostLike> postLikes;

	@ElementCollection
	private List<String> images;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments;

	@OneToMany(mappedBy = "post")
	private List<PostLike> likes;

	private Boolean isDeleted;
}
