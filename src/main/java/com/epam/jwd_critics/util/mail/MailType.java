package com.epam.jwd_critics.util.mail;

import com.epam.jwd_critics.controller.command.Parameter;

public enum MailType {
    ACCOUNT_ACTIVATION("http://localhost:8081/controller?command=activate_user&" +
            Parameter.USER_ID.getName() + "=%s&" + Parameter.ACTIVATION_KEY.getName() + "=%s",
            "activation"),
    PASSWORD_RECOVERY("http://localhost:8081/controller?command=open_password_recovery&" +
            Parameter.USER_ID.getName() + "=%s&" + Parameter.RECOVERY_KEY.getName() + "=%s",
            "recovery");
    private final String link;
    private final String propertyName;

    MailType(String link, String propertyName) {
        this.link = link;
        this.propertyName = propertyName;
    }

    public String getLink() {
        return link;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
