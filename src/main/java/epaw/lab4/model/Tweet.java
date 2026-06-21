package epaw.lab4.model;

import java.sql.Timestamp;

/**
 * A post broadcast inside a Bubble. Carries derived display data
 * (author name/picture, like and comment counts, whether the current
 * viewer has liked it) so the view layer can render without extra trips.
 */
public class Tweet implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int userId;
	private String username;
	private String userPicture;
	private int bubbleId;
	private String content;
	private String type;            // STANDARD | OFFICIAL | ALERT | WARNING
	private Timestamp createdAt;

	// derived
	private int likeCount;
	private int commentCount;
	private boolean likedByMe;

	public Tweet() {
	}

	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }

	public int getUserId() { return this.userId; }
	public void setUserId(int userId) { this.userId = userId; }

	public String getUsername() { return this.username; }
	public void setUsername(String username) { this.username = username; }

	public String getUserPicture() { return this.userPicture; }
	public void setUserPicture(String userPicture) { this.userPicture = userPicture; }

	public int getBubbleId() { return this.bubbleId; }
	public void setBubbleId(int bubbleId) { this.bubbleId = bubbleId; }

	public String getContent() { return this.content; }
	public void setContent(String content) { this.content = content; }

	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }

	public Timestamp getCreatedAt() { return this.createdAt; }
	public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

	public int getLikeCount() { return this.likeCount; }
	public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

	public int getCommentCount() { return this.commentCount; }
	public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

	public boolean isLikedByMe() { return this.likedByMe; }
	public void setLikedByMe(boolean likedByMe) { this.likedByMe = likedByMe; }
}
