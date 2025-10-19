package com.autotax.dao;

import com.autotax.domain.GenericStatusConstant;
import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalAccountMembership;
import com.autotax.domain.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PortalAccountMembershipRepository  extends JpaRepository<PortalAccountMembership, Long> {
    @Query("select m FROM PortalAccountMembership m WHERE m.portalAccount=?1 AND m.portalUser=?2 AND m.status=?3")
    Optional<PortalAccountMembership> findMembership(PortalAccount portalAccount, PortalUser portalUser, GenericStatusConstant statusConstant);

    // Adding a derived query method for convenience, as used in the service impl
    Optional<PortalAccountMembership> findByPortalAccountAndPortalUserAndStatus(PortalAccount portalAccount, PortalUser portalUser, GenericStatusConstant status);
    Optional<PortalAccountMembership> findByPortalAccountAndPortalUser(PortalAccount portalAccount, PortalUser portalUser);
    List<PortalAccountMembership> findByPortalUser(PortalUser portalUser);
}
