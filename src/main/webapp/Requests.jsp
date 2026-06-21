<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="page page-narrow">
	<h2 class="w3-text-theme"><i class="fa fa-inbox"></i> Join requests</h2>
	<p style="color:var(--muted); margin-top:-6px">People asking to join the bubbles you own.</p>

	<c:choose>
	<c:when test="${empty requests}">
		<div class="w3-center w3-padding-large" style="color:var(--muted)">
			<i class="fa fa-check-circle-o fa-2x"></i>
			<p>No pending requests right now.</p>
		</div>
	</c:when>
	<c:otherwise>
	<c:forEach var="r" items="${requests}">
		<div class="buddy reqRow" data-user="${r.userId}" data-bubble="${r.bubbleId}">
			<c:choose>
				<c:when test="${not empty r.picture}">
					<img class="avatar" src="${r.picture}" alt="">
				</c:when>
				<c:otherwise>
					<span class="avatar"><i class="fa fa-user"></i></span>
				</c:otherwise>
			</c:choose>
			<div class="grow">
				<div class="uname">
					<a class="userLink" data-user="${r.userId}" style="cursor:pointer; color:inherit; text-decoration:none">@${r.username}</a>
				</div>
				<div class="full">
					${r.name} · wants to join
					<strong>${r.bubbleName}</strong>
					<span class="cat-chip ${r.bubbleCategory}">${r.bubbleCategory}</span>
				</div>
			</div>
			<button class="approveReq w3-button w3-theme w3-round"><i class="fa fa-check"></i> Approve</button>
			<button class="rejectReq w3-button w3-light-grey w3-round"><i class="fa fa-times"></i> Reject</button>
		</div>
	</c:forEach>
	</c:otherwise>
	</c:choose>
</div>
