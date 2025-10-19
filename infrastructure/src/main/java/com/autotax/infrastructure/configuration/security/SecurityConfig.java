package com.autotax.infrastructure.configuration.security;

import com.autotax.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2020
 **/
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Environment environment;
    private final JWTService jwtService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthProcessingFilter customAuthProcessingFilter(AuthenticationManager authenticationManager, JWTService jwtService, ApplicationEventPublisher eventPublisher) {
        // Define the RequestMatcher for paths that require authentication
        AntPathRequestMatcher requiresAuthenticationRequestMatcher = new AntPathRequestMatcher("/api/**");
        return new CustomAuthProcessingFilter(requiresAuthenticationRequestMatcher, authenticationManager, jwtService, eventPublisher);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthProcessingFilter customAuthProcessingFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("**/actuator/health").permitAll()
                .requestMatchers("**/actuator/**").authenticated()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().permitAll()
            )
            .addFilterBefore(customAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);
            // .oauth2ResourceServer(oauth2 -> oauth2.jwt()); // Uncomment if using native Spring Security OAuth2 Resource Server

        return http.build();
    }

    // TODO: You will need to re-implement or adapt CustomAuthenticationProvider if its logic is still required.
    // @Bean
    // public CustomAuthenticationProvider customAuthenticationProvider() {
    //     return new CustomAuthenticationProvider();
    // }

    // TODO: If you need to configure specific JWT processing (e.g., custom claims extraction),
    // you might need a JwtDecoder bean or a custom JwtAuthenticationConverter.
}

