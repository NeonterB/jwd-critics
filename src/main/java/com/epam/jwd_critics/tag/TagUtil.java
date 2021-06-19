package com.epam.jwd_critics.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class TagUtil {
    static void paginate(PageContext pageContext, int pageCount, String commandName) throws JspException {
        try {
            JspWriter writer = pageContext.getOut();
            String contextPath = pageContext.getServletContext().getContextPath();
            writer.write("<form method=\"post\" action=\"" + contextPath + "/controller?command=" + commandName + "\">");
            writer.write("<div class=\"accent-bar\">");
            writer.write("<ul class=\"accents\">");
            for (int i = 0; i < pageCount; i++) {
                createButton(writer, i + 1);
            }
            writer.write("</ul>");
            writer.write("</div>");
            writer.write("</form>");
        } catch (IOException e) {
            //logger.error(e);
            throw new JspException(e);
        }
    }
    static void createButton(JspWriter writer, int pageNumber) throws IOException {
        writer.write("<li><button type=\"submit\" name=\"newPage\" ");
        writer.write("value=\"" + pageNumber + "\" ");
        writer.write("style=\""
                + "background-color: #ffffffb8; color: #000" + "\">");
        writer.write(pageNumber + " </button></li>");
    }
}
