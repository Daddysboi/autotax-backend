package com.autotax.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Jeremiah Udoh
 * @Email judoh@byteworks.com.ng
 * 05, 05/09/2022 - 10:55
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.audit")
@Getter
@Setter
public class AuditConfigurationProperties {
    private String url;
    private String username;
    private String password;
    private String schema;
    private String hibernateDialect;
    private String driverClassName;

    private int maxIdle;
    private int minIdle;
    private int maxActive;

}
