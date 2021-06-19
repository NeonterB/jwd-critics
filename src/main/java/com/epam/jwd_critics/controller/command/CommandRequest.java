package com.epam.jwd_critics.controller.command;

import javax.servlet.http.HttpSession;

public interface CommandRequest {
    Object getAttribute(Attribute attribute);

    void setAttribute(Attribute attribute, Object value);

    String getParameter(Parameter parameter);

    Object getSessionAttribute(Attribute attribute);

    void setSessionAttribute(Attribute attribute, Object value);

    void removeSessionAttribute(Attribute attribute);
}
