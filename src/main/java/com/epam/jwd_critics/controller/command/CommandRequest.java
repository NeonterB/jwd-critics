package com.epam.jwd_critics.controller.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface CommandRequest {
    Object getAttribute(Attribute attribute);

    void setAttribute(Attribute attribute, Object value);

    String getParameter(Parameter parameter);

    Object getSessionAttribute(Attribute attribute);

    void setSessionAttribute(Attribute attribute, Object value);

    void removeSessionAttribute(Attribute attribute);

    static CommandRequest from(HttpServletRequest req){
        return new CommandRequest() {
            @Override
            public Object getAttribute(Attribute attribute) {
                return req.getAttribute(attribute.getName());
            }

            @Override
            public void setAttribute(Attribute attribute, Object value) {
                req.setAttribute(attribute.getName(), value);
            }

            @Override
            public String getParameter(Parameter parameter) {
                return req.getParameter(parameter.getName());
            }

            @Override
            public Object getSessionAttribute(Attribute attribute) {
                return req.getSession().getAttribute(attribute.getName());
            }

            @Override
            public void setSessionAttribute(Attribute attribute, Object value) {
                req.getSession().setAttribute(attribute.getName(), value);
            }

            @Override
            public void removeSessionAttribute(Attribute attribute) {
                req.getSession().removeAttribute(attribute.getName());
            }
        };
    }
}
