package middleware.whitelist.config;

import middleware.whitelist.aspect.DoJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration class for white-list functionality.
 * It registers "whiteListConfig" and the AOP aspect, so that
 * any method annotated with @WhiteListCheck will be processed.
 */
@Configuration
@ConditionalOnClass(WhiteListProperties.class)
@EnableConfigurationProperties(WhiteListProperties.class)
public class WhiteListAutoConfigure {

    /**
     * Register a Bean storing the comma-separated white-list.
     * Bean name is "whiteListConfig". If the user defines the same Bean,
     * this one will be skipped due to @ConditionalOnMissingBean.
     */
    @Bean("whiteListConfig")
    @ConditionalOnMissingBean
    public String whiteListConfig(WhiteListProperties properties) {
        return properties.getUsers() == null ? "" : properties.getUsers();
    }

    /**
     * Register the DoJoinPoint AOP aspect if none is present.
     */
    @Bean
    @ConditionalOnMissingBean
    public DoJoinPoint doJoinPoint() {
        return new DoJoinPoint();
    }
}

