package com.userwebapp.filter;

import com.userwebapp.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) rq;
        User user = (User) req.getSession().getAttribute("user");
        String uri = req.getRequestURI();

        if (uri.contains("/admin/") && !"ADMIN".equals(user.getRole())) {
            req.getRequestDispatcher("/WEB-INF/views/error.jsp")
                    .forward(req, rs);
        } else {
            chain.doFilter(rq, rs);
        }
    }
}
