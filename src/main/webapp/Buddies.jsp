<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Followed mode is a standalone page (wrapped + titled); discover mode is a
     bare list injected into the Discover › People tab. --%>
<c:if test="${mode == 'followed'}">
<div class="page page-narrow">
	<h2 class="w3-text-theme"><i class="fa fa-users"></i> Buddies</h2>
</c:if>

<c:choose>
<c:when test="${empty users}">
	<p style="color:var(--muted)">
		<c:choose>
			<c:when test="${mode == 'followed'}">You are not following anyone yet. Try Discover.</c:when>
			<c:otherwise>No more people to discover right now.</c:otherwise>
		</c:choose>
	</p>
</c:when>
<c:otherwise>
<c:forEach var="u" items="${users}">
	<div class="buddy" data-id="${u.id}">
		<c:choose>
			<c:when test="${not empty u.picture}">
				<img class="avatar" src="${u.picture}" alt="">
			</c:when>
			<c:otherwise>
				<span class="avatar"><i class="fa fa-user"></i></span>
			</c:otherwise>
		</c:choose>
		<div class="grow">
			<div class="uname"><a class="userLink" data-user="${u.id}" style="cursor:pointer; color:inherit; text-decoration:none">@${u.username}</a>
				<c:if test="${u.admin}"><span class="role-badge">ADMIN</span></c:if>
			</div>
			<div class="full">${u.name}</div>
		</div>
		<c:choose>
			<c:when test="${mode == 'followed'}">
				<button class="unfollowUser w3-button w3-light-grey w3-round"><i class="fa fa-user-times"></i> Unfollow</button>
			</c:when>
			<c:otherwise>
				<button class="followUser w3-button w3-theme w3-round"><i class="fa fa-user-plus"></i> Follow</button>
			</c:otherwise>
		</c:choose>
	</div>
</c:forEach>
</c:otherwise>
</c:choose>

<c:if test="${mode == 'followed'}">
</div>
</c:if>
