package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;

import java.io.IOException;
import java.util.List;

/**
 * Personalized timeline: the posts published by the people the current user
 * follows, newest first. Registered users only.
 */
@WebServlet("/Timeline")
public class Timeline extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			request.getRequestDispatcher("Login.jsp").forward(request, response);
			return;
		}

		User user = (User) session.getAttribute("user");
		List<Tweet> tweets = TweetService.getInstance().getFollowedTimeline(user.getId(), 0, 50);
		request.setAttribute("tweets", tweets);
		request.setAttribute("user", user);
		request.getRequestDispatcher("Timeline.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
