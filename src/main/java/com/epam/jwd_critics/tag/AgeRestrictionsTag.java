package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Movie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class AgeRestrictionsTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeAgeRestrictions(writer, req);
    }

    private void writeAgeRestrictions(JspWriter writer, CommandRequest req) throws JspException {
        Movie movie = (Movie) req.getSessionAttribute(Attribute.MOVIE);
        AgeRestriction[] ageRestrictions = AgeRestriction.values();
        for (AgeRestriction ageRestriction : ageRestrictions) {
            try {
                writer.write("<div class=\"form-check\">");
                if (movie != null && movie.getAgeRestriction().equals(ageRestriction)) {
                    writer.write("<input class=\"form-check-input\" id=\"" + ageRestriction.name() + "\" type=\"radio\" name=\"movieAgeRestrictionId\" checked value=\"" + ageRestriction.getId() + "\"/>");
                } else {
                    writer.write("<input class=\"form-check-input\" id=\"" + ageRestriction.name() + "\" type=\"radio\" name=\"movieAgeRestrictionId\" value=\"" + ageRestriction.getId() + "\"/>");
                }
                writer.write("<label for=\"" + ageRestriction.name() + "\">" + ageRestriction.name().replace("_", "-") + "</label><br>");
                writer.write("</div>");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
