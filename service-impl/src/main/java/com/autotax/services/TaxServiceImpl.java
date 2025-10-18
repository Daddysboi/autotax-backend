package com.autotax.services;

import com.autotax.domain.Tax;
import com.autotax.dao.TaxRepository;
import com.autotax.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    @Override
    public List<Tax> getAllTaxes() {
        return null;
    }

    @Override
    public Optional<Tax> getTaxById(Long id) {
        return Optional.empty();
    }

    @Override
    public Tax saveTax(Tax tex) {
        return tex;
    }

    @Override
    public void deleteTax(Long id) {
    }
}
