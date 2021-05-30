package com.epam.jwd_critics.model.util;

public class ApplicationProperties {
    private final String url;
    private final String databaseName;
    private final String user;
    private final String password;

    private final Integer minPoolSize;
    private final Integer maxPoolSize;

    public ApplicationProperties(String url, String databaseName, String user, String password, Integer minPoolSize, Integer maxPoolSize) {
        this.url = url;
        this.databaseName = databaseName;
        this.user = user;
        this.password = password;
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    public String getUrl() {
        return url;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Integer getMinPoolSize() {
        return minPoolSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }
}
