<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${empty bubbles}">
	<p style="color:var(--muted)">No bubbles available.</p>
</c:when>
<c:otherwise>
<c:forEach var="b" items="${bubbles}">
	<div class="bubbleRow" data-id="${b.id}">
		<span class="cat-dot ${b.category}"></span>
		<div class="grow">
			<div class="uname">${b.name}
				<c:if test="${b.home}"><span class="home-tag">home</span></c:if>
			</div>
			<div class="full">
				<span class="cat-chip ${b.category}">${b.category}</span>
				<i class="fa fa-map-marker"></i> ${b.city}
				&middot; <i class="fa fa-users"></i> ${b.memberCount}
				<c:choose>
					<c:when test="${b.open}">&middot; <i class="fa fa-globe"></i> Open</c:when>
					<c:otherwise>&middot; <i class="fa fa-lock"></i> Closed</c:otherwise>
				</c:choose>
			</div>
		</div>
		<c:choose>
			<c:when test="${b.member}">
				<button class="leaveBubble w3-button w3-light-grey w3-round"><i class="fa fa-check"></i> Member</button>
			</c:when>
			<c:when test="${b.pending}">
				<button class="leaveBubble w3-button w3-pale-yellow w3-round"><i class="fa fa-clock-o"></i> Requested</button>
			</c:when>
			<c:when test="${b.open}">
				<button class="joinBubble w3-button w3-theme w3-round"><i class="fa fa-plus"></i> Join</button>
			</c:when>
			<c:otherwise>
				<button class="joinBubble w3-button w3-theme w3-round"><i class="fa fa-paper-plane"></i> Request</button>
			</c:otherwise>
		</c:choose>
	</div>
</c:forEach>
</c:otherwise>
</c:choose>
