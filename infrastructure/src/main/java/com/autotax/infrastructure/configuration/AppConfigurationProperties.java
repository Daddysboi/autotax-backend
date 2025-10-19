package com.autotax.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kene Iloeje
 * email: kenyfrank@gmail.com
 * Feb, 2023
 **/
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = true)
@Getter
@Setter
public class AppConfigurationProperties {
    private String authBaseUrl;
    private String reportApiBaseUrl;
    private String frontendBaseUrl;
    private String serverIpAddress;
    /**
     * Path to the directory where to find email templates
     */
    private String emailTemplatesDirectory;
    private String reportTemplatesDirectory;
    private String htmlReportTemplatesDirectory;
    /**
     * Secret key for communication with nvs
     */
    private String middlewareSecretKey;

    private String twilioAccountSSID;

    private String twilioAuthToken;

    private String dynamicLinksDomainUriPrefix;
    private String metabaseSiteUrl;
    private String metabaseSecretKey;
    private String keystorePath;
    private String mailgunApiBaseUrl;
    private String mailgunApiKey;
    /**
     * This is the secret key to be used for all aes encryptions
     */
    private String encryptionSecretKey;
    private String customAuthTokenSecret;
    /**
     * This is the base url that will be used when contacting file server
     */
    private String fileServerBaseUrl;
    private String bwPaymentBaseUrl;
    private String mobileAppClientId;
    private String mobileAppClientSecret;

    /** Directory path for image report download*/
    private String reportImagePath;
    private String cadenceReportImagePath;


    /**
     * Path to the Production Process Training Guide Director
     */
    private String trainingGuideDirectory;

    private String cronCadenceStartTime;
}
