package com.epam.jwd_critics.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationUtil {
    private static final String RESOURCE_NAME = "properties/content";

    public static String getLocalizedMessage(String language, String key) {
        Locale locale = Locale.getDefault();
        if (language != null) {
            locale = new Locale(language);
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, locale);
        return resourceBundle.getString(key);
    }
}
