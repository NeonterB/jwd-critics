package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.util.ContentPropertiesKeys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;

import static com.epam.jwd_critics.util.LocalizationUtil.getLocalizedMessage;

public class MovieTag extends SimpleTagSupport {
    private PageContext pageContext;

    @Override
    public void doTag() throws JspException {
        pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeMovie(writer, req);
    }

    private void writeMovie(JspWriter writer, CommandRequest req) throws JspException {
        Movie movie = (Movie) req.getSessionAttribute(Attribute.MOVIE);
        String language = (String) req.getSessionAttribute(Attribute.LANG);
        String ratingLabel = getLocalizedMessage(language, ContentPropertiesKeys.MOVIE_RATING);
        String releaseDateLabel = getLocalizedMessage(language, ContentPropertiesKeys.MOVIE_RELEASE_DATE);
        String runtimeLabel = getLocalizedMessage(language, ContentPropertiesKeys.MOVIE_RUNTIME);
        String countryLabel = getLocalizedMessage(language, ContentPropertiesKeys.MOVIE_COUNTRY);
        String genresLabel = getLocalizedMessage(language, ContentPropertiesKeys.MOVIE_GENRES);
        String ageRestrictionLabel = getLocalizedMessage(language, ContentPropertiesKeys.MOVIE_AGE_RESTRICTION);
        String reviewCountLabel = getLocalizedMessage(language, ContentPropertiesKeys.MOVIE_REVIEW_COUNT);
        String summaryLabel = getLocalizedMessage(language, ContentPropertiesKeys.MOVIE_SUMMARY);

        String releaseDate = movie.getReleaseDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        String ageRestriction = movie.getAgeRestriction().name().replace("_", "-");
        String runtime = "";
        long hours = movie.getRuntime().toHours();
        runtime += (hours > 0) ? ((hours == 1) ? ("1 hour ") : (hours + " hours ")) : ("");
        long minutes = movie.getRuntime().toMinutes() % 60;
        runtime += (minutes > 0) ? ((minutes == 1) ? ("1 minute") : (minutes + " minutes")) : ("");
        String contextPath = pageContext.getServletContext().getContextPath();
        try {
            writer.write("<div class=\"row mt-4\">");

            writer.write("<div class=\"col-4\">");
            writer.write("<img src=\"" + contextPath + "/picture?currentPicture=" + movie.getImagePath() + "\" alt=\"" + movie.getName() + "\"" + "class=\"img-thumbnail\">");
            writer.write("</div>");

            writer.write("<div class=\"col-4\">");
            writer.write("<strong>" + releaseDateLabel + ":</strong> " + releaseDate + "<br>");
            writer.write("<strong>" + runtimeLabel + ":</strong> " + runtime + "<br>");
            writer.write("<strong>" + countryLabel + ":</strong> " + movie.getCountry() + "<br>");
            writer.write("<strong>" + genresLabel + ":</strong> ");
            if (movie.getGenres() == null || movie.getGenres().size() == 0) {
                writer.write("Unknown");
            } else {
                for (Genre genre : movie.getGenres()) {
                    writer.write(genre.name() + ", ");
                }
            }
            writer.write("<br>");
            writer.write("<strong>" + ageRestrictionLabel + ":</strong> " + ageRestriction + "<br>");
            writer.write("<strong>" + ratingLabel + ":</strong> " + movie.getRating() + "<br>");
            writer.write("<strong>" + reviewCountLabel + ":</strong> " + movie.getReviewCount());
            writer.write("<hr/>");
            if (movie.getStaff() != null && !movie.getStaff().isEmpty()) {
                for (Map.Entry<Position, List<Celebrity>> entry : movie.getStaff().entrySet()) {
                    writer.write("<div class=\"row\">");
                    writer.write("<div class=\"col\">");
                    writer.write("<strong>" + entry.getKey() + "S</strong>:");
                    for (Celebrity celebrity : entry.getValue()) {
                        writer.write("<a class=\"dark-link\"");
                        writer.write("href=\"" + contextPath + "/controller?command=open_celebrity_profile&celebrityId=" + celebrity.getId() + "\">");
                        writer.write(celebrity.getFullName() + ",</a>");
                    }
                    writer.write("</div>");
                    writer.write("</div>");
                }
            }
            writer.write("</div>");

            writer.write("<div class=\"col-4\">");
            writer.write("<strong>" + summaryLabel + ":</strong> " + movie.getSummary());
            writer.write("</div>");

            writer.write("</div>");
        } catch (IOException e) {
            throw new JspException(e);
        }
    }
}
