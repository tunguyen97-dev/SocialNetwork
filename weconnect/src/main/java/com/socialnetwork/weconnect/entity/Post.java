package com.socialnetwork.weconnect.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
	private User user;
	
	private String content;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<PostLike> postLikes;

	@ElementCollection
	@CollectionTable(name = "post_images")
	@JsonIgnore
	private List<String> postImages;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@JsonIgnore
	private List<Comment> postComments;
	
	private String createdAt;
	private String updatedAt;
	private Boolean isDeleted;
}
