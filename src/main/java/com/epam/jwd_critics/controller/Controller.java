package com.epam.jwd_critics.controller;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        final String commandName = req.getParameter(Parameter.COMMAND.getName());
        Command command = Command.of(commandName);
        if (command != null) {
            CommandResponse response = command.execute(new CommandRequest() {
                @Override
                public Object getAttribute(Attribute attribute) {
                    return req.getAttribute(attribute.getName());
                }

                @Override
                public String getParameter(Parameter parameter) {
                    return req.getParameter(parameter.getName());
                }

                @Override
                public HttpSession getSession(boolean flag) {
                    return req.getSession(flag);
                }
            });
            try {
                switch (response.getTransferType()) {
                    case FORWARD:
                        req.getRequestDispatcher(response.getDestination().getPath()).forward(req, resp);
                        break;
                    case REDIRECT:
                        resp.sendRedirect(req.getContextPath() + response.getDestination().getPath());
                        break;
                }
            } catch (ServletException | IOException e) {
                //todo
                logger.error(e.getMessage(), e);
            }
        }
    }

}
