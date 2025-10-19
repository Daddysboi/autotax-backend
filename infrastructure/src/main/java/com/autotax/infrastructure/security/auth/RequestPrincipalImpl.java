package com.autotax.infrastructure.security.auth;

import com.autotax.dao.AppRepository;
import com.autotax.dao.PortalUserRepository;
import com.autotax.domain.Platform;
import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalAccountTypeRole;
import com.autotax.domain.PortalUser;
import com.autotax.domain.pojo.AccountMembershipPojo;
import com.autotax.domain.principal.RequestPrincipal;
import com.autotax.infrastructure.configuration.security.CustomAuthenticationToken;
import com.autotax.infrastructure.configuration.security.CustomAuthenticationUserPrincipal;
import com.autotax.service.PortalAccountMembershipService;
import com.autotax.dao.PortalAccountMembershipRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest; // Corrected import

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Olaleye Afolabi <oafolabi@byteworks.com.ng>
 */
@RequestScope
@Service
@Slf4j
public class RequestPrincipalImpl implements RequestPrincipal {
    private final String ipAddress;
    private final String userAgent;
    private String idTokenString;
    private String userId;
    private String username;
    private String clientId;
    private String scope;
    private PortalUser portalUser;
    private String accountCode;
    private Long platformId;
    private PrincipalType principalType;
    private String sessionId;
    private PortalAccount portalAccount;

    // Injected dependencies
    private final PortalUserRepository portalUserRepository;
    private final AppRepository appRepository;
    private final PortalAccountMembershipRepository membershipRepository;
    private final PortalAccountMembershipService membershipService;

    private List<AccountMembershipPojo> accountMembership;

    @Autowired
    public RequestPrincipalImpl(HttpServletRequest request,
                                PortalUserRepository portalUserRepository,
                                PortalAccountMembershipRepository membershipRepository,
                                AppRepository appRepository,
                                PortalAccountMembershipService membershipService) {
        this.appRepository = appRepository;
        this.membershipService = membershipService;
        this.portalUserRepository = portalUserRepository;
        this.membershipRepository = membershipRepository;

        Principal principal = request.getUserPrincipal();
        this.ipAddress = StringUtils.defaultIfBlank(request.getHeader("X-FORWARDED-FOR"), request.getRemoteAddr());
        this.accountCode = StringUtils.defaultIfBlank(request.getHeader("X-ACCOUNT-CODE"), request.getParameter("xAccountCode"));
        this.userAgent = StringUtils.defaultIfBlank(request.getHeader("X-USER-AGENT"), request.getParameter("xUserAgent"));
        this.platformId = StringUtils.isNotBlank(request.getHeader("X-PLATFORM-ID")) ? Long.parseLong(request.getHeader("X-PLATFORM-ID")) : null;

        if (principal == null) {
            this.principalType = PrincipalType.GUEST;
        } else {
            // TODO: Re-implement Keycloak binding using Spring Security 6 OAuth2/OIDC mechanisms
            // For now, assume CustomAuthenticationToken is the primary authentication method
            if (principal instanceof CustomAuthenticationToken) {
                bindForCustomAuth((CustomAuthenticationToken) principal);
            } else {
                // Handle other principal types or throw an exception
                this.principalType = PrincipalType.GUEST; // Default to guest if not custom token
            }
        }
    }

    @Override
    public String getAccessToken() {
        return this.idTokenString;
    }

    @Override
    public String getUserName() {
        return this.username;
    }

