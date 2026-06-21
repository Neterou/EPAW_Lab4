<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- BubbleNet home: split view — interactive map (left) + live feed (right) -->
<div class="app-grid">
	<div id="map"></div>
	<div id="feedPanel">
		<div class="w3-center w3-padding-large" style="color:var(--muted)">
			<i class="fa fa-map-marker fa-2x"></i>
			<p>Select a bubble on the map to see its feed.</p>
		</div>
	</div>
</div>

<script>
	// Initialise the Leaflet map once this fragment is in the DOM.
	App.initMap();
</script>
