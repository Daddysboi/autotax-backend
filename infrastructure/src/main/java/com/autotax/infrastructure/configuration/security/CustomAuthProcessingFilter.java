package com.autotax.infrastructure.configuration.security;

import com.autotax.domain.PortalUser;
import com.autotax.service.JWTService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
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
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    // TODO: PortalUserRepository and AppRepository need to be re-implemented or replaced
    // private PortalUserRepository portalUserRepository;
    // private AppRepository appRepository;

    public CustomAuthProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager, JWTService jwtService, ApplicationEventPublisher eventPublisher) {
        super(requiresAuthenticationRequestMatcher);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.eventPublisher = eventPublisher;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        // adapterDeploymentContext = applicationContext.getBean(AdapterDeploymentContext.class); // Keycloak specific
        // bind(); // Keycloak specific
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

            // TODO: Replace with actual PortalUser retrieval logic for autotax project
            // You will need to inject PortalUserRepository and use it here.
            // Example: Optional<PortalUser> optionalPortalUser = portalUserRepository.findByUserId(jwtClaimsSet.getSubject());
            // PortalUser portalUser = optionalPortalUser.orElseThrow(() -> new BadCredentialsException("User not found"));
            // .where(QPortalUser.portalUser.status.eq(GenericStatusConstant.ACTIVE))

            // Placeholder for now, you'll need to implement actual user retrieval
            PortalUser portalUser = new PortalUser(); // Dummy user
            portalUser.setUserId(jwtClaimsSet.getSubject()); // Set user ID from JWT
            portalUser.setDisplayName(jwtClaimsSet.getStringClaim("name")); // Example claim

            CustomAuthenticationUserPrincipal etaxUserPrincipal = new CustomAuthenticationUserPrincipal(ipAddress, authHeader, portalUser);
            context.setAuthentication(new CustomAuthenticationToken(etaxUserPrincipal, jwtClaimsSet));

            SecurityContextHolder.setContext(context);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Assert.notNull(authentication, "Authentication SecurityContextHolder was null");
            logger.info("=====> custom authing ");
            return authenticationManager.authenticate(authentication);
        } catch (JOSEException e) { // Removed IOException and ServletException
            e.printStackTrace();
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("An unexpected error occurred");
        }
    }

    private void bind() {
        // Keycloak specific binding removed.
        // JWTService is now injected via constructor.
        // TODO: Re-implement or replace AppRepository and PortalUserRepository binding if needed.
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
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
