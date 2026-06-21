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
import java.util.List;

/** Lists the replies of a post as an HTML fragment. */
@WebServlet("/Comments")
public class Comments extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			request.getRequestDispatcher("Login.jsp").forward(request, response);
			return;
		}

		try {
			Integer tweetId = Integer.valueOf(request.getParameter("tweetId"));
			List<Comment> comments = CommentService.getInstance().getComments(tweetId);
			request.setAttribute("comments", comments);
			request.setAttribute("tweetId", tweetId);
			request.setAttribute("user", session.getAttribute("user"));
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		request.getRequestDispatcher("Comments.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
