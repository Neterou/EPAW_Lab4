<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="page page-narrow">
	<div class="brand-hero">🫧 BubbleNet</div>

	<div class="card">
		<h2>Log in</h2>

		<c:if test="${not empty registered}">
			<div class="w3-panel w3-pale-green w3-round w3-border w3-border-green">
				<p>Registration successful — please log in.</p>
			</div>
		</c:if>

		<form id="loginForm" action="Login" method="POST">
			<div class="w3-section">
				<label for="username" class="w3-text-theme">Username</label>
				<input type="text" class="w3-input w3-border w3-light-grey"
					id="username" name="username" required minlength="4" maxlength="30"
					value="${user.username}" title="Your username." />
			</div>
			<div class="w3-section">
				<label for="password" class="w3-text-theme">Password</label>
				<input type="password" class="w3-input w3-border w3-light-grey"
					id="password" name="password" required />
			</div>
			<button type="submit" class="w3-button w3-theme w3-round w3-section w3-block">Log in</button>
		</form>

		<p class="w3-small" style="margin-top:14px; color:var(--muted)">
			No account yet? <a class="menu w3-text-theme" href="Register">Create one</a>.
		</p>
		<p class="w3-small" style="color:var(--muted)">
			Demo: <strong>admin_demo / Admin123!</strong> &middot; <strong>user_demo / User123!</strong>
		</p>
	</div>
</div>

<script>
	App.Errors = {
		<c:forEach var="error" items="${errors}">
			"${error.key}": "${error.value}",
		</c:forEach>
	};
	App.initLoginValidation(App.Errors);
</script>
