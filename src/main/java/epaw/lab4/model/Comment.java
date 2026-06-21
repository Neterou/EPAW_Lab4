package epaw.lab4.model;

import java.sql.Timestamp;

/** A threaded reply written by a user on a tweet. */
public class Comment implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int tweetId;
	private int userId;
	private String username;
	private String userPicture;
	private String content;
	private Timestamp createdAt;

	public Comment() {
	}

	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }

	public int getTweetId() { return this.tweetId; }
	public void setTweetId(int tweetId) { this.tweetId = tweetId; }

	public int getUserId() { return this.userId; }
	public void setUserId(int userId) { this.userId = userId; }

	public String getUsername() { return this.username; }
	public void setUsername(String username) { this.username = username; }

	public String getUserPicture() { return this.userPicture; }
	public void setUserPicture(String userPicture) { this.userPicture = userPicture; }

	public String getContent() { return this.content; }
	public void setContent(String content) { this.content = content; }

	public Timestamp getCreatedAt() { return this.createdAt; }
	public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
