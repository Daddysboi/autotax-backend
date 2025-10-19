package com.autotax.web;

import com.autotax.infrastructure.configuration.AppConfigurationProperties;
import com.autotax.infrastructure.configuration.AuditConfigurationProperties;
import com.autotax.infrastructure.configuration.GitHubConfigurationProperties;
import com.autotax.infrastructure.configuration.KeycloakConfigurationProperties;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.autotax")
@EnableJpaRepositories(basePackages = "com.autotax.dao")
@EntityScan(basePackages = "com.autotax.domain")
@EnableConfigurationProperties({AppConfigurationProperties.class, AuditConfigurationProperties.class, GitHubConfigurationProperties.class, KeycloakConfigurationProperties.class})
public class AutoTaxApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(AutoTaxApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            log.info("------------------------------------------------");
            log.info("|                                               |");
            log.info("|           Started Auto Tax Core API           |");
            log.info("|                                               |");
            log.info("------------------------------------------------");
        };
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AutoTaxApplication.class);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
