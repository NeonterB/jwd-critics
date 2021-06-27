package com.epam.jwd_critics.controller.command.impl.user;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.message.ErrorMessage;
import com.epam.jwd_critics.util.ApplicationPropertiesLoader;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public class UploadPicture implements Command {
    private static final String USER_ICONS_DIR = "user-icons";
    private static final String MOVIE_POSTERS_DIR = "movie-posters";
    private static final String CELEBRITY_ICONS_DIR = "celebrity-icons";
    private static final String SYSTEM_DIR = ApplicationPropertiesLoader.getApplicationProperties().getAssetsDir();
    private static final char FILE_FORMAT_SEPARATOR = '.';

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        try {
            Collection<Part> fileParts = req.getFileParts();
            String uploadDir;
            String prevPage = resp.getDestination().getPath();
            if (prevPage.equals(ServletDestination.UPDATE_USER.getPath())) {
                uploadDir = USER_ICONS_DIR;
            } else if (prevPage.equals(ServletDestination.UPDATE_CELEBRITY.getPath())) {
                uploadDir = CELEBRITY_ICONS_DIR;
            } else if (prevPage.equals(ServletDestination.UPDATE_MOVIE.getPath())) {
                uploadDir = MOVIE_POSTERS_DIR;
            } else {
                throw new CommandException(ErrorMessage.MISSING_ARGUMENTS);
            }
            String fileName = null;
            for (Part part : fileParts) {
                fileName = part.getSubmittedFileName();
                if (fileName != null && !fileName.isEmpty()) {
                    fileName = buildNewFileName(fileName, uploadDir);
                    part.write(SYSTEM_DIR + '/' + fileName);
                    break;
                }
            }
            if (fileName != null && !fileName.isEmpty()) {
                req.setAttribute(Attribute.NEW_IMAGE, fileName);

            }
        } catch (ServletException | IOException e) {
            throw new CommandException(e);
        }
        return resp;
    }

    private String buildNewFileName(String oldFileName, String uploadDir) {
        String fileFormat = oldFileName.substring(oldFileName.indexOf(FILE_FORMAT_SEPARATOR));
        return uploadDir + "/" + UUID.randomUUID() + fileFormat;
    }
}
