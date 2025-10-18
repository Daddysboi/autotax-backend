package com.autotax.dao;

import com.autotax.domain.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {
    List<Tax> findByTaxpayerId(String taxpayerId);
}
