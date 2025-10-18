package com.autotax.service;

import com.autotax.domain.Tax;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaxService {

    List<Tax> getAllTaxes();

    Optional<Tax> getTaxById(Long id);

    Tax saveTax(Tax tax);

    void deleteTax(Long id);

}
