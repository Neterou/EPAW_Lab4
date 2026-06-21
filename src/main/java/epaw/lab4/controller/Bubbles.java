package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.Bubble;
import epaw.lab4.model.User;
import epaw.lab4.service.BubbleService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns the full set of bubbles as JSON so the Leaflet map can render
 * category-coloured markers. Also reports the viewer's own bubble so the
 * map can highlight it with the pulsing ring.
 */
@WebServlet("/Bubbles")
public class Bubbles extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer userBubbleId = null;
		Integer userId = null;
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("user") != null) {
			User u = (User) session.getAttribute("user");
			userBubbleId = u.getBubbleId();
			userId = u.getId();
		}

		// Annotate with the viewer's membership status when logged in.
		List<Bubble> bubbles = userId != null
				? BubbleService.getInstance().getBubblesForUser(userId)
				: new ArrayList<>(BubbleService.getInstance().getAllBubbles());

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		StringBuilder json = new StringBuilder();
		json.append("{\"userBubbleId\":").append(userBubbleId == null ? "null" : userBubbleId);
		json.append(",\"loggedIn\":").append(userId != null);
		json.append(",\"bubbles\":[");
		for (int i = 0; i < bubbles.size(); i++) {
			Bubble b = bubbles.get(i);
			if (i > 0) json.append(',');
			json.append('{')
				.append("\"id\":").append(b.getId()).append(',')
				.append("\"name\":\"").append(esc(b.getName())).append("\",")
				.append("\"category\":\"").append(esc(b.getCategory())).append("\",")
				.append("\"city\":\"").append(esc(b.getCity())).append("\",")
				.append("\"lat\":").append(b.getLat()).append(',')
				.append("\"lng\":").append(b.getLng()).append(',')
				.append("\"open\":").append(b.isOpen()).append(',')
				.append("\"members\":").append(b.getMemberCount()).append(',')
				.append("\"membership\":").append(b.getMembership() == null ? "null" : "\"" + b.getMembership() + "\"")
				.append('}');
		}
		json.append("]}");

		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
	}

	private static String esc(String s) {
		if (s == null) return "";
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
