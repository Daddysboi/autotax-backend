package com.autotax.dao;

import com.autotax.domain.PortalUser;
import com.autotax.domain.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long>, QuerydslPredicateExecutor<UserPermission> {
    // TODO: Add specific query methods as needed
    List<UserPermission> findAllByPortalUser(PortalUser portalUser);
}
