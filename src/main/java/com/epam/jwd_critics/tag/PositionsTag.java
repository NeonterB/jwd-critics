package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.entity.Position;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class PositionsTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        JspWriter writer = pageContext.getOut();
        writePositions(writer);
    }

    private void writePositions(JspWriter writer) throws JspException {
        Position[] positions = Position.values();
        for (Position position : positions) {
            try {
                writer.write("<div class=\"form-check\">");
                writer.write("<input class=\"form-check-input\" id=\"" + position.name() + "\" type=\"checkbox\" name=\"celebrityPositions\" value=\"" + position.getId() + "\"/>");
                writer.write("<label for=\"" + position.name() + "\">" + position.name() + "</label><br>");
                writer.write("</div>");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
