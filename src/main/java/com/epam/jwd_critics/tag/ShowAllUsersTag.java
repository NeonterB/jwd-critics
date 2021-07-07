package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.util.ApplicationPropertiesKeys;
import com.epam.jwd_critics.util.ApplicationPropertiesLoader;
import com.epam.jwd_critics.util.ContentPropertiesKeys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;

import static com.epam.jwd_critics.util.LocalizationUtil.getLocalizedMessage;

public class ShowAllUsersTag extends SimpleTagSupport {
    private static final int userPerPage = Integer.parseInt(ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.WEBAPP_USERS_PER_PAGE));
    private static final int usersPerRow = Integer.parseInt(ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.WEBAPP_USERS_PER_ROW));
    private PageContext pageContext;

    public static int getUserPerPage() {
        return userPerPage;
    }

    public static int getUsersPerRow() {
        return usersPerRow;
    }

    @Override
    public void doTag() throws JspException {
        pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeUsers(writer, req);
        int userCount = (int) req.getSessionAttribute(Attribute.USER_COUNT);
        int pageCount = userCount % userPerPage == 0 ? (userCount / userPerPage) : (userCount / userPerPage + 1);
        String commandName = CommandInstance.OPEN_ALL_USERS.toString().toLowerCase();
        TagUtil.paginate(pageContext, pageCount, commandName, Parameter.NEW_USERS_PAGE);
    }

    private void writeUsers(JspWriter writer, CommandRequest req) throws JspException {
        List<UserDTO> users = (List<UserDTO>) req.getSessionAttribute(Attribute.USERS_TO_DISPLAY);
        String language = (String) req.getSessionAttribute(Attribute.LANG);
        String roleStr = getLocalizedMessage(language, ContentPropertiesKeys.USER_ROLE);
        String statusStr = getLocalizedMessage(language, ContentPropertiesKeys.USER_STATUS);
        String banStr = getLocalizedMessage(language, ContentPropertiesKeys.BAN);
        String unbanStr = getLocalizedMessage(language, ContentPropertiesKeys.UNBAN);

        String currentPage = (String) req.getAttribute(Attribute.CURRENT_PAGE);
        UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
        if (users != null) {
            String contextPath = pageContext.getServletContext().getContextPath();
            try {
                for (int i = 0, j = 0; i < users.size() && i < userPerPage; i++) {
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
                    if (j == usersPerRow - 1 || j == users.size() - 1) {
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
