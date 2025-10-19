package com.autotax.infrastructure.configuration.security;

import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.ArrayList;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2021
 **/
public class CustomAuthenticationToken extends AbstractAuthenticationToken implements Authentication {

    private Principal principal;
    private JWTClaimsSet jwtClaimsSet;

    public CustomAuthenticationToken(CustomAuthenticationUserPrincipal etaxUserPrincipal, JWTClaimsSet jwtClaimsSet) {
        super(new ArrayList<>());
        this.principal = etaxUserPrincipal;
        this.jwtClaimsSet = jwtClaimsSet;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return ((CustomAuthenticationUserPrincipal) this.principal).getPortalUser().getUserId();
    }

    @Override
    public Object getDetails() {
        return super.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public boolean isInteractive() {
        return false;
    }
}
