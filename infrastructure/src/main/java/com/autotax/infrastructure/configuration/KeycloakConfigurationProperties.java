package com.autotax.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2020
 **/
@Configuration
@ConfigurationProperties(prefix = "app.keycloak")
@Getter
@Setter
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
