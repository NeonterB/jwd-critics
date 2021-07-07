package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.dto.CelebrityDTO;
import com.epam.jwd_critics.dto.UserDTO;
import com.epam.jwd_critics.entity.Role;
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

public class ShowAllCelebritiesTag extends SimpleTagSupport {
    private static final int celebritiesPerPage = Integer.parseInt(ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.WEBAPP_CELEBRITIES_PER_PAGE));
    private static final int celebritiesPerRow = Integer.parseInt(ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.WEBAPP_CELEBRITIES_PER_ROW));
    private PageContext pageContext;

    public static int getCelebritiesPerPage() {
        return celebritiesPerPage;
    }

    public static int getCelebritiesPerRow() {
        return celebritiesPerRow;
    }

    @Override
    public void doTag() throws JspException {
        pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeCelebrities(writer, req);
        try {
            int celebrityCount = (int) req.getSessionAttribute(Attribute.CELEBRITY_COUNT);
            int pageCount = celebrityCount % celebritiesPerPage == 0 ? (celebrityCount / celebritiesPerPage) : (celebrityCount / celebritiesPerPage + 1);
            String commandName = CommandInstance.OPEN_ALL_CELEBRITIES.toString().toLowerCase();
            TagUtil.paginate(pageContext, pageCount, commandName, Parameter.NEW_CELEBRITIES_PAGE);

            UserDTO user = (UserDTO) req.getSessionAttribute(Attribute.USER);
            if (user != null && user.getRole().equals(Role.ADMIN)) {
                String createButton = getLocalizedMessage((String) req.getSessionAttribute(Attribute.LANG), ContentPropertiesKeys.CREATE);
                writer.write("<p class=\"mt-4\"><a class=\"btnRef mt-2\"" +
                        " href=\"" + pageContext.getRequest().getServletContext().getContextPath() + "/controller?command=open_create_celebrity\">" +
                        createButton +
                        "</a></p>");
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    private void writeCelebrities(JspWriter writer, CommandRequest req) throws JspException {
        List<CelebrityDTO> celebrities = (List<CelebrityDTO>) req.getSessionAttribute(Attribute.CELEBRITIES_TO_DISPLAY);

        String contextPath = pageContext.getServletContext().getContextPath();
        try {
            for (int i = 0, j = 0; i < celebrities.size() && i < celebritiesPerPage; i++) {
                CelebrityDTO celebrityDTO = celebrities.get(i);
                if (j == 0) {
                    writer.write("<div class=\"row\">");
                }
                writer.write("<div class=\"col-2\">");
                writer.write("<a href=\"" + contextPath + "/controller?command=open_celebrity_profile&celebrityId=" + celebrityDTO.getId() + "\">");
                writer.write("<img class=\"img-thumbnail\" src=\"" + contextPath + "/picture?currentPicture=" + celebrityDTO.getImagePath() + "\" alt=\"" + celebrityDTO.getName() + "\">");
                writer.write("</a>");
                writer.write("<p class=\"text-center\">" + celebrityDTO.getName() + "</p>");
                writer.write("</div>");
                if (j == celebritiesPerRow - 1 || j == celebrities.size() - 1) {
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
