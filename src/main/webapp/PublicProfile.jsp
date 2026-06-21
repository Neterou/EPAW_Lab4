<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="page page-narrow">

<c:choose>
<c:when test="${empty profileUser}">
	<div class="card w3-center">
		<p style="color:var(--muted)">This user could not be found.</p>
		<a class="menu w3-button w3-theme w3-round w3-small" href="Content"><i class="fa fa-map-o"></i> Back to map</a>
	</div>
</c:when>
<c:otherwise>

	<!-- Public information -->
	<div class="card w3-center">
		<c:choose>
			<c:when test="${not empty profileUser.picture}">
				<img class="w3-circle" src="${profileUser.picture}" alt="" style="width:96px;height:96px;object-fit:cover">
			</c:when>
			<c:otherwise>
				<div class="avatar" style="width:96px;height:96px;margin:0 auto;font-size:32px;border-radius:50%;background:var(--teal-500);color:#fff;display:flex;align-items:center;justify-content:center">
					<i class="fa fa-user"></i>
				</div>
			</c:otherwise>
		</c:choose>

		<h2 style="margin-bottom:0">${profileUser.name}</h2>
		<p style="color:var(--muted);margin-top:2px">@${profileUser.username}
			<c:if test="${profileUser.admin}"><span class="role-badge">ADMIN</span></c:if>
		</p>

		<div class="w3-left-align" style="margin-top:16px">
			<p>
				<i class="fa fa-circle fa-fw" style="color:var(--online)"></i>
				Status: <strong>${profileUser.status}</strong>
			</p>
			<c:if test="${not empty bubble}">
				<p>
					<i class="fa fa-users fa-fw w3-text-theme"></i>
					Bubble: <strong>${bubble.name}</strong>
					<span class="cat-chip ${bubble.category}">${bubble.category}</span>
				</p>
			</c:if>
		</div>
	</div>

	<!-- Published posts -->
	<h3 class="w3-text-theme" style="margin-top:24px"><i class="fa fa-comments-o"></i> Posts</h3>
	<c:choose>
	<c:when test="${empty posts}">
		<p style="color:var(--muted)">@${profileUser.username} hasn't published any posts yet.</p>
	</c:when>
	<c:otherwise>
	<c:forEach var="t" items="${posts}">
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
				<span class="author">@${t.username}</span>
				<c:if test="${t.type != 'STANDARD'}">
					<span class="badge ${t.type}">${t.type}</span>
				</c:if>
				<span class="time" style="margin-left:auto">${t.createdAt}</span>
			</div>

			<div class="body">${t.content}</div>

			<div class="actions">
				<span class="guest-stat"><i class="fa fa-heart"></i> <span class="likeCount">${t.likeCount}</span></span>
				<span class="guest-stat"><i class="fa fa-comment-o"></i> <span class="commentCount">${t.commentCount}</span></span>
			</div>
		</div>
	</c:forEach>
	</c:otherwise>
	</c:choose>

</c:otherwise>
</c:choose>

</div>
