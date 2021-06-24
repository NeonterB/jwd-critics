package com.epam.jwd_critics.controller.filter;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Role;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class PageAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) resp;
        HttpSession session = httpRequest.getSession();

        String page = (String) req.getAttribute(Attribute.PREVIOUS_PAGE.getName());
        if (page == null) {
            page = ServletDestination.MAIN.getPath();
        }
        UserDTO user = (UserDTO) session.getAttribute(Attribute.USER.getName());
        Role userRole = Role.GUEST;
        if (user != null) {
            userRole = user.getRole();
        }
        String uri = httpRequest.getRequestURI();
        try {
            if (uri.contains(ServletDestination.ADMIN_URL) && (userRole != Role.ADMIN)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + page);
                return;
            } else if (uri.contains(ServletDestination.USER_URL) && (userRole == Role.GUEST)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + page);
                return;
            } else if (uri.contains(ServletDestination.GUEST_URL) && (userRole != Role.GUEST)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + page);
                return;
            } else if (uri.contains(ServletDestination.ERROR_URL) || uri.contains(ServletDestination.COMPONENT_URL)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + page);
                return;
            }
            chain.doFilter(req, resp);
        } catch (IOException | ServletException e) {
            //todo
        }
    }
}