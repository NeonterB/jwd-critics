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

import static com.epam.jwd_critics.util.LocalizationUtil.getLocalizedMessageFromResources;

public class ShowAllUsersTag extends TagSupport {
    public static final int USERS_PER_PAGE = 2;
    private static final int USERS_PER_ROW = 6;
    private static final String ROLE_KEY = "user.role";
    private static final String STATUS_KEY = "user.status";
    private static final String BAN_KEY = "button.ban";
    private static final String UNBAN_KEY = "button.unban";


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
        String roleStr = getLocalizedMessageFromResources((String) req.getSessionAttribute(Attribute.LANG), ROLE_KEY);
        String statusStr = getLocalizedMessageFromResources((String) req.getSessionAttribute(Attribute.LANG), STATUS_KEY);
        String banStr = getLocalizedMessageFromResources((String) req.getSessionAttribute(Attribute.LANG), BAN_KEY);
        String unbanStr = getLocalizedMessageFromResources((String) req.getSessionAttribute(Attribute.LANG), UNBAN_KEY);

        String currentPage = (String) req.getAttribute(Attribute.CURRENT_PAGE);
        UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
        if (users != null) {
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                for (int i = 0, j = 0; i < users.size() && i < USERS_PER_PAGE; i++) {
                    UserDTO userDTO = users.get(i);
                    if (j == 0) {
                        writer.write("<div class=\"row\">");
                    }
                    writer.write("<div class=\"col-2\">");
                    writer.write("<a href=\"" + contextPath + "/controller?command=open_user_profile&userId=" + userDTO.getId() + "\">");
                    writer.write("<img class=\"img-thumbnail\" src=\"" + contextPath + "/picture?currentPicture=" + userDTO.getImagePath() + "\" alt=\"" + userDTO.getName() + "\">");
                    writer.write("</a>");
                    writer.write("<p class=\"text-center\">" + userDTO.getName() + "</p>");
                    writer.write("<p class=\"text-center\">" + roleStr + ": " + userDTO.getRole() + "</p>");
                    writer.write("<p class=\"text-center\">" + statusStr + ": " + userDTO.getStatus() + "</p>");
                    writer.write("<p class=\"text-center\">");
                    if (user.getId() != userDTO.getId()) {
                        if (userDTO.getStatus().equals(Status.BANNED)) {
                            writer.write("<a class=\"btnRef\" href=\"" + contextPath + "/controller?command=update_user_status&previousPage=" + currentPage + "&newStatus=active&userId=" + userDTO.getId() + "\">" + unbanStr + "</a>");
                        } else if (userDTO.getStatus().equals(Status.ACTIVE)) {
                            writer.write("<a class=\"btnRef\" href=\"" + contextPath + "/controller?command=update_user_status&previousPage=" + currentPage + "&newStatus=banned&userId=" + userDTO.getId() + "\">" + banStr + "</a>");
                        }
                    }
                    writer.write("</p>");
                    writer.write("</div>");
                    if (j == USERS_PER_ROW - 1 || j == users.size() - 1) {
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
