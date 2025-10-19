package com.autotax.dao;

import com.autotax.domain.PortalAccountTypeRole;
import com.autotax.domain.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long>, QuerydslPredicateExecutor<RolePermission> {
    // TODO: Add specific query methods as needed
    List<RolePermission> findAllByPortalAccountTypeRoleIn(Collection<PortalAccountTypeRole> roles);
}
