package com.userwebapp.servlet;

import com.userwebapp.dao.UserDAO;
import com.userwebapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Show login form
        req.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Read credentials
        String username = safeTrim(req.getParameter("username"));
        String password = req.getParameter("password");

        // 2) Basic validation
        String error = null;
        if (username.isEmpty() || password == null || password.isBlank()) {
            error = "Username and password are required";
        } else {
            try {
                Optional<User> opt = userDao.authenticate(username, password);
                if (opt.isPresent()) {
                    // 3) Success → store in session and redirect
                    HttpSession session = req.getSession();
                    session.setAttribute("user", opt.get());
                    resp.sendRedirect(req.getContextPath() + "/profile");
                    return;
                } else {
                    error = "Invalid username or password";
                }
            } catch (SQLException e) {
                throw new ServletException("Login error", e);
            }
        }

        // 4) On error → re-show form with message
        req.setAttribute("error", error);
        req.setAttribute("username", username);
        req.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(req, resp);
    }

    private String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}