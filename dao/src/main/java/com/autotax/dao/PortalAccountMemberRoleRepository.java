package com.autotax.dao;

import com.autotax.domain.PortalAccountMemberRole;
import com.autotax.domain.PortalUser;
import com.autotax.domain.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PortalAccountMemberRoleRepository extends JpaRepository<PortalAccountMemberRole, Long>, QuerydslPredicateExecutor<PortalAccountMemberRole> {
    // TODO: Add specific query methods as needed
    List<PortalAccountMemberRole> findByPortalUserAndStatus(PortalUser portalUser, GenericStatusConstant status);
    List<PortalAccountMemberRole> findByPortalUser(PortalUser portalUser);
}
