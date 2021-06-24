package com.epam.jwd_critics.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface CommandRequest {
    Object getAttribute(Attribute attribute);

    void setAttribute(Attribute attribute, Object value);

    String getParameter(Parameter parameter);

    Object getSessionAttribute(Attribute attribute);

    void setSessionAttribute(Attribute attribute, Object value);

    void removeSessionAttribute(Attribute attribute);

    Collection<Part> getFileParts() throws ServletException, IOException;

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

            @Override
            public Collection<Part> getFileParts() throws ServletException, IOException {
                return req.getParts();
            }
        };
    }
}
