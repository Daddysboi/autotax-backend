package com.autotax.service;

import com.autotax.domain.dto.EditUserDto;
import com.autotax.domain.dto.NewUserDto;
import com.autotax.domain.dto.RolePermissionHolder;
import com.autotax.domain.dto.UpdateProfileDto;
import com.autotax.domain.dto.AddRolesAndPrivilegesDto;
import com.autotax.domain.pojo.NameIdPojo;
import com.autotax.domain.pojo.PortalUserPojo;
import com.autotax.domain.pojo.ProfilePojo;
import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalAccountTypeRole;
import com.autotax.domain.PortalUser;
import com.autotax.domain.TermsOfUse;
import com.autotax.domain.constants.AbsenceReasonConstant;
import com.autotax.domain.constants.PermissionTypeConstant;
import com.autotax.domain.constants.PortalAccountTypeConstant;
import com.mysema.commons.lang.Pair;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public interface UserService {
}
