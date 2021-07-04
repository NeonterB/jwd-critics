package com.epam.jwd_critics.tag;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Movie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class CountriesTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        CommandRequest req = CommandRequest.from(request);
        JspWriter writer = pageContext.getOut();
        writeCountries(writer, req);
    }

    private void writeCountries(JspWriter writer, CommandRequest req) throws JspException {
        Movie movie = (Movie) req.getSessionAttribute(Attribute.MOVIE);
        Country[] countries = Country.values();
        for (Country country : countries) {
            try {
                if (movie != null && movie.getCountry().equals(country)) {
                    writer.write("<option selected value=\"" + country.getId() + "\">" + country.name() + "</option>");
                } else {
                    writer.write("<option value=\"" + country.getId() + "\">" + country.name() + "</option>");
                }
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
}
