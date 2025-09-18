package com.userwebapp.servlet;

import com.userwebapp.dao.UserDAO;
import com.userwebapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Show the form
        req.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Read form values
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String email    = req.getParameter("email");

        // 2) Simple validation
        String error = null;
        if (username == null || username.isBlank()) {
            error = "Username is required";
        } else if (password == null || password.length() < 8) {
            error = "Password must be at least 8 characters";
        } else if (fullName == null || fullName.isBlank()) {
            error = "Full name is required";
        } else if (email == null || !email.contains("@")) {
            error = "A valid email is required";
        }

        // 3) On error, re-display form
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("username", username);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(req, resp);
            return;
        }

        // 4) Register user (role defaults to 'USER' in DB)
        try {
            User user = new User(0, username, null, fullName, email, "USER");
            userDao.register(user, password);
            // 5) Redirect to login with success flag
            resp.sendRedirect(req.getContextPath() + "/login?registered=true");
        } catch (SQLException e) {
            throw new ServletException("Registration failed", e);
        }
    }
}