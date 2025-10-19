package com.autotax.infrastructure.configuration.security;


import com.bw.cfs.dao.AppRepository;
import com.bw.cfs.dao.PortalUserRepository;
import com.bw.cfs.entity.PortalUser;
import com.bw.cfs.entity.QPortalUser;
import com.bw.cfs.enumeration.GenericStatusConstant;
import com.bw.cfs.services.JWTService;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.authentication.RequestAuthenticatorFactory;
import org.keycloak.adapters.springsecurity.authentication.SpringSecurityRequestAuthenticatorFactory;
import org.keycloak.adapters.springsecurity.token.AdapterTokenStoreFactory;
import org.keycloak.adapters.springsecurity.token.SpringSecurityAdapterTokenStoreFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2021
 **/
public class CustomAuthProcessingFilter extends AbstractAuthenticationProcessingFilter implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private AdapterDeploymentContext adapterDeploymentContext;
    private AdapterTokenStoreFactory adapterTokenStoreFactory = new SpringSecurityAdapterTokenStoreFactory();
    private AuthenticationManager authenticationManager;
    private RequestAuthenticatorFactory requestAuthenticatorFactory = new SpringSecurityRequestAuthenticatorFactory();
    private JWTService jwtService;
    private PortalUserRepository portalUserRepository;
    private AppRepository appRepository;

    protected CustomAuthProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    protected CustomAuthProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected CustomAuthProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher);
        this.authenticationManager = authenticationManager;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        adapterDeploymentContext = applicationContext.getBean(AdapterDeploymentContext.class);
        bind();
        super.afterPropertiesSet();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        JWTClaimsSet jwtClaimsSet = null;
        Map<String, Object> claims = null;
        String authHeader = StringUtils.defaultIfBlank(request.getHeader("Authorization"), request.getParameter("access_token"));
        try {
            jwtClaimsSet = jwtService.decodeToken(authHeader);
            claims = jwtClaimsSet.getClaims();
            String ipAddress = StringUtils.defaultIfBlank(request.getHeader("X-FORWARDED-FOR"), request.getRemoteAddr());
            ipAddress = Arrays.stream(ipAddress.split(" *, *")).filter(StringUtils::isNotBlank).findFirst().orElse(null);
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            PortalUser portalUser = appRepository.startJPAQuery(QPortalUser.portalUser)
                    .where(QPortalUser.portalUser.userId.likeIgnoreCase(jwtClaimsSet.getSubject()))
                    .where(QPortalUser.portalUser.status.eq(GenericStatusConstant.ACTIVE))
                    .fetchFirst();
            if (portalUser == null) {
                throw new BadCredentialsException("Invalid credentials");
            }
            CustomAuthenticationUserPrincipal etaxUserPrincipal = new CustomAuthenticationUserPrincipal(ipAddress, authHeader, portalUser);
            context.setAuthentication(new CustomAuthenticationToken(etaxUserPrincipal, jwtClaimsSet));
            SecurityContextHolder.setContext(context);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Assert.notNull(authentication, "Authentication SecurityContextHolder was null");
            logger.info("=====> custom authing ");
            return authenticationManager.authenticate(authentication);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private void bind() {
        if (jwtService == null) {
            jwtService = applicationContext.getBean(JWTService.class);
        }
        if (appRepository == null) {
            appRepository = applicationContext.getBean(AppRepository.class);
        }

        if (portalUserRepository == null) {
            portalUserRepository = applicationContext.getBean(PortalUserRepository.class);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
//        if (authResult instanceof EtaxAuthenticationToken) {
//            super.successfulAuthentication(request, response, chain, authResult);
//            return;
//        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        try {
            // Fire event
            if (this.eventPublisher != null) {
                eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
            }
            chain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
