package com.autotax.service;

import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalUser;
import com.autotax.domain.pojo.AccountMembershipPojo;
import com.autotax.domain.PortalAccountMembership;

import java.util.List;

public interface PortalAccountMembershipService {

    PortalAccountMembership createMembership(PortalAccount portalAccount, PortalUser user);

    List<AccountMembershipPojo> getAllMemberships(PortalUser portalUser);

    PortalAccountMembership removeMembership(PortalAccountMembership portalAccountMembership);

    PortalAccountMembership createOrGetMembership(PortalAccount portalAccount, PortalUser user);

}
