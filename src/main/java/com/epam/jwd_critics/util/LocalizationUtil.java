package com.epam.jwd_critics.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationUtil {
    private static final String RESOURCE_NAME = "properties/content";
    private static final Locale DEFAULT_LOCALE = new Locale("en");

    public static String getLocalizedMessageFromResources(String language, String key) {
        Locale locale;
        if (language != null) {
            locale = new Locale(language);
        } else {
            locale = DEFAULT_LOCALE;
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, locale);
        return resourceBundle.getString(key);
    }
}
