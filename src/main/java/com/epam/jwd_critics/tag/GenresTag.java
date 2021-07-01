package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class GenresTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeGenres(writer, req);
    }

    private void writeGenres(JspWriter writer, CommandRequest req) throws JspException {
        Movie movie = (Movie) req.getSessionAttribute(Attribute.MOVIE);
        Genre[] genres = Genre.values();
        for (Genre genre : genres) {
            try {
                writer.write("<div class=\"form-check\">");
                if (movie != null && movie.getGenres().contains(genre)) {
                    writer.write("<input class=\"form-check-input\" id=\"" + genre.name() + "\" type=\"checkbox\" name=\"movieGenres\" checked=\"checked\" value=\"" + genre.getId() + "\"/>");
                } else {
                    writer.write("<input class=\"form-check-input\" id=\"" + genre.name() + "\" type=\"checkbox\" name=\"movieGenres\" value=\"" + genre.getId() + "\"/>");
                }
                writer.write("<label for=\"" + genre.name() + "\">" + genre.name() + "</label><br>");
                writer.write("</div>");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
