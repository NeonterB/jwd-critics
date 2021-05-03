package com.epam.jwd_critics.entity;

public class ApplicationProperties {
    private static final ApplicationProperties instance = new ApplicationProperties();

    private String url;
    private String databaseName;
    private String user;
    private String password;

    private Integer minPoolSize;
    private Integer maxPoolSize;

    private ApplicationProperties() {
    }

    public static ApplicationProperties getInstance() {
        return instance;
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

    public void setMinPoolSize(Integer minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
