package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import epaw.lab4.model.User;
import epaw.lab4.service.UserService;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

@MultipartConfig
@WebServlet("/Register")
public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = UserService.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("Register.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = new User();
		String passwordConfirm = request.getParameter("confirmPassword");
		try {
			BeanUtils.populate(user, request.getParameterMap());
			String picturePath = userService.saveProfilePicture(request.getPart("picture"), user.getUsername());
			user.setPicture(picturePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, String> errors = userService.register(user, passwordConfirm);
		if (errors.isEmpty()) {
			request.setAttribute("registered", true);
			User clean = new User();
			clean.setUsername(user.getUsername());
			request.setAttribute("user", clean);
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		} else {
			user.setPassword("");
			request.setAttribute("user", user);
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("Register.jsp").forward(request, response);
		}
	}
}
