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

public class ShowReviewsTag extends TagSupport {
    public static final int REVIEWS_PER_PAGE = 2;

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
        writeReviews(writer, req);
        int reviewCount = (int) req.getSessionAttribute(Attribute.REVIEW_COUNT);
        int pageCount = reviewCount % REVIEWS_PER_PAGE == 0 ? (reviewCount / REVIEWS_PER_PAGE) : (reviewCount / REVIEWS_PER_PAGE + 1);
        String commandName = CommandInstance.OPEN_MOVIE_REVIEWS.toString().toLowerCase();
        TagUtil.paginate(pageContext, pageCount, commandName, Parameter.NEW_REVIEW_PAGE);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    private void writeReviews(JspWriter writer, CommandRequest req) throws JspException {
        List<MovieReviewDTO> reviews = (List<MovieReviewDTO>) req.getSessionAttribute(Attribute.REVIEWS_TO_DISPLAY);
        String currentPage = (String) req.getAttribute(Attribute.CURRENT_PAGE);
        UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
        Role userRole = Role.GUEST;
        if (user != null) {
            userRole = user.getRole();
        }
        if (reviews != null) {
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                writer.write("<div class=\"container mt-5\">");
                for (int i = 0; i < reviews.size() && i < REVIEWS_PER_PAGE; i++) {
                    MovieReviewDTO review = reviews.get(i);
                    writer.write("<div class=\"row mt-4\">");

                    writer.write("<div class=\"col-1\">");
                    writer.write("<a href=\"" + contextPath + "/controller?command=open_profile&userId=" + review.getUserId() + "\">");
                    writer.write("<img class=\"img-thumbnail\" src=\"" + review.getUserImagePath() + "\" alt=\"" + review.getUserName() + "\">");
                    writer.write("</a>");
                    writer.write("</div>");

                    writer.write("<div class=\"col\">");
                    writer.write("<strong>" + review.getUserName() + "</strong><br>");
                    writer.write("Score: " + review.getScore() + "<br>");
                    writer.write(review.getText());
                    writer.write("</div>");

                    if (userRole.equals(Role.ADMIN)) {
                        writer.write("<div class=\"col-1\">");
                        writer.write("<a href=\"" + contextPath + "/controller?command=delete_movie_review&movieReviewId=" + review.getId() + "&currentPage=" + currentPage + "\">");
                        writer.write("Delete");
                        writer.write("</a>");
                        writer.write("</div>");
                    }

                    writer.write("</div>");
                }
                writer.write("</div>");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
