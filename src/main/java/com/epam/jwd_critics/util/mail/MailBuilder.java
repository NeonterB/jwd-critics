package com.epam.jwd_critics.util.mail;

import com.epam.jwd_critics.entity.User;

import static com.epam.jwd_critics.util.LocalizationUtil.getLocalizedMessageFromResources;

public class MailBuilder {
    private static final String MAIL_SUBJECT = "mail.subject";
    private static final String MAIL_BODY = "mail.body";
    private final MailType mailType;

    public MailBuilder(MailType type) {
        this.mailType = type;
    }


    public String buildMailSubject(String locale) {
        return getLocalizedMessageFromResources(locale, MAIL_SUBJECT + "." + mailType.getPropertyName());
    }

    public String buildMailBody(User user, String key, String locale) {
        return String.format(getLocalizedMessageFromResources(locale, MAIL_BODY + "." + mailType.getPropertyName()),
                user.getFirstName(), String.format(mailType.getLink(), user.getId(), key));
    }
}
