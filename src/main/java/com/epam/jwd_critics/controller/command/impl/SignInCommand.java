package com.epam.jwd_critics.controller.command.impl;

import com.epam.jwd_critics.controller.command.Attribute;
import com.epam.jwd_critics.controller.command.Command;
import com.epam.jwd_critics.controller.command.CommandRequest;
import com.epam.jwd_critics.controller.command.CommandResponse;
import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.controller.command.ServletDestination;
import com.epam.jwd_critics.controller.command.TransferType;
import com.epam.jwd_critics.controller.validation.ConstraintViolation;
import com.epam.jwd_critics.controller.validation.UserValidator;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.model.entity.User;
import com.epam.jwd_critics.model.service.UserService;
import com.epam.jwd_critics.model.service.impl.UserServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.Set;
import java.util.stream.Collectors;

public class SignInCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse response = new CommandResponse(ServletDestination.SIGN_IN_PAGE, TransferType.REDIRECT);

        String login = req.getParameter(Parameter.LOGIN);
        String password = req.getParameter(Parameter.PASSWORD);
        if (login == null || password == null) {
            req.getSession(true).setAttribute(Attribute.VALIDATION_ERRORS.getName(), "Sign in fields can't be empty");
        } else {
            UserValidator userValidator = new UserValidator();
            Set<ConstraintViolation> violations = userValidator.validateLogInData(login, password);
            if (violations.isEmpty()) {
                try {
                    User user = userService.login(login, password);
                    HttpSession reqSession = req.getSession(true);
                    reqSession.setAttribute(Attribute.USER_ID.getName(), user.getId());
                    reqSession.setAttribute(Attribute.USER_ROLE.getName(), user.getRole());
                    response.setDestination(ServletDestination.MAIN_PAGE);
                    response.setTransferType(TransferType.FORWARD);
                } catch (ServiceException e) {
                    req.getSession(true).setAttribute(Attribute.SERVICE_ERROR.getName(), e.getMessage());
                    return response;
                }
            } else {
                req.getSession(true).setAttribute(Attribute.VALIDATION_ERRORS.getName(), violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()));
                return response;
            }
        }
        return response;
    }
}
