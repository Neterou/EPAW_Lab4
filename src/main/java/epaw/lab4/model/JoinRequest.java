package epaw.lab4.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A pending request from a user to join a closed bubble. It is a derived view
 * (a PENDING row in {@code user_bubbles}) enriched with the requester's and the
 * bubble's display data, so a bubble owner can review and act on it.
 */
public class JoinRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private int userId;
	private String name;
	private String username;
	private String picture;

	private int bubbleId;
	private String bubbleName;
	private String bubbleCategory;

	private Timestamp requestedAt;

	public JoinRequest() {
		super();
	}

	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPicture() { return picture; }
	public void setPicture(String picture) { this.picture = picture; }

	public Integer getBubbleId() { return bubbleId; }
	public void setBubbleId(Integer bubbleId) { this.bubbleId = bubbleId; }

	public String getBubbleName() { return bubbleName; }
	public void setBubbleName(String bubbleName) { this.bubbleName = bubbleName; }

	public String getBubbleCategory() { return bubbleCategory; }
	public void setBubbleCategory(String bubbleCategory) { this.bubbleCategory = bubbleCategory; }

	public Timestamp getRequestedAt() { return requestedAt; }
	public void setRequestedAt(Timestamp requestedAt) { this.requestedAt = requestedAt; }
}
