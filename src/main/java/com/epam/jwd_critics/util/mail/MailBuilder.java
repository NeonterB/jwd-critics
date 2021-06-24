package com.epam.jwd_critics.util.mail;

import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.entity.User;

import java.util.Locale;
import java.util.ResourceBundle;

public class MailBuilder {
    private static final String EMAIL_SUBJECT = "mail.subject";
    private static final String EMAIL_BODY = "mail.body";
    private static final String RESOURCE_NAME = "properties/content";
    private static final Locale DEFAULT_LOCALE = new Locale("en");

    private static final String LINK_FOR_CONFIRMATION = "http://localhost:8081/controller?command=activate_user&" +
            Parameter.USER_ID.getName() + "=%s&" + Parameter.ACTIVATION_KEY.getName() + "=%s";

    private MailBuilder() {
    }

    private static String getLocalizedMessageFromResources(String language, String key) {
        Locale locale;
        if (language != null) {
            locale = new Locale(language);
        } else {
            locale = DEFAULT_LOCALE;
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, locale);
        return resourceBundle.getString(key);
    }


    public static String buildEmailSubject(String locale) {
        String emailSubject = getLocalizedMessageFromResources(locale, EMAIL_SUBJECT);
        return emailSubject;
    }

    public static String buildEmailBody(User user, String key, String locale) {
        String emailBody = String.format(getLocalizedMessageFromResources(locale, EMAIL_BODY),
                user.getFirstName(), String.format(LINK_FOR_CONFIRMATION, user.getId(), key));
        return emailBody;
    }
}
