package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandInstance;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.dto.CelebrityDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;

public class ShowAllCelebritiesTag extends SimpleTagSupport {
    public static final int CELEBRITIES_PER_PAGE = 2;
    private static final int CELEBRITIES_PER_ROW = 6;
    private PageContext pageContext;


    @Override
    public void doTag() throws JspException {
        pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeCelebrities(writer, req);
        int celebrityCount = (int) req.getSessionAttribute(Attribute.CELEBRITY_COUNT);
        int pageCount = celebrityCount % CELEBRITIES_PER_PAGE == 0 ? (celebrityCount / CELEBRITIES_PER_PAGE) : (celebrityCount / CELEBRITIES_PER_PAGE + 1);
        String commandName = CommandInstance.OPEN_ALL_CELEBRITIES.toString().toLowerCase();
        TagUtil.paginate(pageContext, pageCount, commandName, Parameter.NEW_CELEBRITIES_PAGE);
    }

    private void writeCelebrities(JspWriter writer, CommandRequest req) throws JspException {
        List<CelebrityDTO> celebrities = (List<CelebrityDTO>) req.getSessionAttribute(Attribute.CELEBRITIES_TO_DISPLAY);

        String contextPath = pageContext.getServletContext().getContextPath();
        try {
            for (int i = 0, j = 0; i < celebrities.size() && i < CELEBRITIES_PER_PAGE; i++) {
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
                if (j == CELEBRITIES_PER_ROW - 1 || j == celebrities.size() - 1) {
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
