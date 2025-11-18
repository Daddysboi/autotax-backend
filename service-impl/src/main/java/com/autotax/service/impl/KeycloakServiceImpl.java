package com.autotax.service.impl;

import com.autotax.domain.*;
import com.autotax.domain.dto.PortalAccountDto;
import com.autotax.integration.exception.ErrorResponse;
import com.autotax.service.KeycloakService;
import com.autotax.auth.Scope;
import com.autotax.dao.AppRepository;
import com.autotax.dao.PortalAccountTypeRoleRepository;
import com.autotax.dao.PortalUserRepository;
import com.autotax.dao.SettingRepository;
import com.autotax.domain.constants.SystemRole;
import com.autotax.domain.dto.PortalUserDto;
import com.autotax.domain.dto.SignUpResponse;
import com.autotax.domain.dto.AuthUserDto;
import com.autotax.service.ActivityLogService;
import com.autotax.domain.constants.GenericStatusConstant;
import com.autotax.domain.constants.PortalAccountTypeConstant;
import com.autotax.principal.RequestPrincipal;
import com.autotax.domain.service.KeycloakConfigurationProperties;

import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.*;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloakAdmin;
    @Autowired
    private KeycloakConfigurationProperties keycloakConfigurationProperties;
    @Autowired
    private KeycloakSpringBootProperties keycloakSpringBootProperties;

    private Configuration configuration;

    private RequestPrincipal requestPrincipal;

    private final PortalUserRepository portalUserRepository;

    private final PortalAccountTypeRoleRepository portalAccountTypeRoleRepository;

    private final AppRepository appRepository;

    private final TransactionTemplate transactionTemplate;
    private final SettingRepository settingRepository;


    @PostConstruct
    public void init() {
        configuration = new Configuration(keycloakSpringBootProperties.getAuthServerUrl(), keycloakSpringBootProperties.getRealm(), keycloakSpringBootProperties.getResource(), keycloakSpringBootProperties.getCredentials(), null);
    }

    @Override
    public AuthUserDto createNewUser(PortalUserDto user) {
        return createNewUser(user, true);
    }

    @Override
    public AuthUserDto createNewUser(PortalUserDto user, boolean isEnabled) {
        if (StringUtils.isBlank(user.getUsername())) {
            throw new ErrorResponse(400, "Username not specified");
        }
        log.info("creating new users= ===");
        List<UserRepresentation> userRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().search(user.getUsername(), true);
        if (StringUtils.isNotBlank(user.getEmail())) {
            userRepresentations.addAll(keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().search(null, null, null, user.getEmail(), 0, 1));
        }
        if (userRepresentations.isEmpty()) {
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setUsername(user.getUsername());
            userRepresentation.setEnabled(isEnabled);
            userRepresentation.setEmailVerified(false);

//            for mobile/phone number
            userRepresentation.setAttributes(Collections.singletonMap("phoneNumber", Collections.singletonList(user.getPhoneNumber())));

            Response response = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().create(userRepresentation);
            log.info("=========keycloak response===========> {}", response);
            if (response == null) {
                return null;
            }

            System.out.printf("Repsonse: %s %s%n", response.getStatus(), response.getStatusInfo());
            if (response.getStatus() > 300) {
                return null;
            }
            String userId = CreatedResponseUtil.getCreatedId(response);
            UserResource userResource = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().get(userId);
            log.info("====> creating user {} with password {}", user.getUsername(), user.getPassword());
            if (StringUtils.isNotBlank(user.getPassword())) {
                CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                credentialRepresentation.setValue(user.getPassword());
                credentialRepresentation.setTemporary(false);
                userResource.resetPassword(credentialRepresentation);
            }
            UserRepresentation userRep = userResource.toRepresentation();
            AuthUserDto authUserDto = new AuthUserDto();
            authUserDto.setId(userRep.getId());
            authUserDto.setUserName(userRep.getUsername());
            return authUserDto;
        }

        UserRepresentation userRepresentation = userRepresentations.get(0);
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(userRepresentation.getId());
        authUserDto.setUserName(userRepresentation.getUsername());
        return authUserDto;
    }

    @Override
    public void logout(PortalUser portalUser) {
        keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().get(portalUser.getUserId()).logout();
    }


    @Override
    public AuthUserDto getUserByUsername(String username) {
        List<UserRepresentation> userRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().search(username, true);
        if (userRepresentations.isEmpty()) {
            return null;
        }
        UserRepresentation userRepresentation = userRepresentations.get(0);
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(userRepresentation.getId());
        authUserDto.setUserName(userRepresentation.getUsername());
        authUserDto.setFirstName(userRepresentation.getFirstName());
        authUserDto.setLastName(userRepresentation.getLastName());
        return authUserDto;
    }

    @Override
    public void createDefaultScopes() {
        log.info("====> creating client scopes");
        List<ClientScopeRepresentation> clientScopeRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clientScopes().findAll();
        for (Scope value : Scope.values()) {
            if (clientScopeRepresentations.stream().noneMatch(clientScopeRepresentation -> clientScopeRepresentation.getName().equalsIgnoreCase(value.getCode()))) {
                ClientScopeRepresentation clientScopeRepresentation = new ClientScopeRepresentation();
                clientScopeRepresentation.setName(value.getCode());
                clientScopeRepresentation.setDescription(value.getDescription());
                clientScopeRepresentation.setProtocol("openid-connect");
                keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clientScopes().create(clientScopeRepresentation);
            }
        }
    }

    @Override
    public SignUpResponse createNewUser(PortalUser portalUser, String password) {
        PortalUserDto newUserDto = new PortalUserDto();
        newUserDto.setPassword(password);
        newUserDto.setEmail(portalUser.getEmail());
        newUserDto.setPhoneNumber(portalUser.getPhoneNumber());
        newUserDto.setFirstName(portalUser.getFirstName());
        newUserDto.setLastName(portalUser.getLastName());
        newUserDto.setUsername(portalUser.getUsername());
//        newUserDto.setGender(portalUser.getGender());
        AuthUserDto newUser = null;
        try {
            newUser = createNewUser(newUserDto);
        } catch (Exception e) {
            log.info("keycloak error ==> {}", e.getMessage());
            e.printStackTrace();
        }
        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setUserId(newUser != null ? newUser.getId() : null);
        signUpResponse.setAuthToken(null);
        signUpResponse.setPortalUser(portalUser);
        return signUpResponse;
    }

    @Override
    public void updateUserDetails(PortalUser user) {
        List<UserRepresentation> userRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().search(user.getUsername(), true);
        if (userRepresentations.isEmpty()) {
            throw new ErrorResponse(400, "User not found");
        }
        UserRepresentation userRepresentation = userRepresentations.get(0);
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());

