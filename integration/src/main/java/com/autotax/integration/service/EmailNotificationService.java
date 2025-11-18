package com.autotax.integration.service;

import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EmailNotificationService {
    void sendUserCreationEmail(PortalUser createdBy, PortalUser newUser, PortalAccount portalAccount, String roles);
    void sendUserUpdateEmail(PortalUser updatedBy, PortalUser updatedUser, LocalDateTime updateTime);
    void sendUserActivationNoticeEmail(PortalUser admin, PortalUser activatedUser, LocalDateTime activationTime);
    void sendUserDeactivationNoticeEmail(PortalUser admin, PortalUser deactivatedUser, LocalDateTime deactivationTime, String reason);
    void sendAddRemovePrivilegeEmail(PortalUser admin, PortalAccount portalAccount, List<String> recipients, List<String> addedPermissions, List<String> removedPermissions);
    // Add other methods as needed
}
