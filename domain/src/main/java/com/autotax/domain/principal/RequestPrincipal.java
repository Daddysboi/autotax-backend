package com.autotax.domain.principal;

import com.autotax.domain.Platform;
import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalAccountTypeRole;
import com.autotax.domain.PortalUser;
import com.autotax.domain.pojo.AccountMembershipPojo;

import java.util.List;

public interface RequestPrincipal {

    String AUTH_TOKEN_NAME = "auth_token";

    String getUserId();

    default String getUserName() {
        return null;
    }

    boolean hasScope(String scope);


    String getScope();

    default String getIpAddress() {
        return null;
    }

    default String getAccessToken() {
        return null;
    }

    default PortalUser getPortalUser() {
        return null;
    }

    List<AccountMembershipPojo> getAccountPermissions();

    default PortalAccount getCurrentPortalAccount() {
        return null;
    }

    default List<PortalAccountTypeRole> getCurrentRoles() {
        return null;
    }

    default String getUserAgent() {
        return null;
    }

    default String getClientId() {

        return null;
    }

    default Platform getPlatform() {
        return null;
    }

    boolean isAuthenticated();

    boolean hasAnyScope(String... scopes);

    enum PrincipalType {
        GUEST, USER, CLIENT
    }

    enum ActorTypeConstant {
        PORTAL_USER,
        PORTAL_ACCOUNT,
        SYSTEM
    }
}
