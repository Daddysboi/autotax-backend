package com.autotax.service.impl;

import com.autotax.domain.Tax;
import com.autotax.dao.TaxRepository;
import com.autotax.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    @Override
    public void getAllTaxes() {
        return;
    }

    @Override
    public void getTaxById(Long id) {
        return;
    }

}
