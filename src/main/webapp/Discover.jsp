<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="page page-narrow">
	<h2 class="w3-text-theme"><i class="fa fa-search"></i> Discover</h2>

	<div class="discover-tabs">
		<button class="discoverTab active w3-button w3-round" data-target="NotFollowed">
			<i class="fa fa-users"></i> People
		</button>
		<button class="discoverTab w3-button w3-round" data-target="BubbleList">
			<i class="fa fa-map-o"></i> Bubbles
		</button>
	</div>

	<div id="discoverBody"></div>
</div>

<script>
	// Default to the People tab when the Discover page loads.
	$("#discoverBody").load("NotFollowed");
</script>
