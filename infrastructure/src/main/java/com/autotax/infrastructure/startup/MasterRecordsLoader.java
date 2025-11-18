package com.autotax.infrastructure.startup;

import com.autotax.dao.PortalUserRepository;
import com.autotax.dao.UserRegistrationService;
import com.autotax.domain.PortalAccount;
import com.autotax.domain.constants.GenderConstant;
import com.autotax.domain.constants.SystemRole;
import com.google.gson.Gson;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * @author Isa Umar
 * email: iumaar@icloud.com
 **/

@Slf4j
@RequiredArgsConstructor
@Named
@Profile("!test")
public class MasterRecordsLoader {
    private final Gson gson;
    private final PortalUserRepository portalUserRepository;
    private final UserRegistrationService userRegistrationService;
    private final Environment environment;

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void init() {
        try {
            if (environment.acceptsProfiles("dev")) {
                if (environment.acceptsProfiles("startup"))
                    loadData();
            } else {
                loadData();
            }
        } catch (IOException ioEx) {
            log.error(ioEx.getMessage());
        }
    }

    private void loadData() throws IOException {
        loadCountries();
        loadDefaultAccounts();
    }



    private void loadDefaultAccounts() {

        String adminUsername = "cfsadmin@byteworks.com.ng";
        String adminPassword = "$cf$@dmg1n#";
        String adminEmail = "cfsadmin@byteworks.com.ng";
        String adminFirstName = "CFS";
        String adminLastName = "Admin";
        String adminPhone = "+2348130631034";



        createAdminUser(adminUsername, adminPassword, adminEmail, adminFirstName, adminLastName, adminPhone);
    }



    private void createAdminUser(String username, String password, String email, String firstName, String lastName, String phoneNumber) {
        portalUserRepository.findByEmailOrUsername(email, username)
                .orElseGet(() -> {
                    log.info("===========CREATING ADMIN {} ============", username);
                    try {
                        PortalAccount portalAccount = userRegistrationService.createDefaultAdminUser(firstName, lastName, email, GenderConstant.OTHER, username, phoneNumber, password, SystemRole.ADMIN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }


    private void loadCountries() throws IOException {

    }



    private void loadStates() throws IOException {

    }


    private void loadLgas() throws IOException {

    }

}
