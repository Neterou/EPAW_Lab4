<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="page page-narrow">
	<h2 class="w3-text-theme"><i class="fa fa-newspaper-o"></i> Your timeline</h2>
	<p style="color:var(--muted); margin-top:-6px">Posts from the people you follow.</p>

	<c:choose>
	<c:when test="${empty tweets}">
		<div class="w3-center w3-padding-large" style="color:var(--muted)">
			<i class="fa fa-users fa-2x"></i>
			<p>No posts yet. Follow people from <a class="menu w3-text-theme" href="Discover">Discover</a> to fill your timeline.</p>
		</div>
	</c:when>
	<c:otherwise>
	<c:forEach var="t" items="${tweets}">
		<div class="post type-${t.type}" data-id="${t.id}">
			<div style="display:flex; align-items:center; gap:8px">
				<a class="userLink" data-user="${t.userId}" style="display:flex; align-items:center; gap:8px; cursor:pointer; color:inherit; text-decoration:none">
					<c:choose>
						<c:when test="${not empty t.userPicture}">
							<img class="avatar" src="${t.userPicture}" alt="">
						</c:when>
						<c:otherwise>
							<span class="avatar"><i class="fa fa-user"></i></span>
						</c:otherwise>
					</c:choose>
					<span class="author">@${t.username}</span>
				</a>
				<c:if test="${t.type != 'STANDARD'}">
					<span class="badge ${t.type}">${t.type}</span>
				</c:if>
				<span class="time" style="margin-left:auto">${t.createdAt}</span>
			</div>

			<div class="body">${t.content}</div>

			<div class="actions">
				<a class="likeBtn ${t.likedByMe ? 'liked' : ''}">
					<i class="fa fa-heart"></i> <span class="likeCount">${t.likeCount}</span>
				</a>
				<a class="replyToggle">
					<i class="fa fa-reply"></i> <span class="commentCount">${t.commentCount}</span>
				</a>
			</div>

			<div class="replyDrawer w3-hide">
				<div class="commentList"></div>
				<div style="display:flex; gap:8px; margin-top:8px">
					<input class="commentInput" type="text" maxlength="500" placeholder="Write a reply...">
					<button class="commentBtn w3-button w3-theme w3-round">Reply</button>
				</div>
			</div>
		</div>
	</c:forEach>
	</c:otherwise>
	</c:choose>
</div>
