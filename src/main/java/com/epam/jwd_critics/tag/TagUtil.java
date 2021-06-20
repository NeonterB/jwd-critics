package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Parameter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class TagUtil {
    static void paginate(PageContext pageContext, int pageCount, String commandName, Parameter newPage) throws JspException {
        try {
            JspWriter writer = pageContext.getOut();
            String contextPath = pageContext.getServletContext().getContextPath();
            writer.write("<form method=\"get\" action=\"" + contextPath + "/controller\">");
            writer.write("<input type=\"hidden\" name=\"command\" value=\"" + commandName + "\"/>");
            writer.write("<nav>");
            writer.write("<ul class=\"pagination\">");
            for (int i = 0; i < pageCount; i++) {
                createButton(writer, i + 1, newPage.getName());
            }
            writer.write("</ul>");
            writer.write("</div>");
            writer.write("</form>");
        } catch (IOException e) {
            //logger.error(e);
            throw new JspException(e);
        }
    }
    static void createButton(JspWriter writer, int pageNumber, String newPage) throws IOException {
        writer.write("<li class=\"page-item\"><button type=\"submit\" name=\"" + newPage + "\" ");
        writer.write("value=\"" + pageNumber + "\" ");
        writer.write(">");
        writer.write(pageNumber + " </button></li>");
    }
}
