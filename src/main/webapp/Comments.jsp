<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
<c:when test="${empty comments}">
	<p style="color:var(--muted); font-size:13px; margin:4px 0">No replies yet. Be the first.</p>
</c:when>
<c:otherwise>
<c:forEach var="cm" items="${comments}">
	<div class="comment">
		<c:choose>
			<c:when test="${not empty cm.userPicture}">
				<img class="c-avatar" src="${cm.userPicture}" alt="">
			</c:when>
			<c:otherwise>
				<span class="c-avatar"><i class="fa fa-user"></i></span>
			</c:otherwise>
		</c:choose>
		<div>
			<span class="c-author">@${cm.username}</span>
			<span class="c-time">&middot; ${cm.createdAt}</span>
			<div>${cm.content}</div>
		</div>
	</div>
</c:forEach>
</c:otherwise>
</c:choose>
