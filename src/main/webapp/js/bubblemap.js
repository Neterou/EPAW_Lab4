window.App = window.App || {};

/* Category -> marker colour + emoji (Seminar 4 §2.2 / §4.2). */
App.categoryStyle = {
  sports:    { color: "#1D9E75", emoji: "🏃" }, // 🏃
  community: { color: "#7F77DD", emoji: "👥" }, // 👥
  public:    { color: "#D85A30", emoji: "🚔" }, // 🚔
  transport: { color: "#185FA5", emoji: "🚇" }, // 🚇
  special:   { color: "#BA7517", emoji: "🐾" }  // 🐾
};

/* Load a bubble's feed into the right-hand panel. */
App.loadFeed = function (id) {
  $("#feedPanel").load("Feed?bubbleId=" + id);
};

/* Popup HTML for a marker, with a membership-aware Join/Leave button. */
App.bubblePopup = function (b, isGuest) {
  var btn;
  if (isGuest) {
    btn = '<a class="menu w3-button w3-theme w3-round w3-small" href="Login" style="margin-top:6px">' +
          '<i class="fa fa-sign-in"></i> Log in to join</a>';
  } else if (b.membership === "APPROVED") {
    btn = '<button class="popupLeave w3-button w3-light-grey w3-round w3-small" data-bubble="' + b.id + '">' +
          '<i class="fa fa-check"></i> Member</button>';
  } else if (b.membership === "PENDING") {
    btn = '<button class="popupLeave w3-button w3-pale-yellow w3-round w3-small" data-bubble="' + b.id + '">' +
          '<i class="fa fa-clock-o"></i> Requested</button>';
  } else {
    btn = '<button class="popupJoin w3-button w3-theme w3-round w3-small" data-bubble="' + b.id + '">' +
          (b.open ? '<i class="fa fa-plus"></i> Join' : '<i class="fa fa-paper-plane"></i> Request') + '</button>';
  }
  return '<div class="bubble-popup"><strong>' + b.name + '</strong><br>' +
         '<span class="muted">' + b.members + ' members &middot; ' + (b.open ? 'Open' : 'Closed') + '</span><br>' + btn +
         '</div>';
};

/* Build the Leaflet map with custom SVG-style divIcon markers. */
App.initMap = function () {
  if (typeof L === "undefined") return;            // Leaflet not loaded
  if (App._map) { App._map.remove(); App._map = null; }

  var map = L.map("map", { zoomControl: true }).setView([41.39, 2.17], 13);
  App._map = map;

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution: "&copy; OpenStreetMap contributors"
  }).addTo(map);

  $.getJSON("Bubbles", function (data) {
    var bounds = [];
    var isGuest = (data.userBubbleId === null && !data.loggedIn);
    (data.bubbles || []).forEach(function (b) {
      var st = App.categoryStyle[b.category] || App.categoryStyle.community;
      // radius scales with sqrt(member count) so size reflects community size
      var size = Math.round(30 + Math.sqrt(b.members) * 8);
      var mine = (b.id === data.userBubbleId);
      var html = '<div class="bubble-marker' + (mine ? " mine" : "") + '" style="width:' +
        size + "px;height:" + size + "px;background:" + st.color + '">' + st.emoji + "</div>";

      var icon = L.divIcon({
        html: html, className: "",
        iconSize: [size, size], iconAnchor: [size / 2, size / 2]
      });

      var marker = L.marker([b.lat, b.lng], { icon: icon }).addTo(map);
      marker.bindPopup(App.bubblePopup(b, isGuest));
      marker.on("click", function () { App.loadFeed(b.id); });
      bounds.push([b.lat, b.lng]);
    });

    if (bounds.length) map.fitBounds(bounds, { padding: [50, 50], maxZoom: 14 });

    // Open the user's own bubble first, falling back to the first bubble.
    var initial = data.userBubbleId || (data.bubbles[0] && data.bubbles[0].id);
    if (initial) App.loadFeed(initial);

    // The map container was just injected into the DOM; force a redraw.
    setTimeout(function () { map.invalidateSize(); }, 200);
  });
};