    @Override
    public String getUserId() {
        if (principalType == PrincipalType.GUEST) {
            return null;
        }
        return this.userId;
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public boolean isAuthenticated() {
        return this.principalType != PrincipalType.GUEST;
    }

    @Override
    public boolean hasAnyScope(String... scopes) {
        return Arrays.stream(scopes).anyMatch(this::hasScope);
    }

    @Override
    public boolean hasScope(String scope) {
        String scopeInToken = getScope();
        System.out.println("{{{{{{ " + scopeInToken);
        if (StringUtils.isBlank(scopeInToken)) {
            return false;
        }
        return Arrays.asList(scopeInToken.split(" ")).contains(scope);
    }

    @Override
    public PortalUser getPortalUser() {
        if (principalType == PrincipalType.GUEST) {
            return null;
        }
        if (this.portalUser == null) {
            // TODO: Implement actual user retrieval from portalUserRepository
            this.portalUser = portalUserRepository.findByUserId(getUserId()).orElse(null);
        }
        return this.portalUser;
    }

    @Override
    public PortalAccount getCurrentPortalAccount() {
        if (principalType == PrincipalType.GUEST) {
            return null;
        }
        if (portalAccount == null) {
            // TODO: Re-implement QueryDSL logic for PortalAccount retrieval
            // For now, returning null or a dummy account
            // JPAQuery<PortalAccount> portalAccountJPAQuery = appRepository.startJPAQuery(QPortalAccountMembership.portalAccountMembership)
            //         .where(QPortalAccountMembership.portalAccountMembership.portalUser.eq(getPortalUser()))
            //         .where(QPortalAccountMembership.portalAccountMembership.status.eq(GenericStatusConstant.ACTIVE))
            //         .select(QPortalAccountMembership.portalAccountMembership.portalAccount);
            // if (StringUtils.isNotBlank(accountCode)) {
            //     portalAccountJPAQuery.where(QPortalAccountMembership.portalAccountMembership.portalAccount.code.equalsIgnoreCase(accountCode));
            // }
            // this.portalAccount = portalAccountJPAQuery.limit(1).fetchOne();
            return null;
        }
        return portalAccount;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    // Removed bindForKeycloak method - incompatible with Spring Boot 3.x

    @Override
    public List<AccountMembershipPojo> getAccountPermissions() {
        if (principalType == PrincipalType.GUEST) {
            return Collections.emptyList(); // Return empty list instead of null
        }
        PortalUser user = getPortalUser();
        if (user == null) {
            return Collections.emptyList();
        }
        if (accountMembership == null) {
            // TODO: Implement actual account membership retrieval using membershipService
            accountMembership = membershipService.getAllMemberships(user); // Assuming this method exists and works
        }
        return accountMembership;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    private void bindForCustomAuth(CustomAuthenticationToken principal) {
        CustomAuthenticationUserPrincipal p = (CustomAuthenticationUserPrincipal) principal.getPrincipal();
        PortalUser user = p.getPortalUser();
        this.userId = user.getUserId();
        this.clientId = null; // Or retrieve from JWTClaimsSet if available in CustomAuthenticationToken
        this.scope = ""; // Or retrieve from JWTClaimsSet
        this.idTokenString = p.getAuthHeader();
        this.username = user.getUsername();
        if (StringUtils.isNotBlank(getClientId())) {
            this.principalType = PrincipalType.CLIENT;
        } else {
            this.principalType = PrincipalType.USER;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public List<PortalAccountTypeRole> getCurrentRoles() {
        if (principalType == PrincipalType.GUEST) {
            return Collections.emptyList(); // Return empty list instead of null
        }
        if (getCurrentPortalAccount() == null) {
            return Collections.emptyList();
        }
        // TODO: Re-implement QueryDSL logic for PortalAccountTypeRole retrieval
        // return appRepository.startJPAQuery(QPortalAccountMemberRole.portalAccountMemberRole)
        //         .where(QPortalAccountMemberRole.portalAccountMemberRole.membership.portalUser.eq(getPortalUser()))
        //         .where(QPortalAccountMemberRole.portalAccountMemberRole.status.eq(GenericStatusConstant.ACTIVE))
        //         .where(QPortalAccountMemberRole.portalAccountMemberRole.membership.status.eq(GenericStatusConstant.ACTIVE))
        //         .where(QPortalAccountMemberRole.portalAccountMemberRole.membership.portalAccount.eq(getCurrentPortalAccount()))
        //         .select(QPortalAccountMemberRole.portalAccountMemberRole.role).fetch();
        return Collections.emptyList(); // Placeholder
    }
}
