<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="page page-narrow">
	<div class="card w3-center">
		<c:choose>
			<c:when test="${not empty user.picture}">
				<img class="w3-circle" src="${user.picture}" alt="" style="width:96px;height:96px;object-fit:cover">
			</c:when>
			<c:otherwise>
				<div class="avatar" style="width:96px;height:96px;margin:0 auto;font-size:32px;border-radius:50%;background:var(--teal-500);color:#fff;display:flex;align-items:center;justify-content:center">
					<i class="fa fa-user"></i>
				</div>
			</c:otherwise>
		</c:choose>

		<h2 style="margin-bottom:0">${user.name}</h2>
		<p style="color:var(--muted);margin-top:2px">@${user.username}
			<c:if test="${user.admin}"><span class="role-badge">ADMIN</span></c:if>
		</p>

		<a class="menu w3-button w3-theme w3-round w3-small" href="EditProfile">
			<i class="fa fa-pencil"></i> Edit profile
		</a>

		<div class="w3-left-align" style="margin-top:16px">
			<p><i class="fa fa-envelope fa-fw w3-text-theme"></i> ${user.email}</p>
			<p><i class="fa fa-map-marker fa-fw w3-text-theme"></i> ${user.city}, ${user.country} (${user.zip})</p>
			<p>
				<i class="fa fa-circle fa-fw" style="color:var(--online)"></i>
				Status: <strong>${user.status}</strong>
			</p>
			<c:choose>
				<c:when test="${not empty bubble}">
					<p>
						<i class="fa fa-users fa-fw w3-text-theme"></i>
						Bubble: <strong>${bubble.name}</strong>
						<span class="cat-chip ${bubble.category}">${bubble.category}</span>
					</p>
				</c:when>
				<c:otherwise>
					<p><i class="fa fa-users fa-fw w3-text-theme"></i> No bubble assigned yet.</p>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
