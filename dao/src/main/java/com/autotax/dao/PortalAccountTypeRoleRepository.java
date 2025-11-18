package com.autotax.dao;

import com.autotax.domain.PortalAccountTypeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortalAccountTypeRoleRepository extends JpaRepository<PortalAccountTypeRole, Long> {
}
