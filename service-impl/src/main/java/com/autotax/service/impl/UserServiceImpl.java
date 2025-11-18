package com.autotax.service.impl;

import com.autotax.dao.PortalUserRepository;
import com.autotax.dao.RolePermissionRepository;
import com.autotax.dao.UserPermissionRepository;
import com.autotax.domain.PortalUser;

import com.autotax.domain.dto.UpdateProfileDto;
import com.autotax.domain.pojo.ProfilePojo;

import com.autotax.principal.RequestPrincipal;
import com.autotax.integration.service.EmailNotificationService;
import com.autotax.service.FileService;
import com.autotax.service.PasswordService;
import com.autotax.service.PortalAccountMembershipService;
import com.autotax.service.UserService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {



    public PortalUser updateUserProfile(UpdateProfileDto data) {
        return null;
    }


    public void updateUserBasicInfo(PortalUser user, UpdateProfileDto data) {

    }

    public void updateAddress(PortalUser user, UpdateProfileDto data) {

    }

    public ProfilePojo getUserProfileDetails() {
        log.debug("Fetching user profile details");
        return null;
    }


    private void updateNextOfKin(PortalUser user, UpdateProfileDto data) {
        log.debug("Checking for existing next of kin details");
    }

    public String getRolesString(Map<String, Set<String>> roles) {
        return roles.values().stream()
                .flatMap(Set::stream)
                .map(role -> role.replaceAll("_", " "))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            if (list.size() > 1) {
                                String last = list.remove(list.size() - 1);
                                return String.join(", ", list) + " and " + last;
                            } else if (list.size() == 1) {
                                return list.get(0);
                            } else {
                                return "";
                            }
                        }
                ));
    }
}
