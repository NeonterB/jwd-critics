package com.epam.jwd_critics.controller.filter;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;

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

@WebFilter(urlPatterns = "/controller/*")
public class CommandAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession();
        Role userRole = (Role) session.getAttribute(Attribute.USER_ROLE.getName());
        if (userRole == null) {
            userRole = Role.GUEST;
        }
        Status userStatus = (Status) session.getAttribute(Attribute.USER_STATUS.getName());
        if (userStatus == null) {
            userStatus = Status.INACTIVE;
        }
        String commandName = httpRequest.getParameter(Parameter.COMMAND.getName());
        CommandInstance commandInstance;
        try {
            if (commandName == null) {
                commandName = "open_main";
            }
            commandInstance = CommandInstance.valueOf(commandName.toUpperCase());
        } catch (IllegalArgumentException e) {
            httpRequest.getSession(true).setAttribute(Attribute.GLOBAL_ERROR.getName(), "Unknown command");
            httpResponse.sendRedirect(ServletDestination.ERROR_404.getPath());
            return;
        }
        if (!commandInstance.isRoleAllowed(userRole) && (userStatus.equals(Status.ACTIVE) || !commandInstance.isUserMustBeActive())) {
            httpRequest.getSession(true).setAttribute(Attribute.GLOBAL_ERROR.getName(), "Illegal access to command");
            httpResponse.sendRedirect((String) session.getAttribute(Parameter.CURRENT_PAGE.getName()));
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
