package com.autotax.infrastructure.configuration.security;

import com.autotax.domain.PortalUser;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2021
 **/
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final Principal principal;
    private final JWTClaimsSet jwtClaimsSet;

    public CustomAuthenticationToken(CustomAuthenticationUserPrincipal etaxUserPrincipal, JWTClaimsSet jwtClaimsSet) {
        super(Collections.emptyList()); // No authorities initially
        this.principal = etaxUserPrincipal;
        this.jwtClaimsSet = jwtClaimsSet;
        setAuthenticated(true); // Mark as authenticated after setting authorities
    }

    public CustomAuthenticationToken(CustomAuthenticationUserPrincipal etaxUserPrincipal, JWTClaimsSet jwtClaimsSet, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = etaxUserPrincipal;
        this.jwtClaimsSet = jwtClaimsSet;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        // Assuming getPortalUser() is available on CustomAuthenticationUserPrincipal
        // and returns a non-null PortalUser
        if (principal instanceof CustomAuthenticationUserPrincipal) {
            PortalUser portalUser = ((CustomAuthenticationUserPrincipal) principal).getPortalUser();
            if (portalUser != null) {
                return portalUser.getUserId();
            }
        }
        return null; // Or throw an exception if credentials are always expected
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public JWTClaimsSet getJwtClaimsSet() {
        return jwtClaimsSet;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated && !super.isAuthenticated()) {
            // Only allow setting to authenticated if authorities were provided or it's already authenticated
            if (getAuthorities() == null || getAuthorities().isEmpty()) {
                throw new IllegalArgumentException(
                        "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
            }
        }
        super.setAuthenticated(isAuthenticated);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        // No mutable credentials to erase here, as jwtClaimsSet is final
    }
}
