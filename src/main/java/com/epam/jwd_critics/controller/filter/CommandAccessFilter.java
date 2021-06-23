package com.epam.jwd_critics.controller.filter;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.impl.common.OpenMoviePageCommand;
import com.epam.jwd_critics.controller.command.impl.common.OpenMovieReviewsPageCommand;
import com.epam.jwd_critics.dto.UserDTO;
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
        UserDTO user = (UserDTO) session.getAttribute(Attribute.USER.getName());
        Role userRole = Role.GUEST;
        Status userStatus = Status.INACTIVE;
        if (user != null) {
            userRole = user.getRole();
            userStatus = user.getStatus();
        }
        String commandName = httpRequest.getParameter(Parameter.COMMAND.getName());
        CommandInstance commandInstance;
        try {
            if (commandName == null) {
                commandName = "open_main";
            }
            commandInstance = CommandInstance.valueOf(commandName.toUpperCase());
        } catch (IllegalArgumentException e) {
            httpRequest.getSession(true).setAttribute(Attribute.COMMAND_ERROR.getName(), "Unknown command");
            httpResponse.sendRedirect(ServletDestination.ERROR_404.getPath());
            return;
        }
        if (!commandInstance.isRoleAllowed(userRole) || (userStatus.equals(Status.INACTIVE) && commandInstance.isUserMustBeActive())) {
            httpRequest.getSession(true).setAttribute(Attribute.FATAL_NOTIFICATION.getName(), "Illegal access to command");
            String page = httpRequest.getParameter(Parameter.CURRENT_PAGE.getName());
            if (page != null) {
                httpResponse.sendRedirect(page);
            } else{
                httpResponse.sendRedirect(ServletDestination.MAIN.getPath());
            }
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
