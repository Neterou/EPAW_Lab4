package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.User;
import epaw.lab4.service.BubbleService;

import java.io.IOException;

/**
 * Approves a pending join request. The service verifies that the logged-in
 * user actually owns the target bubble before applying the change.
 */
@WebServlet("/ApproveRequest")
public class ApproveRequest extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		User owner = (User) session.getAttribute("user");

		try {
			Integer userId = Integer.valueOf(request.getParameter("userId"));
			Integer bubbleId = Integer.valueOf(request.getParameter("bubbleId"));
			boolean ok = BubbleService.getInstance().approveRequest(owner.getId(), userId, bubbleId);
			if (!ok) response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
