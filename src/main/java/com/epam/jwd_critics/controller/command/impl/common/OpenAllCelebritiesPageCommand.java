package com.epam.jwd_critics.controller.command.impl.common;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.dto.CelebrityDTO;
import com.epam.jwd_critics.entity.Celebrity;
import com.epam.jwd_critics.exception.CommandException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.service.CelebrityService;
import com.epam.jwd_critics.service.impl.CelebrityServiceImpl;
import com.epam.jwd_critics.tag.ShowAllCelebritiesTag;

import java.util.List;
import java.util.stream.Collectors;

public class OpenAllCelebritiesPageCommand implements Command {
    private final CelebrityService celebrityService = CelebrityServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) throws CommandException {
        CommandResponse resp = new CommandResponse(ServletDestination.ALL_CELEBRITIES, TransferType.FORWARD);

        Integer currentPage = (Integer) req.getSessionAttribute(Attribute.ALL_CELEBRITIES_CURRENT_PAGE);
        String newPageStr = req.getParameter(Parameter.NEW_CELEBRITIES_PAGE);
        if (newPageStr != null) {
            currentPage = Integer.valueOf(newPageStr);
        } else if (currentPage == null) {
            currentPage = 1;
        }
        req.setSessionAttribute(Attribute.ALL_CELEBRITIES_CURRENT_PAGE, currentPage);
        int begin = (currentPage - 1) * ShowAllCelebritiesTag.CELEBRITIES_PER_PAGE;
        int end = ShowAllCelebritiesTag.CELEBRITIES_PER_PAGE + begin;
        try {
            List<Celebrity> celebrities = celebrityService.getAllBetween(begin, end);
            List<CelebrityDTO> celebrityDTOS = celebrities.stream().map(CelebrityDTO::new).collect(Collectors.toList());
            req.setSessionAttribute(Attribute.CELEBRITIES_TO_DISPLAY, celebrityDTOS);
            int celebrityCount = celebrityService.getCount();
            req.setSessionAttribute(Attribute.CELEBRITY_COUNT, celebrityCount);
            if (celebrityDTOS.size() == 0) {
                req.setSessionAttribute(Attribute.INFO_MESSAGE, "No celebrities here yet");
            }
        } catch (ServiceException e) {
            req.setSessionAttribute(Attribute.FATAL_NOTIFICATION, e.getMessage());
            resp = CommandResponse.redirectToPreviousPageOr(ServletDestination.MAIN, req);
        }
        return resp;
    }
}
