package com.epam.jwd_critics.controller;

import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.util.ApplicationPropertiesKeys;
import com.epam.jwd_critics.util.ApplicationPropertiesLoader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/picture")
public class PictureServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String pictureName = request.getParameter(Parameter.CURRENT_PICTURE.getName());
            String assetsDir = ApplicationPropertiesLoader.get(ApplicationPropertiesKeys.WEBAPP_ASSETS_DIR);
            byte[] image = Files.readAllBytes(Paths.get(assetsDir + '/' + pictureName));
            response.setHeader("Content-Disposition", "inline filename=\"" + pictureName + "\"");
            response.setContentType(getServletContext().getMimeType(pictureName));
            response.setContentLength(image.length);
            response.getOutputStream().write(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
