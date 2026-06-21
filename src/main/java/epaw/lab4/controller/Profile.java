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

@WebServlet("/Profile")
public class Profile extends HttpServlet {

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
		if (user.getBubbleId() != null) {
			request.setAttribute("bubble", BubbleService.getInstance().getBubble(user.getBubbleId()));
		}
		request.getRequestDispatcher("Profile.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
