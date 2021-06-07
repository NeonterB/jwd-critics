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

public class LogInCommand implements Command {
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResponse execute(CommandRequest req) {
        CommandResponse response = new CommandResponse(ServletDestination.LOGIN_PAGE, TransferType.REDIRECT);

        String login = req.getParameter(Parameter.LOGIN);
        String password = req.getParameter(Parameter.PASSWORD);
        if (login == null || password == null) {
            //todo something bad
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
                    return response;
                }
            } else {
                return response;
            }
        }
        return response;
    }

    /*
    public class LoginCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.USER, TransitionType.REDIRECT);
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        if (login == null || password == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_LOGIN_USER_PARAMETERS);
            commandResult.setPage(PagePath.LOGIN);
        } else {
            Map.Entry<List<String>, List<String>> validationResult = userService.validateLoginData(login, password);
            List<String> validParameters = validationResult.getKey();
            List<String> errorMessages = validationResult.getValue();
            if (!errorMessages.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                if (validParameters.contains(Attribute.LOGIN)) sessionRequestContent.setSessionAttribute(
                        Attribute.LOGIN, login);
                commandResult.setPage(PagePath.LOGIN);
            } else {
                User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                if (currentUser.getId() != 0) {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ALREADY_LOGGED_IN);
                    commandResult.setPage(PagePath.MAIN);
                } else {
                    Optional<String> errorMessage;
                    Optional<User> user;
                    Map.Entry<Optional<User>, Optional<String>> findResult;
                    try {
                        findResult = userService.login(login, password);
                        user = findResult.getKey();
                        errorMessage = findResult.getValue();
                        if (errorMessage.isPresent()) {
                            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                            sessionRequestContent.setSessionAttribute(Attribute.LOGIN, login);
                            commandResult.setPage(PagePath.LOGIN);
                        } else {
                            if (user.isPresent()) {
                                sessionRequestContent.setSessionAttribute(Attribute.USER, user.get());
                                sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, user.get());
                                commandResult.setPage(PagePath.USER);
                            }
                        }
                    } catch (ServiceException e) {
                        logger.error(e);
                        throw new CommandException(e);
                    }
                }
            }
        }
        return commandResult;
    }
}
     */
}
