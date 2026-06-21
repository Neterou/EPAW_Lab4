package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.Bubble;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.BubbleService;
import epaw.lab4.service.TweetService;

import java.io.IOException;
import java.util.List;

/**
 * Renders a bubble's feed: the bubble header plus its posts. The selected
 * bubble comes from the {@code bubbleId} parameter, defaulting to the
 * user's own bubble.
 */
@WebServlet("/Feed")
public class Feed extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		User user = null;
		boolean isGuest = true;

		if (session != null && session.getAttribute("user") != null) {
			user = (User) session.getAttribute("user");
			isGuest = false;
		}

		Integer bubbleId = parse(request.getParameter("bubbleId"));
		if (bubbleId == null && user != null) bubbleId = user.getBubbleId();

		if (bubbleId != null) {
			BubbleService bubbleService = BubbleService.getInstance();
			Bubble bubble = bubbleService.getBubble(bubbleId);
			request.setAttribute("bubble", bubble);

			// Guest users see posts but with userId=0 (no personal like state)
			int viewerId = (user != null) ? user.getId() : 0;
			List<Tweet> tweets = TweetService.getInstance().getBubbleFeed(bubbleId, viewerId, 0, 50);
			request.setAttribute("tweets", tweets);

			if (!isGuest) {
				String membership = bubbleService.membershipStatus(user.getId(), bubbleId);
				boolean isMember = "APPROVED".equals(membership);
				request.setAttribute("membership", membership);
				request.setAttribute("isMember", isMember);
				request.setAttribute("canPost", user.isAdmin() && isMember);
			} else {
				request.setAttribute("isMember", false);
				request.setAttribute("canPost", false);
			}
		}
		request.setAttribute("isGuest", isGuest);
		request.setAttribute("user", user);
		request.getRequestDispatcher("Feed.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private Integer parse(String s) {
		try {
			return (s == null || s.isEmpty()) ? null : Integer.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
