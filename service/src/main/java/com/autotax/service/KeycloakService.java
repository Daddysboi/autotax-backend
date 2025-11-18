package com.autotax.service;

import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalUser;
import com.autotax.domain.constants.PortalAccountTypeConstant;
import com.autotax.domain.dto.AuthUserDto;
import com.autotax.domain.dto.PortalAccountDto;
import com.autotax.domain.dto.PortalUserDto;
import com.autotax.domain.dto.SignUpResponse;

import java.util.List;

public interface KeycloakService {

    AuthUserDto createNewUser(PortalUserDto user);

    AuthUserDto createNewUser(PortalUserDto user, boolean isEnabled);

    void logout(PortalUser portalUser);

    AuthUserDto getUserByUsername(String username);

    void createDefaultScopes();

    SignUpResponse createNewUser(PortalUser portalUser, String password);

    void updateUserDetails(PortalUser user);

    String generateAccessToken(String username, String password);

    String generateAccessTokenForUser(String username, String password, String clientId, String clientSecret);

    String generateAccessTokenForClient(String clientId, String clientSecret);

    String generateAccessToken();

    void createPortalAccountClient(PortalAccount portalAccount);

    List<PortalAccountDto> fetchAllPortalAccount();

    void deactivateUser(String userId);

    void activateUser(String userId);

    void createRoleForAccountType(PortalAccountTypeConstant accountType, List<String> roles);

    void checkKeycloakEventLogin();

    void testConnection();

    String generateAccessTokenForClient(String authServerUrl, String realm, String clientId, String clientSecret);
}