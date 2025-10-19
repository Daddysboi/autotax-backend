package com.autotax.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for GitHub integration.
 */
@ConfigurationProperties(prefix = "github", ignoreInvalidFields = true)
@Getter
@Setter
public class GitHubConfigurationProperties {

    /**
     * The base URL for the GitHub API (e.g., https://api.github.com).
     */
    private String apiUrl = "https://api.github.com";

    /**
     * A personal access token for authenticating with the GitHub API.
     */
    private String personalAccessToken;

    /**
     * The owner of the repository (e.g., your GitHub username or organization name).
     */
    private String repositoryOwner;

    /**
     * The name of the repository.
     */
    private String repositoryName;
}
