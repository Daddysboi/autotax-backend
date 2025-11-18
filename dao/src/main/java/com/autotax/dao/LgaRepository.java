package com.autotax.dao;


import com.autotax.domain.Lga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Onyali Oscar
 * email: onyalioscar@gmail.com
 * 04/07/2022
 **/

@Repository
public interface LgaRepository extends JpaRepository<Lga, Long> {
    @Query("SELECT l FROM Lga l WHERE l.status = 'ACTIVE' AND l.id= ?1")
    Optional<Lga> getActiveLgaById(Long aLong);

    Optional<Lga> findByCode(String code);
}
