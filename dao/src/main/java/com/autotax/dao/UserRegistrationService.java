package com.autotax.dao;


import com.autotax.domain.PortalAccount;
import com.autotax.domain.constants.GenderConstant;
import com.autotax.domain.constants.SystemRole;

public interface UserRegistrationService {


    boolean isUniqueEmail(String emailAddress, String s);

    PortalAccount createDefaultAdminUser(String firstName, String lastName, String email, GenderConstant genderConstant, String username, String phoneNumber, String password, SystemRole systemRole);
}
