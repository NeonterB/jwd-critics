package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.dto.MovieDTO;
import com.epam.jwd_critics.util.ContentPropertiesKeys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;

import static com.epam.jwd_critics.util.LocalizationUtil.getLocalizedMessageFromResources;

public class ShowAllMoviesTag extends SimpleTagSupport {
    public static final int MOVIES_PER_PAGE = 1;
    private static final int MOVIES_PER_ROW = 4;
    private PageContext pageContext;

    @Override
    public void doTag() throws JspException {
        pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeMovies(writer, req);
        int movieCount = (int) req.getSessionAttribute(Attribute.MOVIE_COUNT);
        int pageCount = movieCount % MOVIES_PER_PAGE == 0 ? (movieCount / MOVIES_PER_PAGE) : (movieCount / MOVIES_PER_PAGE + 1);
        String commandName = CommandInstance.OPEN_ALL_MOVIES.toString().toLowerCase();
        TagUtil.paginate(pageContext, pageCount, commandName, Parameter.NEW_MOVIES_PAGE);
    }

    private void writeMovies(JspWriter writer, CommandRequest req) throws JspException {
        List<MovieDTO> movies = (List<MovieDTO>) req.getSessionAttribute(Attribute.MOVIES_TO_DISPLAY);
        String ratingStr = getLocalizedMessageFromResources((String) req.getSessionAttribute(Attribute.LANG), ContentPropertiesKeys.MOVIE_RATING);
        if (movies != null) {
            String contextPath = pageContext.getRequest().getServletContext().getContextPath();
            try {
                for (int i = 0, j = 0; i < movies.size() && i < MOVIES_PER_PAGE; i++) {
                    MovieDTO movie = movies.get(i);
                    if (j == 0) {
                        writer.write("<div class=\"row\">");
                    }
                    writer.write("<div class=\"col-3\">");
                    writer.write("<a href=\"" + contextPath + "/controller?command=open_movie&movieId=" + movie.getId() + "\">");
                    writer.write("<img class=\"img-thumbnail\" src=\"" + contextPath + "/picture?currentPicture=" + movie.getImagePath() + "\" alt=\"" + movie.getName() + "\">");
                    writer.write("</a>");
                    writer.write("<p class=\"text-center\">" + movie.getName() + "</p>");
                    writer.write("<p class=\"text-center\">" + ratingStr + ": " + movie.getRating() + "</p>");
                    writer.write("</div>");
                    if (j == MOVIES_PER_ROW - 1 || j == movies.size() - 1) {
                        writer.write("</div>");
                        j = 0;
                    } else {
                        j++;
                    }
                }
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