//        phone number
        userRepresentation.setAttributes(Collections.singletonMap("phoneNumber", Collections.singletonList(user.getPhoneNumber())));
        keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().get(userRepresentation.getId()).update(userRepresentation);


    }

    @Override
    public String generateAccessToken(String username, String password) {
        try {
            return AuthzClient.create(configuration).obtainAccessToken(username, password).getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ErrorResponse(400, "Invalid username or password");
    }

    @Override
    public String generateAccessTokenForUser(String username, String password, String clientId, String clientSecret) {
        try {
            Map<String, Object> credentials = new HashMap<>();
            credentials.put("secret", clientSecret);
            Configuration configuration = new Configuration(keycloakSpringBootProperties.getAuthServerUrl(), keycloakSpringBootProperties.getRealm(), clientId, credentials, null);
            log.info("===> creds {} :: {}", clientId, clientSecret);
            return AuthzClient.create(configuration).obtainAccessToken(username, password).getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ErrorResponse(400, "Invalid username or password");
    }

    @Override
    public String generateAccessTokenForClient(String clientId, String clientSecret) {
        try {
            Map<String, Object> credentials = new HashMap<>();
            credentials.put("secret", clientSecret);
            Configuration configuration = new Configuration(keycloakSpringBootProperties.getAuthServerUrl(), keycloakSpringBootProperties.getRealm(), clientId, credentials, null);
            return AuthzClient.create(configuration).obtainAccessToken().getToken();//.obtainAccessToken().getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ErrorResponse(400, "Invalid credentials");
    }

    @Override
    public String generateAccessToken() {
        try {
            return AuthzClient.create(configuration).obtainAccessToken().getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ErrorResponse(400, "Invalid credentials");
    }


    public static class TokenResponse {
        @SerializedName("access_token")
        public String accessToken;
    }

    @Override
    public void createPortalAccountClient(PortalAccount portalAccount) {
        List<ClientRepresentation> clientRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().findByClientId(portalAccount.getCode());
        if (clientRepresentations.isEmpty()) {
            ClientRepresentation clientRepresentation = new ClientRepresentation();
            clientRepresentation.setClientId(portalAccount.getCode());
            clientRepresentation.setName(portalAccount.getName());
//            clientRepresentation.setAuthorizationServicesEnabled(true);
            clientRepresentation.setServiceAccountsEnabled(true);
            clientRepresentation.setStandardFlowEnabled(false);
            clientRepresentation.setProtocol("openid-connect");
            clientRepresentation.setFullScopeAllowed(true);
            clientRepresentation.setDirectAccessGrantsEnabled(false);
            clientRepresentation.setEnabled(true);
            clientRepresentation.setImplicitFlowEnabled(false);
            clientRepresentation.setPublicClient(false);
            clientRepresentation.setBearerOnly(false);

            HashMap<String, String> attributes = new HashMap<>();
            attributes.put("clientType", PortalAccount.class.getSimpleName());
            attributes.put("status", GenericStatusConstant.ACTIVE.name());
            attributes.put("accountType", portalAccount.getType().name());
            if (requestPrincipal != null && requestPrincipal.getClientId() != null) {
                attributes.put("createdByUserId", requestPrincipal.getUserId());
            }

            clientRepresentation.setAttributes(attributes);

            try (Response response = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().create(clientRepresentation)) {
                if (response.getStatus() == 200 || response.getStatus() == 201) {
                    clientRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().findByClientId(portalAccount.getCode());
                    if (clientRepresentations.isEmpty()) {
                        throw new IllegalArgumentException(String.format("Unable to retrieve Portal Account Client with code" + portalAccount.getCode()));
                    }
                } else {
                    throw new IllegalArgumentException("Unable to create Portal Account Client with code " + portalAccount.getCode());
                }

            }
        }
        if (portalAccount.getType() == PortalAccountTypeConstant.SYSTEM) {
            checkRoles(portalAccount.getCode(), Arrays.stream(SystemRole.values()).map(supportRole -> supportRole.roleName()).collect(Collectors.toList()));
        }
    }

    @Override
    public List<PortalAccountDto> fetchAllPortalAccount() {
        List<ClientRepresentation> clientRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().findAll();
        List<ClientRepresentation> activePortalAccountClients = new ArrayList<>();
        for (ClientRepresentation clientRepresentation : clientRepresentations) {
            if (clientRepresentation.getAttributes() != null
                    && clientRepresentation.getAttributes().containsKey("clientType")
                    && clientRepresentation.getAttributes().get("clientType").equals(PortalAccount.class.getSimpleName())
                    && clientRepresentation.getAttributes().containsKey("status")
                    && clientRepresentation.getAttributes().get("status").equals(GenericStatusConstant.ACTIVE.name())) {
                activePortalAccountClients.add(clientRepresentation);
            }
        }
        return activePortalAccountClients.stream().map(clientRepresentation -> {
            PortalAccountDto portalAccountDto = new PortalAccountDto();
            portalAccountDto.setName(clientRepresentation.getName());
            portalAccountDto.setType(PortalAccountTypeConstant.valueOf(clientRepresentation.getAttributes().get("accountType")));
            portalAccountDto.setEmail(clientRepresentation.getAttributes().get("email"));
            portalAccountDto.setPhoneNumber(clientRepresentation.getAttributes().get("phoneNumber"));
            portalAccountDto.setAltPhoneNumber("");
            portalAccountDto.setCode(clientRepresentation.getClientId());
            return portalAccountDto;
        }).collect(Collectors.toList());
    }

    private void checkRoles(String accountCode, List<String> roles) {

        ClientRepresentation portalAccountRepresentation = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().findByClientId(accountCode).get(0);
        ClientResource portalAccountClient = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().get(portalAccountRepresentation.getId());

        List<String> savedRoleNames = portalAccountClient.roles().list().stream().map(RoleRepresentation::getName).collect(Collectors.toList());

        for (String role : roles) {
            if (!savedRoleNames.contains(role)) {
                createRolesForClient(role, portalAccountClient);
            } else {
                log.info("Role {} exists for {}", role, portalAccountClient.toRepresentation().getName());
            }

        }
    }

    @Override
    public void deactivateUser(String userId) {
        UserResource userResource = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(false);
        userResource.update(userRepresentation);
    }

    @Override
    public void activateUser(String userId) {
        UserResource userResource = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(true);
        userResource.update(userRepresentation);
    }

    private RoleRepresentation createRolesForClient(String roleName, ClientResource clientResource) {
        RoleRepresentation newRole = new RoleRepresentation();
        newRole.setName(roleName);
        HashMap<String, List<String>> attributes = new HashMap<>();
        attributes.put("status", Collections.singletonList(GenericStatusConstant.ACTIVE.name()));
        attributes.put("application", Collections.singletonList(clientResource.toRepresentation().getAttributes().get("application")));
        if (requestPrincipal != null && requestPrincipal.getClientId() != null) {
            attributes.put("createdByUserId", Collections.singletonList(requestPrincipal.getUserId()));
        }

        newRole.setAttributes(attributes);

        try {
            clientResource.roles().create(newRole);
            return newRole;
        } catch (Exception e) {
            if (e instanceof ClientErrorException && e.getMessage().contains("409")) {
                log.info("Role {} exists already", newRole.getName());
            } else {
                log.warn(e.getCause().getMessage());
            }
        }
        return null;
    }

    private List<ClientRepresentation> fetchAllPortalAccountForApplication(String applicationName) {
        List<ClientRepresentation> clientRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().findAll();
        List<ClientRepresentation> activePortalAccountClients = new ArrayList<>();
        for (ClientRepresentation clientRepresentation : clientRepresentations) {
            if (clientRepresentation.getAttributes() != null
                    && clientRepresentation.getAttributes().containsKey("clientType")
                    && clientRepresentation.getAttributes().get("clientType").equals(PortalAccount.class.getSimpleName())
                    && clientRepresentation.getAttributes().containsKey("status")
                    && clientRepresentation.getAttributes().get("status").equals(GenericStatusConstant.ACTIVE.name())
                    && clientRepresentation.getAttributes().containsKey("application")
                    && clientRepresentation.getAttributes().get("application").equals(applicationName)) {
                activePortalAccountClients.add(clientRepresentation);
            }
        }
        return activePortalAccountClients;
    }

    @Override
    public void createRoleForAccountType(PortalAccountTypeConstant accountType, List<String> roles) {
        List<ClientRepresentation> clientRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().findAll();
        for (ClientRepresentation clientRepresentation : clientRepresentations) {
            if (clientRepresentation.getAttributes() != null
                    && PortalAccount.class.getSimpleName().equals(clientRepresentation.getAttributes().get("clientType"))
                    && "CFS".equals(clientRepresentation.getAttributes().get("application"))
                    && GenericStatusConstant.ACTIVE.name().equals(clientRepresentation.getAttributes().get("status"))) {
                if (accountType.name().equals(clientRepresentation.getAttributes().get("accountType"))) {
                    checkRoles(clientRepresentation.getClientId(), roles);
                }
            }
        }
    }

    @Override
    public void checkKeycloakEventLogin() {
        List<String> eventTypes = new ArrayList<>();
        eventTypes.add("LOGIN");

        LocalDateTime endTime = LocalDateTime.now();

        Optional<Setting> optionalSetting = settingRepository.findSettingByName("LOGIN_EVENT_CRON_LAST_RUN");

//        Long lastRunEpoch = optionalSetting.map(setting -> LocalDateTime.parse(setting.getValue()))
//                .map(localDateTime1 -> ZonedDateTime.of(localDateTime1, ZoneId.systemDefault()).toEpochSecond())
//                .orElse(0L);

        String startDate = optionalSetting.map(setting -> LocalDateTime.parse(setting.getValue()).toLocalDate())
                .map(localDate -> localDate.toString()).orElse(null);

        List<String> externalReferenceIds = appRepository.startJPAQuery(QActivityLog.activityLog)
                .where(QActivityLog.activityLog.createdAt.loe(endTime.toLocalDate().atTime(LocalTime.MAX)))
                .where(QActivityLog.activityLog.createdAt.goe(optionalSetting.map(setting -> LocalDateTime.parse(setting.getValue()).toLocalDate())
                        .map(LocalDate::atStartOfDay).orElse(LocalDateTime.of(1970, 1, 1, 0, 0))))
                .where(QActivityLog.activityLog.externalReferenceId.isNotNull())
                .select(QActivityLog.activityLog.externalReferenceId)
                .fetch();

        List<EventRepresentation> eventRepresentations = keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).getEvents(eventTypes, keycloakConfigurationProperties.getFrontendClientId(),
                null, startDate, endTime.toLocalDate().plusDays(1).toString(), null, null, null);


        List<String> eventUserIds = eventRepresentations.stream().map(EventRepresentation::getUserId).collect(Collectors.toList());

        List<PortalUser> portalUsers = !eventUserIds.isEmpty() ? appRepository.startJPAQuery(QPortalUser.portalUser)
                .where(QPortalUser.portalUser.userId.in(eventUserIds)).fetch() : Collections.emptyList();

        eventRepresentations.stream()
                .filter(eventRepresentation -> externalReferenceIds.stream().noneMatch(s -> eventRepresentation.getSessionId().equals(s)))
                .forEach(eventRepresentation -> {
                    try {
                        transactionTemplate.executeWithoutResult(transactionStatus -> {
                            portalUsers.stream().filter(portalUser1 -> portalUser1.getUserId().equalsIgnoreCase(eventRepresentation.getUserId())).findFirst().ifPresent(portalUser -> {
                                //activityLogService.logActivity(ActivityLogActionType.LOGIN, PortalUser.class, portalUser.getId(), portalUser, eventRepresentation.getIpAddress(), portalUser, getDateFromEpochTimestamp(eventRepresentation.getTime()), eventRepresentation.getSessionId());
                            });
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    private LocalDateTime getDateFromEpochTimestamp(long timeStamp) {
        LocalDateTime triggerTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault());

        return triggerTime;
    }

    @Override
    public void testConnection() {
        keycloakAdmin.realm(keycloakSpringBootProperties.getRealm()).clients().findAll();
    }

    @Override
    public String generateAccessTokenForClient(String authServerUrl, String realm, String clientId, String
            clientSecret) {
        try {
            Map<String, Object> credentials = new HashMap<>();
            credentials.put("secret", clientSecret);
            Configuration configuration = new Configuration(authServerUrl, realm, clientId, credentials, null);
            return AuthzClient.create(configuration).obtainAccessToken().getToken();//.obtainAccessToken().getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ErrorResponse(400, "Invalid credentials");
    }

    private LocalDateTime getDateTimeFromTimestamp(long timestamp) {
        if (timestamp == 0)
            return null;
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone
                .getDefault().toZoneId());
    }
}
