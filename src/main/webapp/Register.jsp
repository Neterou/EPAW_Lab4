<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="page">
	<div class="brand-hero">🫧 BubbleNet</div>

	<div class="card">
		<h2>Create your account</h2>
		<p style="color:var(--muted); margin-top:-6px">
			You will be assigned to the Bubble that matches your location.
		</p>

		<form id="registerForm" action="Register" method="POST" enctype="multipart/form-data">

			<div class="w3-section">
				<label for="name" class="w3-text-theme">Full Name *</label>
				<input class="w3-input w3-border w3-light-grey" type="text" id="name" name="name"
					required minlength="2" maxlength="60" pattern="[A-Za-zÀ-ÿ\s\-']{2,60}"
					title="2–60 letters, hyphens, or apostrophes." value="${user.name}" />
				<c:if test="${not empty errors['name']}"><span class="field-error">${errors['name']}</span></c:if>
			</div>

			<div class="w3-section">
				<label for="email" class="w3-text-theme">Email *</label>
				<input class="w3-input w3-border w3-light-grey" type="email" id="email" name="email"
					required value="${user.email}" />
				<c:if test="${not empty errors['email']}"><span class="field-error">${errors['email']}</span></c:if>
			</div>

			<div class="w3-section">
				<label class="w3-text-theme">Phone (optional)</label>
				<div class="w3-row">
					<div class="w3-col" style="width:110px; margin-right:8px">
						<select class="w3-select w3-border w3-light-grey" name="phoneCountry">
							<option value="+34" <c:if test="${user.phoneCountry == '+34'}">selected</c:if>>+34 (ES)</option>
							<option value="+1"  <c:if test="${user.phoneCountry == '+1'}">selected</c:if>>+1 (US)</option>
							<option value="+44" <c:if test="${user.phoneCountry == '+44'}">selected</c:if>>+44 (UK)</option>
							<option value="+33" <c:if test="${user.phoneCountry == '+33'}">selected</c:if>>+33 (FR)</option>
							<option value="+49" <c:if test="${user.phoneCountry == '+49'}">selected</c:if>>+49 (DE)</option>
						</select>
					</div>
					<div class="w3-rest">
						<input class="w3-input w3-border w3-light-grey" type="tel" id="phone" name="phone"
							pattern="\d{7,15}" title="7–15 digits, no spaces." value="${user.phone}" />
					</div>
				</div>
				<c:if test="${not empty errors['phone']}"><span class="field-error">${errors['phone']}</span></c:if>
			</div>

			<div class="w3-section">
				<label for="dni" class="w3-text-theme">DNI *</label>
				<input class="w3-input w3-border w3-light-grey" type="text" id="dni" name="dni"
					required pattern="\d{8}[A-Za-z]" maxlength="9"
					title="8 digits followed by a letter (e.g. 12345678A)." value="${user.dni}" />
				<c:if test="${not empty errors['dni']}"><span class="field-error">${errors['dni']}</span></c:if>
			</div>

			<div class="w3-row" style="gap:0">
				<div class="w3-col m4 w3-section" style="padding-right:6px">
					<label for="zip" class="w3-text-theme">ZIP *</label>
					<input class="w3-input w3-border w3-light-grey" type="text" id="zip" name="zip"
						required pattern="\d{5}" maxlength="5" title="Exactly 5 digits." value="${user.zip}" />
					<c:if test="${not empty errors['zip']}"><span class="field-error">${errors['zip']}</span></c:if>
				</div>
				<div class="w3-col m5 w3-section" style="padding:0 6px">
					<label for="city" class="w3-text-theme">City *</label>
					<input class="w3-input w3-border w3-light-grey" type="text" id="city" name="city"
						required maxlength="100" value="${user.city}" />
					<c:if test="${not empty errors['city']}"><span class="field-error">${errors['city']}</span></c:if>
				</div>
				<div class="w3-col m3 w3-section" style="padding-left:6px">
					<label for="country" class="w3-text-theme">Country *</label>
					<input class="w3-input w3-border w3-light-grey" type="text" id="country" name="country"
						required pattern="[A-Za-z]{2}" maxlength="2" title="2-letter ISO code." value="${user.country}" />
					<c:if test="${not empty errors['country']}"><span class="field-error">${errors['country']}</span></c:if>
				</div>
			</div>

			<div class="w3-section">
				<label for="gender" class="w3-text-theme">Gender</label>
				<select class="w3-select w3-border w3-light-grey" id="gender" name="gender">
					<option value="">— select —</option>
					<option value="Male"   <c:if test="${user.gender == 'Male'}">selected</c:if>>Male</option>
					<option value="Female" <c:if test="${user.gender == 'Female'}">selected</c:if>>Female</option>
					<option value="Other"  <c:if test="${user.gender == 'Other'}">selected</c:if>>Other</option>
				</select>
			</div>

			<div class="w3-section">
				<label for="username" class="w3-text-theme">Username *</label>
				<input class="w3-input w3-border w3-light-grey" type="text" id="username" name="username"
					required minlength="4" maxlength="30" pattern="[A-Za-z0-9_]{4,30}"
					title="4–30 characters: letters, digits, underscores." value="${user.username}" />
				<c:if test="${not empty errors['username']}"><span class="field-error">${errors['username']}</span></c:if>
			</div>

			<div class="w3-section">
				<label for="password" class="w3-text-theme">Password *</label>
				<input class="w3-input w3-border w3-light-grey" type="password" id="password" name="password"
					required pattern="(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+]).{8,}"
					title="Min 8 chars with uppercase, digit, and special character." />
				<c:if test="${not empty errors['password']}"><span class="field-error">${errors['password']}</span></c:if>
			</div>

			<div class="w3-section">
				<label for="confirmPassword" class="w3-text-theme">Confirm Password *</label>
				<input class="w3-input w3-border w3-light-grey" type="password" id="confirmPassword"
					name="confirmPassword" required title="Must match the password above." />
				<c:if test="${not empty errors['confirmPassword']}"><span class="field-error">${errors['confirmPassword']}</span></c:if>
			</div>

			<div class="w3-section">
				<label for="picture" class="w3-text-theme">Profile Picture</label>
				<input class="w3-input w3-border w3-light-grey" type="file" id="picture" name="picture" accept="image/*" />
			</div>

			<button type="submit" class="w3-button w3-theme w3-round w3-section w3-block">Register</button>
		</form>

		<p class="w3-small" style="margin-top:14px; color:var(--muted)">
			Already have an account? <a class="menu w3-text-theme" href="Login">Log in</a>.
		</p>
	</div>
</div>

<script>
	App.Errors = {
		<c:forEach var="error" items="${errors}">
			"${error.key}": "${error.value}",
		</c:forEach>
	};
	App.initRegisterValidation(App.Errors);
</script>
