package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.model.entity.Movie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class ShowAllMoviesTag extends TagSupport {
    public static final int MOVIES_PER_PAGE_NUMBER = 5;
    private static final String RATING_BUNDLE = "movie.rating";

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = new CommandRequest() {
            @Override
            public Object getAttribute(Attribute attribute) {
                return httpServletRequest.getAttribute(attribute.getName());
            }

            @Override
            public void setAttribute(Attribute attribute, Object value) {
                httpServletRequest.setAttribute(attribute.getName(), value);
            }

            @Override
            public String getParameter(Parameter parameter) {
                return httpServletRequest.getParameter(parameter.getName());
            }

            @Override
            public Object getSessionAttribute(Attribute attribute) {
                return httpServletRequest.getSession().getAttribute(attribute.getName());
            }

            @Override
            public void setSessionAttribute(Attribute attribute, Object value) {
                httpServletRequest.getSession().setAttribute(attribute.getName(), value);
            }

            @Override
            public void removeSessionAttribute(Attribute attribute) {
                httpServletRequest.getSession().removeAttribute(attribute.getName());
            }
        };
        JspWriter writer = pageContext.getOut();
        writeMovies(writer, req);
        int movieCount = (int) req.getSessionAttribute(Attribute.MOVIE_COUNT);
        int pageCount = movieCount % MOVIES_PER_PAGE_NUMBER == 0 ? (movieCount / MOVIES_PER_PAGE_NUMBER) : (movieCount / MOVIES_PER_PAGE_NUMBER + 1);
        String commandName = CommandInstance.OPEN_ALL_MOVIES.toString().toLowerCase();
        TagUtil.paginate(pageContext, pageCount, commandName);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    private void writeMovies(JspWriter writer, CommandRequest req) throws JspException {
        List<Movie> movies = (List<Movie>) req.getSessionAttribute(Attribute.MOVIES_TO_DISPLAY);
        if (movies != null) {
            int size = movies.size();
            int createdMoviesCount = 0;
            Movie movie;
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                writer.write("<ul>");
                for (int i = 0; i < MOVIES_PER_PAGE_NUMBER; i++) {
                    if (size > createdMoviesCount) {
                        movie = movies.get(createdMoviesCount);
                        writer.write("<li>");
                        writer.write(" <div class=\"movie\">");
                        writer.write("<a href=\"" + contextPath + "/controller?command=open_movie_page&movieId=" + movie.getId() + "\">");
                        writer.write("<h4 class=\"title\">" + movie.getName() + "</h4>");
                        writer.write("</a>");
                        writer.write("<div class=\"poster\">");
                        writer.write("<a href=\"" + contextPath + "/controller?command=open_movie_page&movieId=" + movie.getId() + "\">");
                        writer.write("<img src=\"" + movie.getImagePath() + "\" alt=\"" + movie.getName() + "\"/>");
                        writer.write("</a>");

                        writer.write("</div>");
                        writer.write("<p class=\"description\">rating: " + movie.getRating() + "</p>");
                        writer.write("</div>");
                        writer.write("</li>");
                        createdMoviesCount++;
                    }
                }
                writer.write("</ul>");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
