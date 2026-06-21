<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="page page-narrow">
	<div class="card">
		<h2 class="w3-text-theme"><i class="fa fa-pencil"></i> Edit profile</h2>
		<p style="color:var(--muted); margin-top:-6px">
			Update your public details. Leave the picture empty to keep your current one.
		</p>

		<div class="w3-center w3-section">
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
		</div>

		<form id="editProfileForm" action="UpdateProfile" method="POST" enctype="multipart/form-data">

			<div class="w3-section">
				<label for="name" class="w3-text-theme">Full Name *</label>
				<input class="w3-input w3-border w3-light-grey" type="text" id="name" name="name"
					required minlength="2" maxlength="60" pattern="[A-Za-zÀ-ÿ\s\-']{2,60}"
					title="2–60 letters, hyphens, or apostrophes." value="${user.name}" />
				<c:if test="${not empty errors['name']}"><span class="field-error">${errors['name']}</span></c:if>
			</div>

			<div class="w3-section">
				<label for="username" class="w3-text-theme">Username *</label>
				<input class="w3-input w3-border w3-light-grey" type="text" id="username" name="username"
					required minlength="4" maxlength="30" pattern="[A-Za-z0-9_]{4,30}"
					title="4–30 characters: letters, digits, underscores." value="${user.username}" />
				<c:if test="${not empty errors['username']}"><span class="field-error">${errors['username']}</span></c:if>
			</div>

			<div class="w3-section">
				<label for="picture" class="w3-text-theme">Profile Picture</label>
				<input class="w3-input w3-border w3-light-grey" type="file" id="picture" name="picture" accept="image/*" />
			</div>

			<div style="display:flex; gap:8px; margin-top:14px">
				<button type="submit" class="w3-button w3-theme w3-round w3-block" style="flex:1">
					<i class="fa fa-check"></i> Save changes
				</button>
				<a class="menu w3-button w3-light-grey w3-round" href="Profile">Cancel</a>
			</div>
		</form>
	</div>
</div>
