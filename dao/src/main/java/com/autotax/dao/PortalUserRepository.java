package com.autotax.dao;

import com.autotax.domain.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortalUserRepository extends JpaRepository<PortalUser, Long> {
    Optional<Object> findByEmailOrUsername(String email, String username);

    PortalUser findByUserId(String userId);
}
