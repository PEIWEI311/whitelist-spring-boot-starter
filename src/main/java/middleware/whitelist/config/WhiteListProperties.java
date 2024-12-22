package middleware.whitelist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for white-list settings.
 * It will read the property "bugstack.whitelist.users" from application.yml/properties,
 * which typically stores comma-separated user accounts.
 */
@ConfigurationProperties(prefix = "bugstack.whitelist")
public class WhiteListProperties {

    /**
     * Stores comma-separated users, e.g. "zhangsan,lisi,wangwu".
     */
    private String users;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}

