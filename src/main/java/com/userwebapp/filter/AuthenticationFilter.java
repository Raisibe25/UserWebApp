package com.userwebapp.filter;

import jakarta.servlet.http.*;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { /* … */ }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();
        HttpSession session = request.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        boolean isLoginRequest = path.endsWith("login") || path.endsWith("register");
        boolean isStaticResource = path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images");

        if (loggedIn || isLoginRequest || isStaticResource) {
            // Allow access
            chain.doFilter(request, response);
        } else {
            // Block access and redirect to login
            response.sendRedirect("login");
        }
    }

    @Override
    public void destroy() { /* … */ }
}