package com.autotax.infrastructure.configuration.security;

import com.autotax.domain.PortalUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2021
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomAuthenticationUserPrincipal implements Principal {

    private String ipAddress;
    private String authHeader;
    private PortalUser portalUser;

    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public String getName() {
        return getPortalUser().getDisplayName();
    }
}
