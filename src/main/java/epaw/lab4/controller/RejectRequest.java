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
 * Rejects a pending join request (removes the PENDING membership row). Only the
 * owner of the target bubble may do this, enforced by the service.
 */
@WebServlet("/RejectRequest")
public class RejectRequest extends HttpServlet {

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
			boolean ok = BubbleService.getInstance().rejectRequest(owner.getId(), userId, bubbleId);
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
