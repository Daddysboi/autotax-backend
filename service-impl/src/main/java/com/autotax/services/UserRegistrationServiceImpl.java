package com.autotax.services;

import com.autotax.dao.UserRegistrationService;
import com.autotax.domain.PortalAccount;
import com.autotax.domain.constants.GenderConstant;
import com.autotax.domain.constants.SystemRole;
import org.springframework.stereotype.Service;


@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    @Override
    public boolean isUniqueEmail(String emailAddress, String s) {
        // Dummy implementation - always return true for testing
        return true;
    }

    @Override
    public PortalAccount createDefaultAdminUser(String firstName, String lastName, String email,
                                                GenderConstant genderConstant, String username,
                                                String phoneNumber, String password, SystemRole systemRole) {
        // Dummy implementation - return a mock PortalAccount
        PortalAccount adminUser = new PortalAccount();
        return adminUser;
    }
}
