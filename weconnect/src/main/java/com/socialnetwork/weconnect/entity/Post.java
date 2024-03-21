package com.socialnetwork.weconnect.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.security.Timestamp;
import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
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
@NoArgsConstructor
@AllArgsConstructor
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
	@JsonIgnore
	private List<String> images;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@JsonIgnore
	private List<Comment> comments;

	@OneToMany(mappedBy = "post")
	@JsonIgnore
	private List<PostLike> likes;

	private Boolean isDeleted;
}
