package com.autotax.infrastructure.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakSpringBootProperties.class)
public class KeycloakAdminClientConfig {

    @Bean
    public Keycloak keycloakAdmin(KeycloakSpringBootProperties keycloakSpringBootProperties) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakSpringBootProperties.getAuthServerUrl())
                .realm("master") // master realm for admin operations
                .grantType("password")
                .clientId(keycloakSpringBootProperties.getResource())
                .clientSecret((String) keycloakSpringBootProperties.getCredentials().get("secret"))
                .username("admin") // replace with admin username from properties
                .password("admin") // replace with admin password from properties
                .build();
    }
}
