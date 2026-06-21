package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.BubbleService;
import epaw.lab4.service.TweetService;
import epaw.lab4.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Public personal page of a registered user: shows their public information
 * and the posts they have published. Open to everyone (guests included), so it
 * never exposes private fields such as email, phone or DNI. The profile owner
 * is published under {@code profileUser} to avoid clashing with the viewer's
 * own session {@code user}.
 */
@WebServlet("/PublicProfile")
public class PublicProfile extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer userId = parse(request.getParameter("userId"));
		if (userId != null) {
			Optional<User> profile = UserRepository.getInstance().findById(userId);
			if (profile.isPresent()) {
				User profileUser = profile.get();
				request.setAttribute("profileUser", profileUser);

				if (profileUser.getBubbleId() != null) {
					request.setAttribute("bubble", BubbleService.getInstance().getBubble(profileUser.getBubbleId()));
				}

				List<Tweet> posts = TweetService.getInstance().getUserPosts(userId, 0, 50);
				request.setAttribute("posts", posts);
			}
		}
		request.getRequestDispatcher("PublicProfile.jsp").forward(request, response);
	}

	@Override
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
