package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.User;
import epaw.lab4.service.BubbleService;
import epaw.lab4.service.UserService;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Persists the changes from the edit-profile form. Mirrors the Register flow:
 * binds the request, optionally stores a new picture, validates through the
 * service, and on success refreshes the session user so the navbar/avatar
 * reflect the change. Returns the read-only Profile fragment when it succeeds,
 * or the edit form (with errors) otherwise.
 */
@MultipartConfig
@WebServlet("/UpdateProfile")
public class UpdateProfile extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = UserService.getInstance();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			request.getRequestDispatcher("Login.jsp").forward(request, response);
			return;
		}

		User current = (User) session.getAttribute("user");

		// Bind the editable fields (name, username) into a working copy.
		User edited = new User();
		try {
			BeanUtils.populate(edited, request.getParameterMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		edited.setId(current.getId());

		// A new upload replaces the picture; otherwise keep the existing one.
		String newPicture = null;
		try {
			newPicture = userService.saveProfilePicture(request.getPart("picture"), edited.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		edited.setPicture(newPicture != null ? newPicture : current.getPicture());

		Map<String, String> errors = userService.updateProfile(edited);

		if (errors.isEmpty()) {
			// Reflect the changes in the live session user.
			current.setName(edited.getName());
			current.setUsername(edited.getUsername());
			current.setPicture(edited.getPicture());

			if (current.getBubbleId() != null) {
				request.setAttribute("bubble", BubbleService.getInstance().getBubble(current.getBubbleId()));
			}
			request.getRequestDispatcher("Profile.jsp").forward(request, response);
		} else {
			// Keep non-editable display data so the form re-renders cleanly.
			edited.setStatus(current.getStatus());
			edited.setRole(current.getRole());
			edited.setBubbleId(current.getBubbleId());
			request.setAttribute("user", edited);
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("EditProfile").forward(request, response);
	}
}
