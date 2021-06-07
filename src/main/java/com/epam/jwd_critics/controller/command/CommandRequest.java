package com.epam.jwd_critics.controller.command;

import javax.servlet.http.HttpSession;

public interface CommandRequest {
    Object getAttribute(Attribute attribute);

    String getParameter(Parameter parameter);

    HttpSession getSession(boolean flag);
}
