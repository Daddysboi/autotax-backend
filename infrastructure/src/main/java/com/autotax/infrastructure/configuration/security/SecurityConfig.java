package com.autotax.infrastructure.configuration.security;

import com.bw.cfs.services.JWTService;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter.DEFAULT_REQUEST_MATCHER;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2020
 **/
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@RequiredArgsConstructor
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
    private final Environment environment;
    private final JWTService jwtService;

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider
                = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
                new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
        auth.authenticationProvider(new CustomAuthenticationProvider());
    }
//
//    @Bean
//    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }

    @Bean
    @Override
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        RequestMatcher requestMatcher =
                new AndRequestMatcher(
                        new IncludeKeycloakProcessFilterRequestMatcher(),
                        DEFAULT_REQUEST_MATCHER
                );
        KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(authenticationManagerBean(), requestMatcher);
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        return filter;
    }

    @Bean
    protected CustomAuthProcessingFilter customAuthProcessingFilter() throws Exception {
        RequestMatcher requestMatcher =
                new OrRequestMatcher(
                        new AntPathRequestMatcher(KeycloakAuthenticationEntryPoint.DEFAULT_LOGIN_URI),
                        new CustomAuthenticationProcessingFilterRequestMatcher(jwtService)
                );
        CustomAuthProcessingFilter filter = new CustomAuthProcessingFilter(requestMatcher, authenticationManagerBean());
        return filter;
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
                new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        CustomAuthProcessingFilter filter = customAuthProcessingFilter();
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            if (authException != null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print("Unauthorizated....");
            }
        });
        http.addFilterBefore(filter, KeycloakAuthenticationProcessingFilter.class)
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("**/actuator/health").permitAll()
                .antMatchers("**/actuator/**").authenticated()
                .antMatchers("/v3/api-docs/**").permitAll()
                .anyRequest()
                .permitAll();
    }

    private static class IncludeKeycloakProcessFilterRequestMatcher implements RequestMatcher {

        IncludeKeycloakProcessFilterRequestMatcher() {

        }

        public boolean matches(HttpServletRequest request) {
            return !(SecurityContextHolder.getContext().getAuthentication() instanceof CustomAuthenticationToken);
        }
    }

    private static class CustomAuthenticationProcessingFilterRequestMatcher implements RequestMatcher {
        private JWTService jwtService;

        CustomAuthenticationProcessingFilterRequestMatcher(JWTService jwtService) {
            this.jwtService = jwtService;
        }

        public boolean matches(HttpServletRequest request) {
            String authorizationHeaderValue = StringUtils.defaultIfBlank(request.getHeader("Authorization"), request.getParameter(OAuth2Constants.ACCESS_TOKEN));
            if (StringUtils.isBlank(authorizationHeaderValue)) {
                return false;
            }
            JWTClaimsSet jwtClaimsSet = null;
            Map<String, Object> claims = null;
            try {
                jwtClaimsSet = jwtService.decodeToken(authorizationHeaderValue);
                claims = jwtClaimsSet.getClaims();
                System.out.println("can custom auth ====> true");
                return true;
            } catch (Exception e) {

            }
            return false;
        }
    }
}
