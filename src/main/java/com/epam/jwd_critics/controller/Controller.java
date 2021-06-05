package com.epam.jwd_critics.controller;

import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/controller")
public class Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String commandName = req.getParameter("command");
        Command command = Command.of(commandName);
        if (command != null) {
            CommandResponse response = command.execute(null);
            switch (response.getTransferType()) {
                case FORWARD:
                    req.getRequestDispatcher(response.getDestination().getPath()).forward(req, resp);
                    break;
                case REDIRECT:
                    resp.sendRedirect(response.getDestination().getPath());
                    break;
            }
        }
    }
}
