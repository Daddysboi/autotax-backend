package com.autotax.dao;

import com.autotax.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findSettingByName(String name);

    Setting findByNameIgnoreCase(String name);
}
