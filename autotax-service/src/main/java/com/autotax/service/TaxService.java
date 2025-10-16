package com.autotax.service;

import com.autotax.domain.Tax;

import java.util.List;

public interface TaxService {

    void getAllTaxes();

    void getTaxById(Long id);

}
