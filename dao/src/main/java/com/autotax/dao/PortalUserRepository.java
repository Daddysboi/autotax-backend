package com.autotax.dao;

import com.autotax.domain.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

// Placeholder interface for PortalUserRepository
// You will need to define specific query methods here as needed.
public interface PortalUserRepository extends JpaRepository<PortalUser, Long>, QuerydslPredicateExecutor<PortalUser> {

    @Query("SELECT p FROM PortalUser p where lower(p.userId) = lower(?1) and p.status = 'ACTIVE'")
    Optional<PortalUser> findByUserId(String userId);
}
