package com.userwebapp.servlet;

import com.userwebapp.dao.UserDAO;
import com.userwebapp.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

// @WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Ensure user is logged in
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 2) Forward to JSP with user in request
        User user = (User) session.getAttribute("user");
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Ensure user is logged in
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 2) Read updated fields
        User sessionUser = (User) session.getAttribute("user");
        String fullName    = req.getParameter("fullName");
        String email       = req.getParameter("email");
        String newPassword = req.getParameter("newPassword");

        // 3) Validate inputs
        String error = null;
        if (fullName == null || fullName.isBlank()) {
            error = "Full name is required";
        } else if (email == null || !email.contains("@")) {
            error = "A valid email is required";
        }

        if (error != null) {
            req.setAttribute("error", error);
            sessionUser.setFullName(fullName);
            sessionUser.setEmail(email);
            req.setAttribute("user", sessionUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp")
                    .forward(req, resp);
            return;
        }

        // 4) Update in DB
        try {
            boolean updated = userDao.updateProfile(
                    new User(
                            sessionUser.getId(),
                            sessionUser.getUsername(),
                            null,
                            fullName,
                            email,
                            sessionUser.getRole()
                    ),
                    newPassword
            );
            if (updated) {
                // 5) Refresh session user
                sessionUser.setFullName(fullName);
                sessionUser.setEmail(email);
                session.setAttribute("user", sessionUser);
            }
        } catch (SQLException e) {
            throw new ServletException("Profile update failed", e);
        }

        // 6) Redirect to avoid form resubmission
        resp.sendRedirect(req.getContextPath() + "/profile?updated=true");
    }
}