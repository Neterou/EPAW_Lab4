package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.service.TweetService;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;

import java.io.IOException;

/**
 * Publishes a post inside a bubble. Only admins may broadcast posts
 * (Seminar 4, role-based compose model). The post type drives its
 * left-border accent colour in the feed.
 */
@WebServlet("/AddTweet")
public class AddTweet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		User user = (User) session.getAttribute("user");

		if (!user.isAdmin()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		String content = request.getParameter("content");
		Integer bubbleId = parse(request.getParameter("bubbleId"));
		if (bubbleId == null) bubbleId = user.getBubbleId();

		if (content != null && !content.trim().isEmpty() && bubbleId != null) {
			Tweet tweet = new Tweet();
			tweet.setUserId(user.getId());
			tweet.setBubbleId(bubbleId);
			tweet.setContent(content.trim());
			String type = request.getParameter("type");
			tweet.setType(type != null && !type.isEmpty() ? type : "STANDARD");
			TweetService.getInstance().add(tweet);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	private Integer parse(String s) {
		try {
			return (s == null || s.isEmpty()) ? null : Integer.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
