package com.epam.jwd_critics.util.mail;

import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.entity.User;

import static com.epam.jwd_critics.util.LocalizationUtil.getLocalizedMessageFromResources;

public class MailBuilder {
    private static final String EMAIL_SUBJECT = "mail.subject";
    private static final String EMAIL_BODY = "mail.body";


    private static final String LINK_FOR_CONFIRMATION = "http://localhost:8081/controller?command=activate_user&" +
            Parameter.USER_ID.getName() + "=%s&" + Parameter.ACTIVATION_KEY.getName() + "=%s";

    private MailBuilder() {
    }


    public static String buildEmailSubject(String locale) {
        return getLocalizedMessageFromResources(locale, EMAIL_SUBJECT);
    }

    public static String buildEmailBody(User user, String key, String locale) {
        return String.format(getLocalizedMessageFromResources(locale, EMAIL_BODY),
                user.getFirstName(), String.format(LINK_FOR_CONFIRMATION, user.getId(), key));
    }
}
