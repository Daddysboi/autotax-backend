package com.autotax.web;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.autotax")
@EnableJpaRepositories(basePackages = "com.autotax.dao")
@EntityScan(basePackages = "com.autotax.domain")
public class AutoTaxApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(com.autotax.web.AutoTaxApplication.class, args);
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
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
