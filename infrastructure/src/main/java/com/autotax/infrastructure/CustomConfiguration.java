package com.autotax.infrastructure;


import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * @author Kene Iloeje <kenyfrank@gmail.com>
 */
@Configuration
@EnableAsync
@EnableJpaAuditing
@Slf4j
public class CustomConfiguration {

    // The AppConfigurationProperties and Environment beans are no longer injected here
    // to prevent circular dependency issues during startup. When you uncomment the beans
    // below, you should pass AppConfigurationProperties as a method parameter to the
    // specific @Bean methods that require it.
    //
    // Example:
    // @Bean
    // public MailgunApi mailgunApi(AppConfigurationProperties appConfig) { ... }

    @Bean
    public Gson gson() {
        // TODO: The custom type adapters (e.g., OffsetTimeConverter) are missing. You may need to recreate them.
        return new GsonBuilder()
                .create();
    }

    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        return executor;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Logger getLogger(InjectionPoint injectionPoint) {
        Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
        return LoggerFactory.getLogger(classOnWired);
    }

    // TODO: The following beans were commented out because their dependencies are missing.
    // To re-enable them, you will need to:
    // 1. Re-implement the missing service classes (e.g., SettingService, KeycloakService).
    // 2. Pass any required configuration (like AppConfigurationProperties) as a method parameter.

}
