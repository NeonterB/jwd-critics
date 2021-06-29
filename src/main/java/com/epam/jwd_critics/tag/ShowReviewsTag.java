package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.dto.MovieReviewDTO;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

import static com.epam.jwd_critics.util.LocalizationUtil.getLocalizedMessageFromResources;

public class ShowReviewsTag extends TagSupport {
    public static final int REVIEWS_PER_PAGE = 2;
    private static final String SCORE_KEY = "review.score";
    private static final String DELETE_KEY = "button.delete";

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeReviews(writer, req);
        int reviewCount = (int) req.getSessionAttribute(Attribute.REVIEW_COUNT);
        int pageCount = reviewCount % REVIEWS_PER_PAGE == 0 ? (reviewCount / REVIEWS_PER_PAGE) : (reviewCount / REVIEWS_PER_PAGE + 1);
        String commandName = CommandInstance.OPEN_MOVIE_REVIEWS.toString().toLowerCase();
        TagUtil.paginate(pageContext, pageCount, commandName, Parameter.NEW_REVIEWS_PAGE);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    private void writeReviews(JspWriter writer, CommandRequest req) throws JspException {
        List<MovieReviewDTO> reviews = (List<MovieReviewDTO>) req.getSessionAttribute(Attribute.REVIEWS_TO_DISPLAY);
        String scoreStr = getLocalizedMessageFromResources((String) req.getSessionAttribute(Attribute.LANG), SCORE_KEY);
        String deleteStr = getLocalizedMessageFromResources((String) req.getSessionAttribute(Attribute.LANG), DELETE_KEY);
        String currentPage = (String) req.getAttribute(Attribute.CURRENT_PAGE);
        UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
        Role userRole = Role.GUEST;
        if (user != null) {
            userRole = user.getRole();
        }
        if (reviews != null) {
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                for (int i = 0; i < reviews.size() && i < REVIEWS_PER_PAGE; i++) {
                    MovieReviewDTO review = reviews.get(i);
                    writer.write("<div class=\"row mt-4\">");

                    writer.write("<div class=\"col-1\">");
                    writer.write("<a href=\"" + contextPath + "/controller?command=open_user_profile&userId=" + review.getUserId() + "\">");
                    writer.write("<img class=\"img-thumbnail\" src=\"" + contextPath + "/picture?currentPicture=" + review.getImagePath() + "\" alt=\"" + review.getTitle() + "\">");
                    writer.write("</a>");
                    writer.write("</div>");

                    writer.write("<div class=\"col\">");
                    writer.write("<strong>" + review.getTitle() + "</strong><br>");
                    writer.write(scoreStr + ": " + review.getScore() + "<br>");
                    writer.write(review.getText());
                    writer.write("</div>");

                    if (userRole.equals(Role.ADMIN)) {
                        writer.write("<div class=\"col-1\">");
                        writer.write("<table style=\"height: 100px;\">");
                        writer.write("<tbody><tr>");
                        writer.write("<td class=\"align-middle\">");
                        writer.write("<a class=\"btnRef\" href=\"" + contextPath + "/controller?command=delete_movie_review&movieReviewId=" + review.getId() + "&previousPage=" + currentPage + "\">");
                        writer.write(deleteStr);
                        writer.write("</a>");
                        writer.write("</td>");
                        writer.write("</tr></tbody></table>");
                        writer.write("</div>");
                    }

                    writer.write("</div>");
                }
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
