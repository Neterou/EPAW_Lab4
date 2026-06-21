<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${empty bubble}">
	<div class="w3-center w3-padding-large" style="color:var(--muted)">
		<p>This bubble could not be found.</p>
	</div>
</c:when>
<c:otherwise>

<input type="hidden" id="feedBubbleId" value="${bubble.id}">

<!-- Bubble header -->
<div class="feed-header">
	<div style="display:flex; align-items:flex-start; gap:8px">
		<div class="grow" style="flex:1">
			<h2>${bubble.name}</h2>
			<div class="meta">
				<span class="cat-chip ${bubble.category}">${bubble.category}</span>
				<i class="fa fa-map-marker"></i> ${bubble.city}
				&middot; <i class="fa fa-users"></i> ${bubble.memberCount}
				<c:choose>
					<c:when test="${bubble.open}">&middot; <i class="fa fa-globe"></i> Open</c:when>
					<c:otherwise>&middot; <i class="fa fa-lock"></i> Closed</c:otherwise>
				</c:choose>
			</div>
		</div>
		<!-- Join / Leave control (hidden for guests) -->
		<c:if test="${!isGuest}">
		<c:choose>
			<c:when test="${isMember}">
				<button class="leaveFromFeed w3-button w3-light-grey w3-round w3-small"><i class="fa fa-check"></i> Member</button>
			</c:when>
			<c:when test="${membership == 'PENDING'}">
				<button class="leaveFromFeed w3-button w3-pale-yellow w3-round w3-small"><i class="fa fa-clock-o"></i> Requested</button>
			</c:when>
			<c:when test="${bubble.open}">
				<button class="joinFromFeed w3-button w3-theme w3-round w3-small"><i class="fa fa-plus"></i> Join</button>
			</c:when>
			<c:otherwise>
				<button class="joinFromFeed w3-button w3-theme w3-round w3-small"><i class="fa fa-paper-plane"></i> Request</button>
			</c:otherwise>
		</c:choose>
		</c:if>
	</div>
</div>

<!-- Guest banner -->
<c:if test="${isGuest}">
	<div class="guest-banner">
		<i class="fa fa-eye fa-lg"></i>
		<div>
			<strong>You're browsing as a guest</strong>
			<span>Sign up or log in to join bubbles, like posts, and participate in your community.</span>
		</div>
		<div class="guest-banner-actions">
			<a class="menu w3-button w3-theme w3-round w3-small" href="Register"><i class="fa fa-user-plus"></i> Sign Up</a>
			<a class="menu w3-button w3-light-grey w3-round w3-small" href="Login"><i class="fa fa-sign-in"></i> Log In</a>
		</div>
	</div>
</c:if>

<!-- Compose / participation notice (authenticated only) -->
<c:if test="${!isGuest}">
<c:choose>
	<c:when test="${canPost}">
		<div class="compose">
			<textarea id="composeContent" rows="2" maxlength="280"
				placeholder="Broadcast a post to ${bubble.name}..."></textarea>
			<div style="display:flex; gap:8px; align-items:center; margin-top:8px">
				<select id="composeType" class="w3-select w3-border" style="width:auto; padding:4px">
					<option value="STANDARD">Standard</option>
					<option value="OFFICIAL">Official</option>
					<option value="ALERT">Local alert</option>
					<option value="WARNING">Safety warning</option>
				</select>
				<button id="postBtn" class="w3-button w3-theme w3-round" style="margin-left:auto">
					<i class="fa fa-paper-plane"></i> Post
				</button>
			</div>
		</div>
	</c:when>
	<c:when test="${isMember}">
		<div class="compose-locked">
			<i class="fa fa-lock"></i>
			Only bubble admins can create posts. You can reply to any post below.
		</div>
	</c:when>
	<c:otherwise>
		<div class="compose-locked">
			<i class="fa fa-info-circle"></i>
			You are not a member of this bubble.
			<strong><a class="joinFromFeed" style="cursor:pointer; color:var(--teal-700)">Join</a></strong>
			to like and reply to posts.
		</div>
	</c:otherwise>
</c:choose>
</c:if>

<!-- Posts -->
<c:choose>
<c:when test="${empty tweets}">
	<div class="w3-center w3-padding-large" style="color:var(--muted)">
		<p>No posts in this bubble yet.</p>
	</div>
</c:when>
<c:otherwise>
<c:forEach var="t" items="${tweets}">
	<div class="post type-${t.type}" data-id="${t.id}">
		<div style="display:flex; align-items:center; gap:8px">
			<c:choose>
				<c:when test="${not empty t.userPicture}">
					<img class="avatar" src="${t.userPicture}" alt="">
				</c:when>
				<c:otherwise>
					<span class="avatar"><i class="fa fa-user"></i></span>
				</c:otherwise>
			</c:choose>
			<a class="userLink author" data-user="${t.userId}" style="cursor:pointer; color:inherit; text-decoration:none">@${t.username}</a>
			<c:if test="${t.type != 'STANDARD'}">
				<span class="badge ${t.type}">${t.type}</span>
			</c:if>
			<span class="time" style="margin-left:auto">${t.createdAt}</span>
		</div>

		<div class="body">${t.content}</div>

		<div class="actions">
			<c:choose>
				<c:when test="${isGuest}">
					<span class="guest-stat"><i class="fa fa-heart"></i> <span class="likeCount">${t.likeCount}</span></span>
				</c:when>
				<c:when test="${isMember}">
					<a class="likeBtn ${t.likedByMe ? 'liked' : ''}">
						<i class="fa fa-heart"></i> <span class="likeCount">${t.likeCount}</span>
					</a>
				</c:when>
				<c:otherwise>
					<span><i class="fa fa-heart"></i> <span class="likeCount">${t.likeCount}</span></span>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${isGuest}">
					<span class="guest-stat"><i class="fa fa-comment-o"></i> <span class="commentCount">${t.commentCount}</span></span>
				</c:when>
				<c:otherwise>
					<a class="replyToggle">
						<i class="fa fa-reply"></i> <span class="commentCount">${t.commentCount}</span>
					</a>
				</c:otherwise>
			</c:choose>
			<c:if test="${!isGuest && t.userId == user.id}">
				<a class="delTweet"><i class="fa fa-trash"></i> Delete</a>
			</c:if>
		</div>

		<c:if test="${!isGuest}">
		<div class="replyDrawer w3-hide">
			<div class="commentList"></div>
			<c:if test="${isMember}">
				<div style="display:flex; gap:8px; margin-top:8px">
					<input class="commentInput" type="text" maxlength="500" placeholder="Write a reply...">
					<button class="commentBtn w3-button w3-theme w3-round">Reply</button>
				</div>
			</c:if>
		</div>
		</c:if>
	</div>
</c:forEach>
</c:otherwise>
</c:choose>

</c:otherwise>
</c:choose>
