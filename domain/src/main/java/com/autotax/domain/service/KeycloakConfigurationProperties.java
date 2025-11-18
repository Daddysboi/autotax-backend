package com.autotax.domain.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "keycloak.frontend")
public class KeycloakConfigurationProperties {
    /**
     * The username of the keycloak admin. This will be used for running admin commands
     */
    private String adminUserName;
    /**
     * The password of the keycloak admin. This will be used for running admin commands
     */
    private String adminPassword;
    /**
     * The url of the cloak instance
     */
    private String authUrl;
    private String authExternalBaseUrl;

    private String realm;

    private String clientId;
    private String clientSecret;
    private String frontendClientId;
}
