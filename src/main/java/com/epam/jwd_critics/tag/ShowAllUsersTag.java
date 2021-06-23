package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class ShowAllUsersTag extends TagSupport {
    public static final int USERS_PER_PAGE = 2;
    public static final int USERS_PER_ROW = 6;

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeUsers(writer, req);
        int userCount = (int) req.getSessionAttribute(Attribute.USER_COUNT);
        int pageCount = userCount % USERS_PER_PAGE == 0 ? (userCount / USERS_PER_PAGE) : (userCount / USERS_PER_PAGE + 1);
        String commandName = CommandInstance.OPEN_ALL_USERS.toString().toLowerCase();
        TagUtil.paginate(pageContext, pageCount, commandName, Parameter.NEW_USERS_PAGE);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    private void writeUsers(JspWriter writer, CommandRequest req) throws JspException {
        List<UserDTO> users = (List<UserDTO>) req.getSessionAttribute(Attribute.USERS_TO_DISPLAY);
        String currentPage = (String) req.getAttribute(Attribute.CURRENT_PAGE);
        UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
        if (users != null) {
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                writer.write("<div class=\"container mt-5\">");
                for (int i = 0, j = 0; i < users.size() && i < USERS_PER_PAGE; i++) {
                    UserDTO userDTO = users.get(i);
                    if (j == 0) {
                        writer.write("<div class=\"row\">");
                    }
                    writer.write("<div class=\"col-2\">");
                    writer.write("<a href=\"" + contextPath + "/controller?command=open_user_profile&userId=" + userDTO.getId() + "\">");
                    writer.write("<img class=\"img-thumbnail\" src=\"" + userDTO.getImagePath() + "\" alt=\"" + userDTO.getName() + "\">");
                    writer.write("</a>");
                    writer.write("<p class=\"text-center\">" + userDTO.getName() + "</p>");
                    writer.write("<p class=\"text-center\">Role: " + userDTO.getRole() + "</p>");
                    writer.write("<p class=\"text-center\">Status: " + userDTO.getStatus() + "</p>");
                    if (!user.getId().equals(userDTO.getId())) {
                        if (userDTO.getStatus().equals(Status.BANNED)) {
                            writer.write("<a href=\"" + contextPath + "/controller?command=update_user_status&currentPage=" + currentPage + "&newStatus=active&userId=" + userDTO.getId() + "\">Unban</a>");
                        } else if (userDTO.getStatus().equals(Status.ACTIVE)) {
                            writer.write("<a href=\"" + contextPath + "/controller?command=update_user_status&currentPage=" + currentPage + "&newStatus=banned&userId=" + userDTO.getId() + "\">Ban</a>");
                        }
                    }
                    writer.write("</div>");
                    if (j == USERS_PER_ROW - 1 || j == users.size() - 1) {
                        writer.write("</div>");
                        j = 0;
                    } else {
                        j++;
                    }
                }
                writer.write("</div>");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
