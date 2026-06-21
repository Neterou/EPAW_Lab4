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
import java.util.List;

/** Lists every bubble with the current user's membership status (Discover › Bubbles). */
@WebServlet("/BubbleList")
public class BubbleList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			request.getRequestDispatcher("Login.jsp").forward(request, response);
			return;
		}
		User user = (User) session.getAttribute("user");
		List<Bubble> bubbles = BubbleService.getInstance().getBubblesForUser(user.getId());
		request.setAttribute("bubbles", bubbles);
		request.getRequestDispatcher("BubbleList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
