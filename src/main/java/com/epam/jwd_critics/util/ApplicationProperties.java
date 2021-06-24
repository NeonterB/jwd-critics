package com.epam.jwd_critics.util;

public class ApplicationProperties {
    private final String url;
    private final String databaseName;
    private final String user;
    private final String password;

    private final Integer minPoolSize;
    private final Integer maxPoolSize;

    private final String assetsDir;

    public ApplicationProperties(String url, String databaseName, String user, String password, Integer minPoolSize, Integer maxPoolSize, String assetsDir) {
        this.url = url;
        this.databaseName = databaseName;
        this.user = user;
        this.password = password;
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.assetsDir = assetsDir;
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

    public String getAssetsDir() {
        return assetsDir;
    }
}
