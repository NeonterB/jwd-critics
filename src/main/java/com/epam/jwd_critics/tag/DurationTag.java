package com.epam.jwd_critics.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DurationTag extends SimpleTagSupport {
    StringWriter sw = new StringWriter();

    @Override
    public void doTag() throws JspException {
        try {
            getJspBody().invoke(sw);
            Duration duration = Duration.parse(sw.toString());
            String result = "";
            long hours = duration.toHours();
            result += (hours < 10) ? ("0" + hours) : (hours);
            result += ":";
            long minutes = duration.toMinutes() % 60;
            result += (minutes < 10) ? ("0" + minutes) : (minutes);
            getJspContext().getOut().write(result);
        } catch (IOException e) {
            throw new JspException(e);
        } catch (DateTimeParseException e) {
            try {
                getJspContext().getOut().write("Failed to parse date");
            } catch (IOException ioException) {
                throw new JspException(ioException);
            }
        }
    }
}
