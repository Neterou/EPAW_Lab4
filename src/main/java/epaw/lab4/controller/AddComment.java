package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.Comment;
import epaw.lab4.model.User;
import epaw.lab4.service.CommentService;

import java.io.IOException;

/**
 * Adds a reply to a post. Any logged-in member can reply (the engagement
 * path for non-admin users in the broadcast-channel model).
 */
@WebServlet("/AddComment")
public class AddComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		User user = (User) session.getAttribute("user");

		String content = request.getParameter("content");
		try {
			Integer tweetId = Integer.valueOf(request.getParameter("tweetId"));
			if (content != null && !content.trim().isEmpty()) {
				Comment comment = new Comment();
				comment.setTweetId(tweetId);
				comment.setUserId(user.getId());
				comment.setContent(content.trim());
				CommentService.getInstance().add(comment);
			}
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
